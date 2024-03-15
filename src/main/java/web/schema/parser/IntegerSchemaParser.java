package web.schema.parser;

import web.schema.obj.IntegerSchema;
import web.schema.obj.Schema;

/**
 * 整型schema校验
 */
public class IntegerSchemaParser extends AbstractSchemaVerification{

    @Override
    public void validate(Schema schema, Object resource, String root) {

    }

    @Override
    public Boolean support(Schema schema) {
        return schema instanceof IntegerSchema;
    }
}
