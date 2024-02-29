package org.demo.handler.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import web.annotation.ApiRequestSchema;

@ApiRequestSchema
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRequest {
    private String id;
}
