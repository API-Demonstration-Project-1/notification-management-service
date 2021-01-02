package com.proarchs.notification.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Otpverificationcheck
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-26T15:48:10.047Z")

public class Otpverificationcheck {
	@JsonProperty("verificationId")
	private Integer verificationId = null;

	@JsonProperty("mobileNum")
	private String mobileNum = null;

	@JsonProperty("emailId")
	private String emailId = null;

	@JsonProperty("code")
	private String code = null;

	public Otpverificationcheck verificationId(Integer verificationId) {
		this.verificationId = verificationId;
		return this;
	}
	
	/**
	 * Verification ID of PNMS
	 * 
	 * @return mobileNum
	 **/
	@ApiModelProperty(value = "Verification ID of PNMS")

	public Integer getVerificationId() {
		return verificationId;
	}

	public void setVerificationId(Integer verificationId) {
		this.verificationId = verificationId;
	}

	public Otpverificationcheck mobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
		return this;
	}

	/**
	 * Mobile No. of the Customer
	 * 
	 * @return mobileNum
	 **/
	@ApiModelProperty(value = "Mobile No. of the Customer")

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public Otpverificationcheck emailId(String emailId) {
		this.emailId = emailId;
		return this;
	}

	/**
	 * Email of the Customer
	 * 
	 * @return emailId
	 **/
	@ApiModelProperty(value = "Email of the Customer")

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Otpverificationcheck code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Received OTP
	 * 
	 * @return code
	 **/
	@ApiModelProperty(required = true, value = "Received OTP")
	@NotNull

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Otpverificationcheck otpverificationcheck = (Otpverificationcheck) o;
		return 	Objects.equals(this.verificationId, otpverificationcheck.verificationId)
				&& Objects.equals(this.mobileNum, otpverificationcheck.mobileNum)
				&& Objects.equals(this.emailId, otpverificationcheck.emailId)
				&& Objects.equals(this.code, otpverificationcheck.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(verificationId, mobileNum, emailId, code);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Otpverificationcheck {\n");
		sb.append("    verificationId: ").append(toIndentedString(verificationId)).append("\n");
		sb.append("    mobileNum: ").append(toIndentedString(mobileNum)).append("\n");
		sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
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
