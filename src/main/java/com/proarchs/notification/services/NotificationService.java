package com.proarchs.notification.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.proarchs.notification.constants.PNMSConstants;
import com.proarchs.notification.exception.MismatchException;
import com.proarchs.notification.exception.NotFoundException;
import com.proarchs.notification.factory.POJOFactory;
import com.proarchs.notification.factory.UIModelFactory;
import com.proarchs.notification.model.EmailInfo;
import com.proarchs.notification.model.Emailverification;
import com.proarchs.notification.model.Otpverification;
import com.proarchs.notification.model.Otpverificationcheck;
import com.proarchs.notification.model.RegVerificationInfo;
import com.proarchs.notification.repository.EmailRegVerificationRepository;
import com.proarchs.notification.util.EmailSender;
import com.proarchs.notification.util.JsonFormatter;
import com.proarchs.notification.util.RandomStringGenerator;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@Service
public class NotificationService {

	@Value("${twilio.acctsid}")
	private String twilioAcctSID;

	@Value("${twilio.authtoken}")
	private String twilioAuthToken;

	@Value("${twilio.service.verification.sid}")
	private String twilioVerificationServiceID;
	
	@Value("${notification.email.defaultFromAddress}")
	private String fromAddress;
	
	@Autowired
	private EmailRegVerificationRepository repo;
	
	@Autowired
	private EmailSender mailSender;
	
	@Autowired
	@Qualifier("emailVerificationConfirmationJmsTemplate")
	private JmsTemplate emailVerificationConfirmationJmsTemplate;
	
	public Integer sendOTPVerificationToken(Otpverification requestInfo) throws IllegalAccessException, InstantiationException {
		Twilio.init(twilioAcctSID, twilioAuthToken);

		// via SMS
		Verification smsVerification = Verification.creator(twilioVerificationServiceID, requestInfo.getMobileNum(), PNMSConstants.CHANNEL_SMS).create();

		// via Email
		Verification emailVerification = Verification.creator(twilioVerificationServiceID, requestInfo.getEmailId(), PNMSConstants.CHANNEL_EMAIL).create();

		StringBuilder strBuilder = new StringBuilder(smsVerification.getSid()).append(PNMSConstants.PIPE_SEPARATOR).append(emailVerification.getSid());
		
		// Save the Info into DB - START
		RegVerificationInfo verificationInfo = (RegVerificationInfo)POJOFactory.getInstance("REGVERIFICATIONINFO");
		
		verificationInfo.setName(requestInfo.getName());
		verificationInfo.setEmail(requestInfo.getEmailId());
		verificationInfo.setMobile(requestInfo.getMobileNum());
		verificationInfo.setSystemName(requestInfo.getSystemName());
		verificationInfo.setTwilioOtpSid(strBuilder.toString());
		
		repo.save(verificationInfo);
		// Save the Info into DB - END

		return verificationInfo.getVerificationId();
	}

	public boolean checkOTPVerificationToken(Otpverificationcheck requestInfo) throws NotFoundException, IllegalAccessException, InstantiationException {
		Twilio.init(twilioAcctSID, twilioAuthToken);

		VerificationCheck verificationCheck = VerificationCheck.creator(twilioVerificationServiceID, requestInfo.getCode()).setTo(requestInfo.getEmailId() != null ? requestInfo.getEmailId() : requestInfo.getMobileNum()).create();

		if (verificationCheck.getValid() && verificationCheck.getStatus().equals(PNMSConstants.TWILIO_APPROVED_STATUS)) {
			// Fetch Verification Entry from DB
			Optional<RegVerificationInfo> verificationInfoOpt = repo.findById(requestInfo.getVerificationId());
			
			if (!verificationInfoOpt.isPresent()) {
				throw new NotFoundException(404, "PNMS - Verification Entry Not Found");
			} 

			RegVerificationInfo verificationInfo = verificationInfoOpt.get();
					
			// Prepare contents required for Email - START
			EmailInfo emailInfo = (EmailInfo) UIModelFactory.getInstance("EMAILINFO");
			
			emailInfo.setFromAddress(fromAddress);
			emailInfo.setToAddress(verificationInfo.getEmail());
			emailInfo.setSubject(verificationInfo.getSystemName().toUpperCase() + PNMSConstants.SINGLE_SPACE + PNMSConstants.HYPEN_SEPARATOR + PNMSConstants.SINGLE_SPACE + PNMSConstants.REG_VERIFICATION_CONFIRMATION_EMAILSUBJECT);
			emailInfo.setTemplateName(PNMSConstants.POSTVERIFICATION_TEMPLATE_KEY);
	
			Map<String, Object> contextVariables = new HashMap<String, Object>(2);
			contextVariables.put("name", verificationInfo.getName());
			contextVariables.put("systemShortName", verificationInfo.getSystemName().toUpperCase());
		
			emailInfo.setContextVariables(contextVariables);
			// Prepare contents required for Email - END
			
			// Send the Email
			mailSender.sendMail(emailInfo);
			
			return true;
		} else {
			return false;
		}
	}
	
