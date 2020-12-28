package com.proarchs.notification.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proarchs.notification.factory.UIModelFactory;
import com.proarchs.notification.model.Data;

import io.swagger.annotations.ApiModelProperty;

/**
 * Data
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-24T18:38:38.170Z")
@Component

public class Data {

	static {
		UIModelFactory.register("DATA", Data.class);
	}
	
	@JsonProperty("id")
	  private String id = null;

	  public Data id(String id) {
	    this.id = id;
	    return this;
	  }

	  /**
	   * Get id
	   * @return id
	  **/
	  @ApiModelProperty(required = true, value = "")
	  @NotNull


	  public String getId() {
	    return id;
	  }

	  public void setId(String id) {
	    this.id = id;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Data data = (Data) o;
	    return Objects.equals(this.id, data.id);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(id);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Data {\n");
	    
	    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
