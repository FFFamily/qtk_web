package web.schema.parser;

import web.schema.obj.AbstractSchema;
import web.schema.obj.ObjectSchema;
import web.schema.obj.Schema;

import java.util.*;

public class ObjectSchemaParser extends AbstractSchemaVerification {

    private void checkObject(ObjectSchema objectSchema,LinkedHashMap<String, Object> object,String path){
        if (objectSchema == null){
            return;
        }
        // 校验必备字段
        Optional.ofNullable(objectSchema.getRequires()).ifPresent(value -> {
            for (String require : value) {
                if (!object.containsKey(require)) {
                    throw new RuntimeException("当前路径为：" + path + "当前schema缺少字段：" + require);
                }
            }
        });
        // 对字段校验
        HashMap<String, Schema> propertiesMap = objectSchema.getPropertiesMap();
        for (Map.Entry<String, Schema> property : propertiesMap.entrySet()) {
            check(property.getValue(), object.get(property.getKey()), path + property.getKey());
        }
    }

    private ObjectSchema match(ObjectSchema node,LinkedHashMap<String, Object> object){
        Optional<ObjectSchema> end = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.END.getCode())).findFirst();
        if (end.isPresent()){
            boolean result = node.getChildren().stream()
                    .filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.MATCH.getCode()))
                    .anyMatch(match -> {
                        String actionType = match.getMatchType();
                        if (actionType.equals("has")) {
                            return Arrays.stream((String[]) match.getMatchResource()).anyMatch(object::containsKey);
                        }
                        return false;
                    });
            if (result){
                return end.get();
            }else {
                return null;
            }
        }else {
            return null;
        }
    }


    @Override
    public Boolean support(Schema schema) {
        return schema instanceof ObjectSchema;
    }

    @Override
    public  void validate(Schema schema, Object resource, String root) {
        ObjectSchema objectSchema = (ObjectSchema) schema;
        LinkedHashMap<String, Object> objectResource;
        if (resource == null) {
            throw new RuntimeException("当前路径为：" + root + "期望值为 object，实际为 null");
        }
        if (resource.getClass() == LinkedHashMap.class) {
            objectResource = (LinkedHashMap<String, Object>) resource;
        } else {
            throw new RuntimeException("当前路径为：" + root + "期望值为 object，实际为" + resource.getClass().getSimpleName());
        }
        LinkedHashMap<String, Object> object = objectResource;
        // 构建 object 校验策略树
        if (objectSchema.getChildren() != null) {
            // 有判断操作
            Deque<ObjectSchema> deque = new LinkedList<>(objectSchema.getChildren());
//            deque.push(objectSchema.getChildren());
            while (!deque.isEmpty()) {
                ObjectSchema node = deque.pop();
                if (node.getType().equals(ObjectSchema.NodeTypeEnum.IF.getCode())) {
//                    Optional<ObjectSchema.ObjectSchemaNode> end = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.END.getCode())).findFirst();
                    Optional<ObjectSchema> elseNext = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.ELSE.getCode())).findFirst();
                    Optional<ObjectSchema> elseIfNext = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.ELSE_IF.getCode())).findFirst();
                    // 校验列表
                    ObjectSchema matchResult = match(node,object);
                    ObjectSchema objectSchemaNode;
                    if (matchResult != null){
                        objectSchemaNode = matchResult;
                    }else if (elseIfNext.isPresent()){
                        objectSchemaNode = match(elseIfNext.get(),object);
                    }else {
                        objectSchemaNode = elseNext.map(value -> match(value, object)).orElse(null);
                    }
                    checkObject(objectSchemaNode,object,root);
                }
//                node.getChildren().forEach(deque::push);
            }
        }
        checkObject(objectSchema,object,root);
    }
}
