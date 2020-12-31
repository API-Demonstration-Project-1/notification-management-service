package com.proarchs.notification.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proarchs.notification.constants.PNMSConstants;
import com.proarchs.notification.model.Otpverificationcheck;
import com.proarchs.notification.services.NotificationService;
import com.proarchs.notification.util.ResponsePreparator;
import com.twilio.exception.ApiException;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-26T15:48:10.047Z")

@Controller
public class OtpverificationcheckApiController implements OtpverificationcheckApi {

    private static final Logger log = LoggerFactory.getLogger(OtpverificationcheckApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    @Autowired
    private NotificationService service;

    @org.springframework.beans.factory.annotation.Autowired
    public OtpverificationcheckApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Otpverificationcheck>> otpverificationcheckGET() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Otpverificationcheck>>(objectMapper.readValue("[ {  \"mobileNum\" : \"mobileNum\",  \"code\" : \"code\",  \"emailId\" : \"emailId\"}, {  \"mobileNum\" : \"mobileNum\",  \"code\" : \"code\",  \"emailId\" : \"emailId\"} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Otpverificationcheck>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Otpverificationcheck>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> otpverificationcheckPOST(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Otpverificationcheck body) throws IllegalAccessException, InstantiationException {
    	log.info("otpverificationcheckPOST() invoked");
    	log.debug("otpverificationcheckPOST() invoked with Request body: " + body);
    	
    	String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
            	String status = service.checkOTPVerificationToken((body.getEmailId() != null ? body.getEmailId() : body.getMobileNum()), body.getCode());
            	
            	if (status.equals(PNMSConstants.OTPCHECK_APPROVED)) {
            		String resp = ResponsePreparator.prepareOtpVerificationCheckResponse(null, "OTP Verified Successfully", true, null);
                	
                	log.info("otpverificationcheckPOST() exited");
                	
                	return new ResponseEntity<String>(resp, HttpStatus.CREATED);
            	} else {
            		String resp = ResponsePreparator.prepareOtpVerificationCheckResponse(null, "OTP Verification Failed - OTP Not Valid", true, null);
                	
                	log.info("otpverificationcheckPOST() exited");
                	
                	return new ResponseEntity<String>(resp, HttpStatus.BAD_REQUEST);
            	}
            } catch (ApiException ae) {
            	log.info("otpverificationcheckPOST() exited with Errors");
            	log.error("Error in the Communication Platform", ae);
            	
            	String resp = ResponsePreparator.prepareOtpVerificationCheckResponse(null, "Communication Platform Error - " + ae.getMessage(), false, -1);
            	
            	return new ResponseEntity<String>(resp, HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
            	log.info("otpverificationcheckPOST() exited with Errors");
                log.error("Couldn't serialize response for content type application/json", e);
                
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }

}
