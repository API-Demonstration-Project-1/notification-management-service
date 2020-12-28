package com.proarchs.notification.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Emailverification
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-27T08:20:31.550Z")




public class Emailverification   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("emailId")
  private String emailId = null;

  @JsonProperty("systemName")
  private String systemName = null;

  @JsonProperty("systemShortName")
  private String systemShortName = null;

  @JsonProperty("systemDesc")
  private String systemDesc = null;

  public Emailverification name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the Customer
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

  public Emailverification emailId(String emailId) {
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

  public Emailverification systemName(String systemName) {
    this.systemName = systemName;
    return this;
  }

  /**
   * System Name
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

  public Emailverification systemShortName(String systemShortName) {
    this.systemShortName = systemShortName;
    return this;
  }

  /**
   * System Short Name
   * @return systemShortName
  **/
  @ApiModelProperty(required = true, value = "System Short Name")
  @NotNull


  public String getSystemShortName() {
    return systemShortName;
  }

  public void setSystemShortName(String systemShortName) {
    this.systemShortName = systemShortName;
  }

  public Emailverification systemDesc(String systemDesc) {
    this.systemDesc = systemDesc;
    return this;
  }

  /**
   * Description about the System
   * @return systemDesc
  **/
  @ApiModelProperty(required = true, value = "Description about the System")
  @NotNull


  public String getSystemDesc() {
    return systemDesc;
  }

  public void setSystemDesc(String systemDesc) {
    this.systemDesc = systemDesc;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Emailverification emailverification = (Emailverification) o;
    return Objects.equals(this.name, emailverification.name) &&
        Objects.equals(this.emailId, emailverification.emailId) &&
        Objects.equals(this.systemName, emailverification.systemName) &&
        Objects.equals(this.systemShortName, emailverification.systemShortName) &&
        Objects.equals(this.systemDesc, emailverification.systemDesc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, emailId, systemName, systemShortName, systemDesc);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Emailverification {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
    sb.append("    systemName: ").append(toIndentedString(systemName)).append("\n");
    sb.append("    systemShortName: ").append(toIndentedString(systemShortName)).append("\n");
    sb.append("    systemDesc: ").append(toIndentedString(systemDesc)).append("\n");
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

