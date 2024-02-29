package org.demo.handler.hello;


import lombok.Builder;
import lombok.Data;
import web.annotation.ApiResponseSchema;

@ApiResponseSchema
@Data
@Builder
public class TestResponse {
    private String id;

}
