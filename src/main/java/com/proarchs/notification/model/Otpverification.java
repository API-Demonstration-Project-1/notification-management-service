package com.proarchs.notification.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Otpverification
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-24T18:38:38.170Z")

public class Otpverification {

	@JsonProperty("mobileNum")
	  private String mobileNum = null;

	  @JsonProperty("emailId")
	  private String emailId = null;

	  public Otpverification mobileNum(String mobileNum) {
	    this.mobileNum = mobileNum;
	    return this;
	  }

	  /**
	   * Mobile No. of the Customer
	   * @return mobileNum
	  **/
	  @ApiModelProperty(required = true, value = "Mobile No. of the Customer")
	  @NotNull


	  public String getMobileNum() {
	    return mobileNum;
	  }

	  public void setMobileNum(String mobileNum) {
	    this.mobileNum = mobileNum;
	  }

	  public Otpverification emailId(String emailId) {
	    this.emailId = emailId;
	    return this;
	  }

	  /**
	   * Email of the Customer
	   * @return emailId
	  **/
	  @ApiModelProperty(required = true, value = "Email of the Customer")
	  @NotNull


	  public String getEmailId() {
	    return emailId;
	  }

	  public void setEmailId(String emailId) {
	    this.emailId = emailId;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Otpverification otpverification = (Otpverification) o;
	    return Objects.equals(this.mobileNum, otpverification.mobileNum) &&
	        Objects.equals(this.emailId, otpverification.emailId);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(mobileNum, emailId);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Otpverification {\n");
	    
	    sb.append("    mobileNum: ").append(toIndentedString(mobileNum)).append("\n");
	    sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

	  /**
	   * Convert the given object to string with each line indented by 4 spaces
	   * (except the first line).
	   */
	  private String toIndentedString(java.lang.Object o) {
	    if (o == null) {
	      return "null";
	    }
	    return o.toString().replace("\n", "\n    ");
	  }
}
