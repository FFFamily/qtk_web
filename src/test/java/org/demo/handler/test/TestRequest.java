package org.demo.handler.test;

import lombok.Data;
import web.annotation.ApiRequestSchema;

@ApiRequestSchema
@Data
public class TestRequest {
    private String id;
}