	public Integer sendEmailVerification(Emailverification requestInfo) throws IllegalAccessException, InstantiationException {
		// Save the Info into DB
		RegVerificationInfo verificationInfo = (RegVerificationInfo)POJOFactory.getInstance("REGVERIFICATIONINFO");
		
		verificationInfo.setName(requestInfo.getName());
		verificationInfo.setEmail(requestInfo.getEmailId());
		verificationInfo.setSystemName(requestInfo.getSystemShortName());
		verificationInfo.setVerificationCode(RandomStringGenerator.getRandomAlphaNumericString(15));
		
		repo.save(verificationInfo);
		
		// Prepare contents required for Email
		EmailInfo emailInfo = (EmailInfo) UIModelFactory.getInstance("EMAILINFO");
		
		emailInfo.setFromAddress(fromAddress);
		emailInfo.setToAddress(requestInfo.getEmailId());
		emailInfo.setSubject(requestInfo.getSystemShortName().toUpperCase() + PNMSConstants.SINGLE_SPACE + PNMSConstants.HYPEN_SEPARATOR + PNMSConstants.SINGLE_SPACE + PNMSConstants.REG_VERIFICATION_EMAILSUBJECT);
		emailInfo.setTemplateName(PNMSConstants.WELCOME_TEMPLATE_KEY);

		Map<String, Object> contextVariables = new HashMap<String, Object>(6);
		contextVariables.put("name", verificationInfo.getName());
		contextVariables.put("systemName", requestInfo.getSystemName());
		contextVariables.put("systemShortName", requestInfo.getSystemShortName().toUpperCase());
		contextVariables.put("systemDesc", requestInfo.getSystemDesc());
		contextVariables.put("verificationId", verificationInfo.getVerificationId());
		contextVariables.put("verificationCode", verificationInfo.getVerificationCode());
	
		emailInfo.setContextVariables(contextVariables);
		
		// Send the Email
		mailSender.sendMail(emailInfo);
		
		return verificationInfo.getVerificationId();
	}

	public void checkEmailVerification(Integer verificationId, String code) throws NotFoundException, MismatchException, IllegalAccessException, InstantiationException, JsonProcessingException {
		// Update 'VERIFICATION_CODE to null if matches
		Optional<RegVerificationInfo> verificationInfoOpt = repo.findById(verificationId);
		
		if (!verificationInfoOpt.isPresent()) {
			throw new NotFoundException(404, "PNMS - Verification Entry Not Found");
		} 

		RegVerificationInfo verificationInfo = verificationInfoOpt.get();
		
		if (verificationInfo.getVerificationCode().equals(code)) {
			verificationInfo.setVerificationCode(null);
			
			repo.save(verificationInfo);
		} else {
			throw new MismatchException(500, "PNMS - Verification Code Does Not Match");
		}
		
		// Prepare the response & send it to the Outbound Queue - START
		Map<String, String> elements = new HashMap<String, String>(1);
	    elements.put("verificationId", verificationInfo.getVerificationId().toString());
	    
	    String jsonResp = JsonFormatter.convertMapToJson(elements);
	    
	    emailVerificationConfirmationJmsTemplate.convertAndSend(jsonResp);
	    // Prepare the response & send it to the Outbound Queue - END
		
		// Prepare contents required for Email - START
		EmailInfo emailInfo = (EmailInfo) UIModelFactory.getInstance("EMAILINFO");
		
		emailInfo.setFromAddress(fromAddress);
		emailInfo.setToAddress(verificationInfo.getEmail());
		emailInfo.setSubject(verificationInfo.getSystemName().toUpperCase() + PNMSConstants.SINGLE_SPACE + PNMSConstants.HYPEN_SEPARATOR + PNMSConstants.SINGLE_SPACE + PNMSConstants.REG_VERIFICATION_CONFIRMATION_EMAILSUBJECT);
		emailInfo.setTemplateName(PNMSConstants.POSTVERIFICATION_TEMPLATE_KEY);

		Map<String, Object> contextVariables = new HashMap<String, Object>(2);
		contextVariables.put("name", verificationInfo.getName());
		contextVariables.put("systemShortName", verificationInfo.getSystemName().toUpperCase());
	
		emailInfo.setContextVariables(contextVariables);
		// Prepare contents required for Email - END
		
		// Send the Email
		mailSender.sendMail(emailInfo);
	}
	
}
