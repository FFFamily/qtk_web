package org.demo.handler.hello.schema;

import web.annotation.api.ApiRequestSchema;
import web.annotation.api.ApiResponseSchema;
import web.annotation.api.ApiSchema;
import web.schema.S;
import web.schema.obj.Schema;
@ApiSchema
public class HelloSchema {
    @ApiRequestSchema
    public static Schema request = S.object()
            .properties("type",S.integer())
            .properties("name",S.object()
                    .properties("id",S.object())
                    .require("id"))
            .toIf()
            .has("type")
            .end()
            .properties("age",S.integer())

            .require("name");

    @ApiResponseSchema
    public static Schema response = S.object();
}
