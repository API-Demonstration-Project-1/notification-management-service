package com.proarchs.notification.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.proarchs.notification.constants.PNMSConstants;
import com.proarchs.notification.exception.NotFoundException;
import com.proarchs.notification.factory.POJOFactory;
import com.proarchs.notification.factory.UIModelFactory;
import com.proarchs.notification.model.EmailInfo;
import com.proarchs.notification.model.EmailRegVerificationInfo;
import com.proarchs.notification.repository.EmailRegVerificationRepository;
import com.proarchs.notification.util.EmailSender;
import com.proarchs.notification.util.JsonFormatter;
import com.proarchs.notification.util.RandomStringGenerator;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@Service
public class JMSNotificationService {
	
	private static final Logger log = LoggerFactory.getLogger(JMSNotificationService.class);

	@Value("${twilio.acctsid}")
	private String twilioAcctSID;

	@Value("${twilio.authtoken}")
	private String twilioAuthToken;

	@Value("${twilio.service.verification.sid}")
	private String twilioVerificationServiceID;
	
	@Autowired
	private EmailSender mailSender;

	@JmsListener(destination = "${activemq.notification.otpverification.inbound}")
	@SendTo("${activemq.notification.otpverification.outbound}")
	public String sendOTPVerificationToken(final Message<String> jmsMessage) throws JsonMappingException, JsonProcessingException {
		
		// Extract all field values from JSON
		JsonNode nodes = JsonFormatter.convertStringToJsonNode(jmsMessage.getPayload());
		String mobileNum = nodes.get("mobileNum").textValue();
		String emailId = nodes.get("emailId").textValue();
		String tenantId = nodes.get("tenantId").textValue();
		
		// Send OTP - START
		Twilio.init(twilioAcctSID, twilioAuthToken);

		// via SMS
		Verification smsVerification = Verification.creator(twilioVerificationServiceID, mobileNum, PNMSConstants.CHANNEL_SMS).create();

		// via Email
		Verification emailVerification = Verification.creator(twilioVerificationServiceID, emailId, PNMSConstants.CHANNEL_EMAIL).create();
		// Send OTP - END

		StringBuilder strBuilder = new StringBuilder(smsVerification.getSid()).append(PNMSConstants.PIPE_SEPARATOR).append(emailVerification.getSid());

		// Prepare the response & send it to the Outbound Queue
		Map<String, String> elements = new HashMap<String, String>();
	    elements.put("tenantId", tenantId);
	    elements.put("verificationId", strBuilder.toString());
	    
	    String jsonResp = JsonFormatter.convertMapToJson(elements);
	    
		return jsonResp;
	}

	@JmsListener(destination = "${activemq.notification.otpverificationconfirmation.inbound}")
	@SendTo("${activemq.notification.otpverificationconfirmation.outbound}")
	public String checkOTPVerificationToken(final Message<String> jmsMessage) throws JsonProcessingException {
		// Extract all field values from JSON
		JsonNode nodes = JsonFormatter.convertStringToJsonNode(jmsMessage.getPayload());
		
		String mobileNumOrEmailId = null;
		if (nodes.get("emailId") != null) {
			mobileNumOrEmailId = nodes.get("emailId").textValue();
		}
		if (nodes.get("mobileNum") != null) {
			mobileNumOrEmailId = nodes.get("mobileNum").textValue();
		}
		String code = nodes.get("code").textValue();
		
		// Verify OTP - START
		Twilio.init(twilioAcctSID, twilioAuthToken);
		VerificationCheck verificationCheck = VerificationCheck.creator(twilioVerificationServiceID, code).setTo(mobileNumOrEmailId).create();
		// Verify OTP - END

		// Prepare the response & send it to the Outbound Queue - START
		Map<String, String> elements = new HashMap<String, String>(1);
		if (verificationCheck.getValid() && verificationCheck.getStatus().equals("approved")) {
		    elements.put("verificationId", verificationCheck.getSid());
		} else {
		    elements.put("mismatchError", "PNMS - Verification Code Does Not Match");
		}
		    
	    String jsonResp = JsonFormatter.convertMapToJson(elements);
	    
		return jsonResp;
		// Prepare the response & send it to the Outbound Queue - END
	}

	@Value("${notification.email.defaultFromAddress}")
	private String fromAddress;

	@Autowired
	private EmailRegVerificationRepository repo;
	

