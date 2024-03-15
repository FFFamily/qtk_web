package web.schema.parser;

import web.schema.obj.Schema;

public class DefaultSchemaParser extends AbstractSchemaVerification{
    @Override
    public void validate(Schema schema, Object resource, String root) {

    }



    @Override
    public Boolean support(Schema schema) {
        return true;
    }
}
