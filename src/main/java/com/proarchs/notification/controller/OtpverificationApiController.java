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
import com.proarchs.notification.model.Otpverification;
import com.proarchs.notification.services.NotificationService;
import com.proarchs.notification.util.ResponsePreparator;
import com.twilio.exception.ApiException;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-24T18:38:38.170Z")

@Controller
public class OtpverificationApiController implements OtpverificationApi {

    private static final Logger log = LoggerFactory.getLogger(OtpverificationApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private NotificationService service;
    
    @org.springframework.beans.factory.annotation.Autowired
    public OtpverificationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Otpverification>> otpverificationGET() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Otpverification>>(objectMapper.readValue("[ {  \"mobileNum\" : \"mobileNum\",  \"channel\" : \"channel\"}, {  \"mobileNum\" : \"mobileNum\",  \"channel\" : \"channel\"} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Otpverification>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Otpverification>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> otpverificationPOST(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Otpverification body) throws IllegalAccessException, InstantiationException {
    	log.info("otpverificationPOST() invoked");
    	log.debug("otpverificationPOST() invoked with Request body: " + body);
    	
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
            	Integer verificationId = service.sendOTPVerificationToken(body);
            	
        		String resp = ResponsePreparator.prepareOtpVerificationResponse(verificationId, "OTP Sent Successfully", true, null);
            	
            	log.info("otpverificationPOST() exited");
            	
            	return new ResponseEntity<String>(resp, HttpStatus.CREATED);
                
            } catch (ApiException ae) { // Twilio Exception
            	log.info("otpverificationPOST() exited with Errors");
            	log.error("Error in the Communication Platform", ae);
            	
            	String resp = ResponsePreparator.prepareOtpVerificationResponse(null, "Communication Platform Error - " + ae.getMessage(), false, -1);
            	
            	return new ResponseEntity<String>(resp, HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
            	log.info("otpverificationPOST() exited with Errors");
                log.error("Couldn't serialize response for content type application/json", e);
                
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }
}
