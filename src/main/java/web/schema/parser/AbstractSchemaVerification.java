package web.schema.parser;

import io.vertx.core.json.JsonObject;
import web.schema.obj.Schema;

public  class AbstractSchemaVerification  {

    public void check(Schema schema, JsonObject resource) {
        SchemaVerification.check(schema,resource);
    }

}

