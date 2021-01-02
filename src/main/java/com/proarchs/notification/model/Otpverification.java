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
	@JsonProperty("name")
	private String name = null;

	@JsonProperty("mobileNum")
	private String mobileNum = null;

	@JsonProperty("emailId")
	private String emailId = null;

	@JsonProperty("systemName")
	private String systemName = null;

	public Otpverification name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of the Customer
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "Name of the Customer")
	@NotNull

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Otpverification mobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
		return this;
	}
	/**
	 * Mobile No. of the Customer
	 * 
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
	 * 
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

	/**
	 * System Name
	 * 
	 * @return systemName
	 **/
	@ApiModelProperty(required = true, value = "System Name")
	@NotNull

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
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
		return Objects.equals(this.name, otpverification.name)
				&& Objects.equals(this.mobileNum, otpverification.mobileNum)
				&& Objects.equals(this.emailId, otpverification.emailId)
				&& Objects.equals(this.systemName, otpverification.systemName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, mobileNum, emailId, systemName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Otpverification {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    mobileNum: ").append(toIndentedString(mobileNum)).append("\n");
		sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
		sb.append("    systemName: ").append(toIndentedString(systemName)).append("\n");
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
