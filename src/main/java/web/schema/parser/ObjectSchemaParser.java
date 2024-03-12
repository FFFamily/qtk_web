package web.schema.parser;

import io.vertx.core.json.JsonObject;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;

import java.util.*;

public class ObjectSchemaParser extends AbstractSchemaVerification {
    private final StringBuilder errorMessage;

    public ObjectSchemaParser() {
        this.errorMessage = new StringBuilder();
    }

    /**
     * 决策树解析
     * @param schema
     * @param resource
     */

    public void check(Schema schema, Object resource,String root) {
        ObjectSchema objectSchema = (ObjectSchema) schema;
        LinkedHashMap<String,Object> objectResource;
        if (resource == null){
            throw new RuntimeException("当前路径为："+root+"期望值为 object，实际为 null");
        }
        if (resource.getClass() == LinkedHashMap.class){
             objectResource = (LinkedHashMap<String, Object>) resource;
        }else {
            throw new RuntimeException("当前路径为："+root+"期望值为 object，实际为"+resource.getClass().getSimpleName());
        }
        LinkedHashMap<String,Object> object = objectResource;
        HashMap<String, Schema> propertiesMap = objectSchema.getPropertiesMap();
        // 校验必备字段
        Optional.ofNullable(objectSchema.getRequires()).ifPresent(value -> {
            for (String require : value) {
                if (!object.containsKey(require)){
                    throw new RuntimeException("当前路径为："+root+"当前schema缺少字段："+require);
                }
            }
        });
        // 对字段校验
        for (Map.Entry<String, Schema> property : propertiesMap.entrySet()) {
            super.check(property.getValue(),object.get(property.getKey()),root+property.getKey());
        }
    }

}
