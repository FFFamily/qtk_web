package org.demo.handler.test;


import lombok.Builder;
import lombok.Data;
import web.annotation.api.ApiResponseSchema;

@Data
@Builder
public class TestResponse {
    private String id;

}
