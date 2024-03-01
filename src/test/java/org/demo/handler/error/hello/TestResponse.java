package org.demo.handler.error.hello;


import lombok.Builder;
import lombok.Data;
import web.annotation.api.ApiResponseSchema;

@ApiResponseSchema
@Data
@Builder
public class TestResponse {
    private String id;

}
