package web.schema.parser;

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
     * @param schema
     * @param resource
     */

    public static void check(Schema schema, Object resource){
        Stack<Schema> stacks = new Stack<>();
//        VerificationInfo verificationInfo = new VerificationInfo();
        if (schema instanceof ObjectSchema objectSchema){
            AbstractSchemaVerification parser = new ObjectSchemaParser();
            parser.check(objectSchema,resource);
        }else {
            throw new CouldNotBuildRouteException("不支持的schema校验");
        }
    }
}
