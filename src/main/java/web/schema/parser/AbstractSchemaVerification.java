package web.schema.parser;

import web.schema.obj.Schema;

public  class AbstractSchemaVerification  {

    public void check(Schema schema, Object resource) {
        SchemaVerification.check(schema,resource);
    }

}

