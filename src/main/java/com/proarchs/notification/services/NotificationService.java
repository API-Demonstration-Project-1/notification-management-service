package com.proarchs.notification.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.proarchs.notification.constants.PNMSConstants;
import com.proarchs.notification.exception.NotFoundException;
import com.proarchs.notification.factory.POJOFactory;
import com.proarchs.notification.factory.UIModelFactory;
import com.proarchs.notification.model.EmailInfo;
import com.proarchs.notification.model.EmailRegVerificationInfo;
import com.proarchs.notification.model.Emailverification;
import com.proarchs.notification.repository.EmailRegVerificationRepository;
import com.proarchs.notification.util.EmailTemplateLoader;
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
	
	@Autowired
	JmsTemplate jmsTemplate;

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

		jmsTemplate.convertAndSend(verificationCheck.getSid());
		
		return verificationCheck.getStatus();

	}

	@Value("${notification.email.defaultFromAddress}")
	private String fromAddress;

	@Qualifier("emailTemplateEngine")
	@Autowired
	private TemplateEngine stringTemplateEngine;

	@Autowired
	private EmailRegVerificationRepository repo;
	
	private static final String BACKGROUND_IMAGE = "email-templates/images/background.png";
	private static final String LOGO_BACKGROUND_IMAGE = "email-templates/images/logo-background.png";
	private static final String THYMELEAF_BANNER_IMAGE = "email-templates/images/proarchs_logo_orange_bkg.jpg";

	private static final String PNG_MIME = "image/png";

	@Autowired
	private JavaMailSender javaMailSender;
	
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
		sendEmail(emailInfo);
		
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
		sendEmail(emailInfo);
	}
	
	@Async
	private void sendEmail(EmailInfo emailInfo) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.addHeader("Content-Type", PNMSConstants.JSON_CONTENT_TYPE);

				final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, PNMSConstants.UNICODE_ENCODING_TYPE);
				message.setFrom(emailInfo.getFromAddress());
				message.setTo(emailInfo.getToAddress());
				message.setSubject(emailInfo.getSubject());
				
				Context ctx = new Context();
				emailInfo.getContextVariables().forEach((key,value) -> ctx.setVariable(key, value));

				// Create the HTML body using Thymeleaf
				String bodyContent = stringTemplateEngine.process(EmailTemplateLoader.getTemplate(emailInfo.getTemplateName()), ctx);
				message.setText(bodyContent, true /* isHtml */);

				// Add the inline images, referenced from the HTML code as "cid:image-name"
				message.addInline("background", new ClassPathResource(BACKGROUND_IMAGE), PNG_MIME);
				message.addInline("logo-background", new ClassPathResource(LOGO_BACKGROUND_IMAGE), PNG_MIME);
				message.addInline("thymeleaf-banner", new ClassPathResource(THYMELEAF_BANNER_IMAGE), PNG_MIME);
			}
		};
		
		// Send email
		javaMailSender.send(preparator);
	}
}
