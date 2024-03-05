package org.demo.handler.hello.schema;

import web.annotation.api.ApiRequestSchema;
import web.annotation.api.ApiResponseSchema;
import web.annotation.api.ApiSchema;
import web.schema.S;
import web.schema.Schema;
@ApiSchema
public class HelloSchema {
    @ApiRequestSchema
    public Schema request = S.object();

    @ApiResponseSchema
    public Schema response = S.object();
}
