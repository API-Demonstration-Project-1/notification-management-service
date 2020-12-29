package com.proarchs.notification.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.proarchs.notification.constants.PNMSConstants;
import com.proarchs.notification.model.EmailInfo;

@Component
public class EmailSender {
	
	@Qualifier("emailTemplateEngine")
	@Autowired
	private TemplateEngine stringTemplateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final String BACKGROUND_IMAGE = "email-templates/images/background.png";
	private static final String LOGO_BACKGROUND_IMAGE = "email-templates/images/logo-background.png";
	private static final String THYMELEAF_BANNER_IMAGE = "email-templates/images/proarchs_logo_orange_bkg.jpg";

	private static final String PNG_MIME = "image/png";
	
	@Async
	public void sendMail(EmailInfo emailInfo) {
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
