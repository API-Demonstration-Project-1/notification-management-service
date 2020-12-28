package com.proarchs.notification.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Emailverificationcheck
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-27T08:20:31.550Z")




public class Emailverificationcheck   {
  @JsonProperty("verificationCode")
  private String verificationCode = null;

  public Emailverificationcheck verificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
    return this;
  }

  /**
   * This is the Verification Code
   * @return verificationCode
  **/
  @ApiModelProperty(required = true, value = "This is the Verification Code")
  @NotNull


  public String getVerificationCode() {
    return verificationCode;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Emailverificationcheck emailverificationcheck = (Emailverificationcheck) o;
    return Objects.equals(this.verificationCode, emailverificationcheck.verificationCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verificationCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Emailverificationcheck {\n");
    
    sb.append("    verificationCode: ").append(toIndentedString(verificationCode)).append("\n");
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

