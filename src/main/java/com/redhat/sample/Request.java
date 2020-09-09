package com.redhat.sample;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName(value = "request")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class Request {

	@JsonProperty("field1")
  public String field1;

  @JsonProperty("field2")
  public String field2;


  public Request() {
  }

  public Request(String field1, String field2) {
    this.field1 = field1;
    this.field2 = field2;
  }

  public String getField1() {
    return field1;
  }

  public void setField1(String field1) {
    this.field1 = field1;
  }

  public String getField2() {
    return field2;
  }

  public void setField2(String field2) {
    this.field2 = field2;
  }
  
}