package com.proarchs.notification.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proarchs.notification.factory.UIModelFactory;

import io.swagger.annotations.ApiModelProperty;

/**
 * Otpverificationresponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-24T18:38:38.170Z")
@Component

public class Otpverificationresponse {

	static {
		UIModelFactory.register("OTPVERIFICATIONRESP", Otpverificationresponse.class);
	}

	 @JsonProperty("success")
	  private Boolean success = null;

	  @JsonProperty("message")
	  private String message = null;

	  @JsonProperty("data")
	  private Data data = null;

	  @JsonProperty("error_code")
	  private Integer errorCode = null;

	  public Otpverificationresponse success(Boolean success) {
	    this.success = success;
	    return this;
	  }

	  /**
	   * Get success
	   * @return success
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull


	  public Boolean isSuccess() {
	    return success;
	  }

	  public void setSuccess(Boolean success) {
	    this.success = success;
	  }

	  public Otpverificationresponse message(String message) {
	    this.message = message;
	    return this;
	  }

	  /**
	   * Get message
	   * @return message
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull


	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }

	  public Otpverificationresponse data(Data data) {
	    this.data = data;
	    return this;
	  }

	  /**
	   * Get data
	   * @return data
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull

	  @Valid

	  public Data getData() {
	    return data;
	  }

	  public void setData(Data data) {
	    this.data = data;
	  }

	  public Otpverificationresponse errorCode(Integer errorCode) {
	    this.errorCode = errorCode;
	    return this;
	  }

	  /**
	   * Get errorCode
	   * @return errorCode
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull


	  public Integer getErrorCode() {
	    return errorCode;
	  }

	  public void setErrorCode(Integer errorCode) {
	    this.errorCode = errorCode;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Otpverificationresponse otpverificationresponse = (Otpverificationresponse) o;
	    return Objects.equals(this.success, otpverificationresponse.success) &&
	        Objects.equals(this.message, otpverificationresponse.message) &&
	        Objects.equals(this.data, otpverificationresponse.data) &&
	        Objects.equals(this.errorCode, otpverificationresponse.errorCode);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(success, message, data, errorCode);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Otpverificationresponse {\n");
	    
	    sb.append("    success: ").append(toIndentedString(success)).append("\n");
	    sb.append("    message: ").append(toIndentedString(message)).append("\n");
	    sb.append("    data: ").append(toIndentedString(data)).append("\n");
	    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
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
