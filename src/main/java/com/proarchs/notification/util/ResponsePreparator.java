package com.proarchs.notification.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.proarchs.notification.factory.UIModelFactory;
import com.proarchs.notification.model.Data;
import com.proarchs.notification.model.Emailverificationresponse;
import com.proarchs.notification.model.Otpverificationcheckresponse;
import com.proarchs.notification.model.Otpverificationresponse;

@Component
public class ResponsePreparator {

	static {
		mapper = new ObjectMapper();
	}

	private static ObjectMapper mapper;

	private static boolean toExcludeNull;

	@Value("${config.response.format.exclude.null}")
	public void setToExcludeNull(boolean value) {
		toExcludeNull = value;
	}

	public static String prepareOtpVerificationResponse(Object dataInfo, String msg, boolean isSuccess,
			Integer errorCode) throws IllegalAccessException, InstantiationException {

		Data data = null;

		if (dataInfo != null) {
			data = (Data) UIModelFactory.getInstance("DATA");
			data.setId((Integer)dataInfo);
		}

		Otpverificationresponse otpVerificationResponse = (Otpverificationresponse) UIModelFactory
				.getInstance("OTPVERIFICATIONRESP");
		otpVerificationResponse.setData(data);
		otpVerificationResponse.setErrorCode(errorCode);
		otpVerificationResponse.setMessage(msg);
		otpVerificationResponse.setSuccess(isSuccess);

		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		if (toExcludeNull)
			mapper.setSerializationInclusion(Include.NON_NULL);
		else
			mapper.setSerializationInclusion(Include.ALWAYS);

		String otpVerificationRespStr = null;
		try {
			otpVerificationRespStr = mapper.writeValueAsString(otpVerificationResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return otpVerificationRespStr;
	}

	public static String prepareOtpVerificationCheckResponse(Object dataInfo, String msg, boolean isSuccess,
			Integer errorCode) throws IllegalAccessException, InstantiationException {

		Data data = null;

		if (dataInfo != null) {
			data = (Data) UIModelFactory.getInstance("DATA");
			data.setId((Integer) dataInfo);
		}

		Otpverificationcheckresponse otpVerificationCheckResponse = (Otpverificationcheckresponse) UIModelFactory
				.getInstance("OTPVERIFICATIONCHECKRESP");
		otpVerificationCheckResponse.setData(data);
		otpVerificationCheckResponse.setErrorCode(errorCode);
		otpVerificationCheckResponse.setMessage(msg);
		otpVerificationCheckResponse.setSuccess(isSuccess);

		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		if (toExcludeNull)
			mapper.setSerializationInclusion(Include.NON_NULL);
		else
			mapper.setSerializationInclusion(Include.ALWAYS);

		String otpVerificationCheckRespStr = null;
		try {
			otpVerificationCheckRespStr = mapper.writeValueAsString(otpVerificationCheckResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return otpVerificationCheckRespStr;
	}

	public static String prepareEmailVerificationResponse(Object dataInfo, String msg, boolean isSuccess, Integer errorCode) throws IllegalAccessException, InstantiationException {

		Data data = null;

		if (dataInfo != null) {
			data = (Data) UIModelFactory.getInstance("DATA");
			data.setId((Integer)dataInfo);
		}

		Emailverificationresponse emailVerificationResponse = (Emailverificationresponse) UIModelFactory.getInstance("EMAILVERIFICATIONRESP");
		emailVerificationResponse.setData(data);
		emailVerificationResponse.setErrorCode(errorCode);
		emailVerificationResponse.setMessage(msg);
		emailVerificationResponse.setSuccess(isSuccess);

		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		if (toExcludeNull)
			mapper.setSerializationInclusion(Include.NON_NULL);
		else
			mapper.setSerializationInclusion(Include.ALWAYS);

		String emailVerificationRespStr = null;
		try {
			emailVerificationRespStr = mapper.writeValueAsString(emailVerificationResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return emailVerificationRespStr;
	}
	
	public static String prepareEmailVerificationCheckResponse(String msg, boolean isSuccess, Integer errorCode) throws IllegalAccessException, InstantiationException {

		Emailverificationresponse emailVerificationResponse = (Emailverificationresponse) UIModelFactory.getInstance("EMAILVERIFICATIONRESP");
		emailVerificationResponse.setData(null);
		emailVerificationResponse.setErrorCode(errorCode);
		emailVerificationResponse.setMessage(msg);
		emailVerificationResponse.setSuccess(isSuccess);

		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		if (toExcludeNull)
			mapper.setSerializationInclusion(Include.NON_NULL);
		else
			mapper.setSerializationInclusion(Include.ALWAYS);

		String emailVerificationRespStr = null;
		try {
			emailVerificationRespStr = mapper.writeValueAsString(emailVerificationResponse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return emailVerificationRespStr;
	}
}
