package web.schema.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import web.exception.FailToBuildSchemaException;

import java.util.*;

@Data
public class ObjectSchema extends AbstractSchema {
    private final HashMap<String, Schema> propertiesMap;
    private ObjectSchemaNode root;
    private String[] requires;
    private Schema ifSchema;

    public ObjectSchema() {
        this.propertiesMap = new HashMap<>();
    }

    public ObjectSchema(Integer type) {
        this.propertiesMap = new HashMap<>();
        root = ObjectSchemaNode
                .builder()
                .type(type)
                .children(new LinkedList<>())
                .build();
    }

    private ObjectSchemaNode createRoot(Integer type){
        // 懒加载
        // 只有当涉及到if等复杂操作时才会尝试构建树
        return ObjectSchemaNode
                .builder()
                .type(type)
                .children(new LinkedList<>())
                .build();
    }

    public ObjectSchema require(String... requireProperties) {
        requires = requireProperties;
        return this;
    }

    public ObjectSchema properties(String keyName, Schema schema) {
        propertiesMap.put(keyName, schema);
//        root.children.add(buildNode(keyName));
        return this;
    }

    public ObjectSchema toIf() {
        ObjectSchema ifSchema = new ObjectSchema(2);
        ObjectSchemaNode currentSchema = ifSchema.root;
        if (root == null){
            root = createRoot(1);
        }
        if (root.type != 1) {
            throw new FailToBuildSchemaException("if 只能去修饰 schema");
        }
        root.children.add(currentSchema);
        currentSchema.parent = root;
        return ifSchema;
    }

    public ObjectSchema end() {
        if (root.type != 2 && root.type != 3 && root.type != 4) {
            // 父节点只能是判断位
            throw new FailToBuildSchemaException("只有在有判断符的前提下才能使用判断条件");
        }
        ObjectSchema node = new ObjectSchema(6);
        Optional<ObjectSchemaNode> otherThenSchema = root.children.stream().filter(item -> item.type.equals(6)).findFirst();
        otherThenSchema.ifPresent(value -> root.children.remove(value));
        root.children.add(node.root);
        node.root.parent = root;
//        ObjectSchemaNode temp = node;
//        while (!temp.getType().equals(NodeTypeEnum.IF.getCode())){
//            temp = temp.parent;
//        }
        return node;
    }

    public ObjectSchema has(String... keyName) {
        if (root.type != 2 && root.type != 3 && root.type != 4) {
            // 父节点只能是判断位
            throw new FailToBuildSchemaException("只有在有判断符的前提下才能使用判断条件");
        }
        ObjectSchemaNode match = buildJudgeNode(5);
        match.setMatchType("has");
        match.setMatchResource(keyName);
        root.children.add(match);
        return this;
    }

    public ObjectSchema isNull(String... keyName) {
        return this;
    }

    public ObjectSchema isNotNull(String... keyName) {
        return this;
    }

    public ObjectSchema equals(String keyName,Object target) {
        return this;
    }


    public ObjectSchema toElse() {
        ObjectSchema elseSchema = new ObjectSchema(4);
        // 如果是if那么子节点都是判断规则
        ObjectSchemaNode currentSchema = elseSchema.root;
        if ( root.type != 6) {
            // 构建错误
            throw new FailToBuildSchemaException("else 前必须为 then");
        }
        root.children.add(elseSchema.root);
        currentSchema.parent = root;
        return elseSchema;
    }

    public ObjectSchema toElseIf() {
        ObjectSchema elseIfSchema = new ObjectSchema(3);
        // 如果是if那么子节点都是判断规则
        ObjectSchemaNode currentSchema = elseIfSchema.root;
        if (root.type != 2) {
            // 构建错误
            throw new FailToBuildSchemaException("else if 前必须为 if ");
        }
        root.children.add(elseIfSchema.root);
        currentSchema.parent = root;
        return elseIfSchema;
    }

    private ObjectSchemaNode buildNode(String schemaKey) {
        return ObjectSchemaNode.builder().schemaKey(schemaKey).type(1).build();
    }

    private ObjectSchemaNode buildJudgeNode(Integer type) {
        return ObjectSchemaNode.builder().type(type).build();
    }

    @Data
    @Builder
    public static class ObjectSchemaNode {
        // schema 1 | if 2 | else if 3 | else 4 | match（判断条件）5 | then 6
        private Integer type;
        private String matchType;
        private Object[] matchResource;
        private String schemaKey;
        // 不能封装自己，会有栈溢出的情况
//        private Schema schema;
        private ObjectSchemaNode parent;
        private List<ObjectSchemaNode> children;
    }

    @Getter
    @AllArgsConstructor
    public enum NodeTypeEnum{
        SCHEMA(1),
        IF(2),
        ELSE_IF(3),
        ELSE(4),
        MATCH(5),
        END(6);
        private final Integer code;
    }
}
