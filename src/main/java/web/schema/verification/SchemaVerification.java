package web.schema.verification;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import web.exception.CouldNotBuildRouteException;
import web.schema.S;
import web.schema.obj.IntegerSchema;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;
import web.schema.parser.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

// 解析策略接口
public class SchemaVerification {

    private static final List<AbstractSchemaVerification> list = new LinkedList<>();
    static {
        list.add(new ObjectSchemaParser());
        list.add(new IntegerSchemaParser());
    }

    /**
     * 每个schema 的解析可以转为一个解析树
     * todo 似乎可以用模板方法
     * @param schema
     * @param resource
     */
    public static void check(Schema schema, Object resource){
        Object o = JsonObject.mapFrom(resource).getMap().get("request");
        AbstractSchemaVerification parser = list.stream().filter(item -> item.support(schema)).findFirst().orElse(new DefaultSchemaParser());
        parser.validate(schema,o,".");
    }
}
