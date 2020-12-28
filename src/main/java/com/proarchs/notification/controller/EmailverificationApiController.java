package com.proarchs.notification.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proarchs.notification.model.Emailverification;
import com.proarchs.notification.services.NotificationService;
import com.proarchs.notification.util.ResponsePreparator;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-27T03:25:09.277Z")

@Controller
public class EmailverificationApiController implements EmailverificationApi {

    private static final Logger log = LoggerFactory.getLogger(EmailverificationApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    @Autowired
    private NotificationService service;

    @org.springframework.beans.factory.annotation.Autowired
    public EmailverificationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> emailverificationEmailverificationcheckGET(@ApiParam(value = "",required=true) @PathVariable("verificationId") String verificationId,@NotNull @ApiParam(value = "This is the Verification Code", required = true) @Valid @RequestParam(value = "code", required = true) String code) {
    	log.info("emailverificationEmailverificationcheckPOST() invoked");
    	log.debug("emailverificationEmailverificationcheckPOST() invoked with URI Parameter: " + verificationId + " & Query Parameter: " + code);
    	
    	try {
        	service.checkEmailVerification(Integer.parseInt(verificationId), code);
        	
    		String resp = ResponsePreparator.prepareEmailVerificationCheckResponse("Email Verification Done Successfully", true, null);
        	
        	log.info("emailverificationEmailverificationcheckPOST() exited");
        	
        	return new ResponseEntity<String>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
        	log.info("emailverificationEmailverificationcheckPOST() exited with Errors");
        	
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Emailverification>> emailverificationGET() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Emailverification>>(objectMapper.readValue("[ {  \"name\" : \"name\",  \"emailId\" : \"emailId\",  \"emailSubject\" : \"emailSubject\"}, {  \"name\" : \"name\",  \"emailId\" : \"emailId\",  \"emailSubject\" : \"emailSubject\"} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Emailverification>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Emailverification>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> emailverificationPOST(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Emailverification body) {
    	log.info("emailverificationPOST() invoked");
    	log.debug("emailverificationPOST() invoked with Request body: " + body);
    	
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
            	Integer verificationId = service.sendEmailVerification(body);
            	
        		String resp = ResponsePreparator.prepareEmailVerificationResponse(verificationId, "Email Verification Sent Successfully", true, null);
            	
            	log.info("emailverificationPOST() exited");
            	
            	return new ResponseEntity<String>(resp, HttpStatus.CREATED);
                
            } catch (Exception e) {
            	log.info("emailverificationPOST() exited with Errors");
            	
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }

}
