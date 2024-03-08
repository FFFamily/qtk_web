package web.schema.parser;

import io.vertx.core.json.JsonObject;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public void check(Schema schema, Object resource) {
        ObjectSchema objectSchema = (ObjectSchema) schema;
        Map<String, Object> object;
        if (resource == null){
            object = new HashMap<>();
        }else {
            object = JsonObject.mapFrom(resource).getMap();
        }

        HashMap<String, Schema> propertiesMap = objectSchema.getPropertiesMap();
        // 校验必备字段
        Optional.ofNullable(objectSchema.getRequires()).ifPresent(value -> {
            for (String require : value) {
                if (!object.containsKey(require)){
                    throw new RuntimeException("当前schema缺少字段："+require);
                }
            }
        });
        // 对字段校验
        for (Map.Entry<String, Schema> property : propertiesMap.entrySet()) {
            super.check(property.getValue(),object.get(property.getKey()));
        }
    }

}
