package com.proarchs.notification.model;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.proarchs.notification.factory.UIModelFactory;

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

@Component
public class EmailInfo {
	
	static {
		UIModelFactory.register("EMAILINFO", EmailInfo.class);
	}
	
	private String fromAddress;
	
	private String toAddress;

	private String subject;
	
	private String templateName;
	
	private Map<String, Object> contextVariables;
}
