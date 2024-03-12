package web.schema.parser;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import web.exception.CouldNotBuildRouteException;
import web.schema.S;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;

import java.util.Stack;

// 解析策略接口
public class SchemaVerification {
    /**
     * 每个schema 的解析可以转为一个解析树
     * todo 似乎可以用模板方法
     * @param schema
     * @param resource
     */
    public static void check(Schema schema, Object resource){
        Object o = JsonObject.mapFrom(resource).getMap().get("request");
        if (schema instanceof ObjectSchema objectSchema){
            AbstractSchemaVerification parser = new ObjectSchemaParser();
            parser.check(objectSchema,o,".");
        }else {
            throw new CouldNotBuildRouteException("不支持的schema校验");
        }
    }
}
