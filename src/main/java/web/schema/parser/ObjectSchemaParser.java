package web.schema.parser;

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
     *
     * @param schema
     * @param resource
     */

    public void check(Schema schema, Object resource, String root) {
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
        HashMap<String, Schema> propertiesMap = objectSchema.getPropertiesMap();
        // 构建 object 校验策略树
        if (objectSchema.getRoot() != null) {
            // 有判断操作
            Deque<ObjectSchema.ObjectSchemaNode> deque = new LinkedList<>();
            deque.push(objectSchema.getRoot());
            while (!deque.isEmpty()) {
                ObjectSchema.ObjectSchemaNode node = deque.pop();
                if (node.getType().equals(ObjectSchema.NodeTypeEnum.IF.getCode())) {
//                    Optional<ObjectSchema.ObjectSchemaNode> end = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.END.getCode())).findFirst();
                    Optional<ObjectSchema.ObjectSchemaNode> elseNext = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.ELSE.getCode())).findFirst();
                    Optional<ObjectSchema.ObjectSchemaNode> elseIfNext = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.ELSE_IF.getCode())).findFirst();
                    // 校验列表
                    ObjectSchema.ObjectSchemaNode matchResult = match(node,object);
                    if (matchResult != null){
                        ObjectSchema.ObjectSchemaNode objectSchemaNode = matchResult;
                    }else if (elseIfNext.isPresent()){
                        ObjectSchema.ObjectSchemaNode objectSchemaNode = match(elseIfNext.get(),object);
                    }else {
                        if (elseNext.isPresent()) {
                            ObjectSchema.ObjectSchemaNode objectSchemaNode = match(elseNext.get(),object);
                        }
                    }
                }
                node.getChildren().forEach(deque::push);
            }
        }
        // 校验必备字段
        Optional.ofNullable(objectSchema.getRequires()).ifPresent(value -> {
            for (String require : value) {
                if (!object.containsKey(require)) {
                    throw new RuntimeException("当前路径为：" + root + "当前schema缺少字段：" + require);
                }
            }
        });
        // 对字段校验
        for (Map.Entry<String, Schema> property : propertiesMap.entrySet()) {
            super.check(property.getValue(), object.get(property.getKey()), root + property.getKey());
        }
    }

    private ObjectSchema.ObjectSchemaNode match(ObjectSchema.ObjectSchemaNode node,LinkedHashMap<String, Object> object){
        Optional<ObjectSchema.ObjectSchemaNode> end = node.getChildren().stream().filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.END.getCode())).findFirst();
        if (end.isPresent()){
            boolean result = node.getChildren().stream()
                    .filter(item -> item.getType().equals(ObjectSchema.NodeTypeEnum.END.getCode()))
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

}
