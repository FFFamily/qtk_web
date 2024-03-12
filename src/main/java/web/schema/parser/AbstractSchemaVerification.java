package web.schema.parser;

import io.vertx.core.json.JsonObject;
import web.exception.CouldNotBuildRouteException;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;

public class AbstractSchemaVerification  {
    public void check(Schema schema, Object resource,String root) {
        if (schema instanceof ObjectSchema objectSchema){
            new ObjectSchemaParser().check(objectSchema,resource,root);
        }else {
            throw new CouldNotBuildRouteException("不支持的schema校验");
        }
    }

}

