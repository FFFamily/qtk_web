package web.schema.parser;

import io.vertx.core.json.JsonObject;
import web.exception.CouldNotBuildRouteException;
import web.schema.obj.AbstractSchema;
import web.schema.obj.IntegerSchema;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;

public abstract class AbstractSchemaVerification implements SchemaParser {

    public abstract <T extends Schema> void  validate(T schema, Object resource, String path);

    public void check(Schema schema, Object resource,String path) {
        if (schema instanceof ObjectSchema){
            new ObjectSchemaParser().validate(schema,resource,path);
        }else if (schema instanceof IntegerSchema){
            new IntegerSchemaParser().validate(schema,resource,path);
        }
    }

}

