package com.proarchs.notification.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.proarchs.notification.constants.PNMSConstants;
import com.proarchs.notification.exception.NotFoundException;
import com.proarchs.notification.factory.POJOFactory;
import com.proarchs.notification.factory.UIModelFactory;
import com.proarchs.notification.model.EmailInfo;
import com.proarchs.notification.model.EmailRegVerificationInfo;
import com.proarchs.notification.model.Emailverification;
import com.proarchs.notification.repository.EmailRegVerificationRepository;
import com.proarchs.notification.util.EmailSender;
import com.proarchs.notification.util.RandomStringGenerator;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@Service
public class NotificationService {
	
	@Autowired
	private EmailSender mailSender;

	@Value("${twilio.acctsid}")
	private String twilioAcctSID;

	@Value("${twilio.authtoken}")
	private String twilioAuthToken;

	@Value("${twilio.service.verification.sid}")
	private String twilioVerificationServiceID;
	
	public String sendOTPVerificationToken(String mobileNum, String emailId) {
		Twilio.init(twilioAcctSID, twilioAuthToken);

		// via SMS
		Verification smsVerification = Verification
				.creator(twilioVerificationServiceID, mobileNum, PNMSConstants.CHANNEL_SMS).create();

		// via Email
		Verification emailVerification = Verification
				.creator(twilioVerificationServiceID, emailId, PNMSConstants.CHANNEL_EMAIL).create();

		StringBuilder strBuilder = new StringBuilder(smsVerification.getSid()).append(PNMSConstants.PIPE_SEPARATOR)
				.append(emailVerification.getSid());

		return strBuilder.toString();
	}

	public String checkOTPVerificationToken(String mobileNumOrEmailId, String code) {
		Twilio.init(twilioAcctSID, twilioAuthToken);

		VerificationCheck verificationCheck = VerificationCheck.creator(twilioVerificationServiceID, code).setTo(mobileNumOrEmailId).create();

		return verificationCheck.getStatus();

	}

	@Value("${notification.email.defaultFromAddress}")
	private String fromAddress;
	
	@Autowired
	private EmailRegVerificationRepository repo;
	
	public Integer sendEmailVerification(Emailverification requestInfo) throws IllegalAccessException, InstantiationException {
		// Save the Info into DB
		EmailRegVerificationInfo verificationInfo = (EmailRegVerificationInfo)POJOFactory.getInstance("EMAILREGVERIFICATIONINFO");
		
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

	
	public void checkEmailVerification(Integer verificationId, String code) throws NotFoundException, IllegalAccessException, InstantiationException {
		// Update 'VERIFICATION_CODE to null if matches
		Optional<EmailRegVerificationInfo> verificationInfoOpt = repo.findById(verificationId);
		
		if (!verificationInfoOpt.isPresent()) {
			throw new NotFoundException(404, "PNMS - Verification Entry Not Found");
		} 

		EmailRegVerificationInfo verificationInfo = verificationInfoOpt.get();
		
		if (verificationInfo.getVerificationCode().equals(code)) {
			verificationInfo.setVerificationCode(null);
			
			repo.save(verificationInfo);
		} else {}
		
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
	}
	
}
