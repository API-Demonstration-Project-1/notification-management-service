package com.proarchs.notification.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.proarchs.notification.factory.POJOFactory;
import com.proarchs.notification.model.audit.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * SubscriptionCustomerInfo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)

@Entity
@Table(name = "REG_VERIFICATION_INFO")
public class RegVerificationInfo extends Auditable<String> {
	
	static {
		POJOFactory.register("REGVERIFICATIONINFO", RegVerificationInfo.class);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VERIFICATION_ID", nullable = false)
	private Integer verificationId;

	@Column(name = "TWILIO_OTP_SID", nullable = false)
	private String twilioOtpSid;
	
	@Column(name = "NAME", nullable = false)
	private String name;
	
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@Column(name = "MOBILE", nullable = false)
	private String mobile;
	
	@Column(name = "VERIFICATION_CODE", nullable = false)
	private String verificationCode;
	
	@Column(name = "SYSTEM_NAME", nullable = false)
	private String systemName;
}