	@JmsListener(destination = "${activemq.notification.emailverification.inbound}")
	@SendTo("${activemq.notification.emailverification.outbound}")
	public String sendEmailVerification(final Message<String> jmsMessage) throws IllegalAccessException, InstantiationException, JsonMappingException, JsonProcessingException {
		
		// Extract all field values from JSON
		JsonNode nodes = JsonFormatter.convertStringToJsonNode(jmsMessage.getPayload());
		String tenantId = nodes.get("tenantId").textValue();
		String name = nodes.get("name").textValue();
		String emailId = nodes.get("emailId").textValue();
		String systemName = nodes.get("systemName").textValue();
		String systemShortName = nodes.get("systemShortName").textValue();
		String systemDesc = nodes.get("systemDesc").textValue();
		
		// Save the Info into DB - START
		EmailRegVerificationInfo verificationInfo = (EmailRegVerificationInfo)POJOFactory.getInstance("EMAILREGVERIFICATIONINFO");
		
		verificationInfo.setName(name);
		verificationInfo.setEmail(emailId);
		verificationInfo.setSystemName(systemShortName);
		verificationInfo.setVerificationCode(RandomStringGenerator.getRandomAlphaNumericString(15));
		
		repo.save(verificationInfo);
		// Save the Info into DB - END
		
		// Prepare contents required for Email
		EmailInfo emailInfo = (EmailInfo) UIModelFactory.getInstance("EMAILINFO");
		
		emailInfo.setFromAddress(fromAddress);
		emailInfo.setToAddress(emailId);
		emailInfo.setSubject(systemShortName.toUpperCase() + PNMSConstants.SINGLE_SPACE + PNMSConstants.HYPEN_SEPARATOR + PNMSConstants.SINGLE_SPACE + PNMSConstants.REG_VERIFICATION_EMAILSUBJECT);
		emailInfo.setTemplateName(PNMSConstants.WELCOME_TEMPLATE_KEY);

		Map<String, Object> contextVariables = new HashMap<String, Object>(6);
		contextVariables.put("name", name);
		contextVariables.put("systemName", systemName);
		contextVariables.put("systemShortName", systemShortName.toUpperCase());
		contextVariables.put("systemDesc", systemDesc);
		contextVariables.put("verificationId", verificationInfo.getVerificationId());
		contextVariables.put("verificationCode", verificationInfo.getVerificationCode());
	
		emailInfo.setContextVariables(contextVariables);
		
		// Send the Email
		mailSender.sendMail(emailInfo);
		
		// Prepare the response & send it to the Outbound Queue
		Map<String, String> elements = new HashMap<String, String>();
	    elements.put("tenantId", tenantId);
	    elements.put("verificationId", verificationInfo.getVerificationId().toString());
	    
	    String jsonResp = JsonFormatter.convertMapToJson(elements);
	    
		return jsonResp;
	}

	@JmsListener(destination = "${activemq.notification.emailverificationconfirmation.inbound}")
	@SendTo("${activemq.notification.emailverificationconfirmation.outbound}")
	public String checkEmailVerification(final Message<String> jmsMessage) throws NotFoundException, IllegalAccessException, InstantiationException, JsonMappingException, JsonProcessingException {
		
		// Extract all field values from JSON
		JsonNode nodes = JsonFormatter.convertStringToJsonNode(jmsMessage.getPayload());
		String verificationId = nodes.get("verificationId").textValue();
		String code = nodes.get("code").textValue();
				
		// Update 'VERIFICATION_CODE to null if matches
		Optional<EmailRegVerificationInfo> verificationInfoOpt = repo.findById(Integer.parseInt(verificationId));
		
		if (!verificationInfoOpt.isPresent()) {
			// Prepare the error response & send it to the Outbound Queue
			Map<String, String> elements = new HashMap<String, String>(1);
		    elements.put("notfoundError", "PNMS - Verification Entry Not Found");
		    
		    String jsonResp = JsonFormatter.convertMapToJson(elements);
		    
			return jsonResp;
		} 

		EmailRegVerificationInfo verificationInfo = verificationInfoOpt.get();
		
		if (verificationInfo.getVerificationCode().equals(code)) {
			verificationInfo.setVerificationCode(null);
			
			repo.save(verificationInfo);
		} else {
			// Prepare the error response & send it to the Outbound Queue
			Map<String, String> elements = new HashMap<String, String>(1);
		    elements.put("mismatchError", "PNMS - Verification Code Does Not Match");
		    
		    String jsonResp = JsonFormatter.convertMapToJson(elements);
		    
			return jsonResp;
		}
		
		// Prepare contents required for Email
		EmailInfo emailInfo = (EmailInfo) UIModelFactory.getInstance("EMAILINFO");
		
		emailInfo.setFromAddress(fromAddress);
		emailInfo.setToAddress(verificationInfo.getEmail());
		emailInfo.setSubject(verificationInfo.getSystemName().toUpperCase() + PNMSConstants.SINGLE_SPACE + PNMSConstants.HYPEN_SEPARATOR + PNMSConstants.SINGLE_SPACE + PNMSConstants.REG_VERIFICATION_CONFIRMATION_EMAILSUBJECT);
		emailInfo.setTemplateName(PNMSConstants.POSTVERIFICATION_TEMPLATE_KEY);

		Map<String, Object> contextVariables = new HashMap<String, Object>(2);
		contextVariables.put("name", verificationInfo.getName());
		contextVariables.put("systemShortName", verificationInfo.getSystemName().toUpperCase());
	
		emailInfo.setContextVariables(contextVariables);
		
		// Send the Email
		mailSender.sendMail(emailInfo);
		
		// Prepare the response & send it to the Outbound Queue
		Map<String, String> elements = new HashMap<String, String>(1);
	    elements.put("verificationId", verificationInfo.getVerificationId().toString());
	    
	    String jsonResp = JsonFormatter.convertMapToJson(elements);
	    
		return jsonResp;
	}
}
