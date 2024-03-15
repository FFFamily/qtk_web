package web.schema.obj;

import lombok.*;
import web.exception.FailToBuildSchemaException;

import java.util.*;

/**
 * 实现方案
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class ObjectSchema extends AbstractSchema {
    private final HashMap<String, Schema> propertiesMap;
//    private ObjectSchemaNode root;
    private String[] requires;
    private Schema ifSchema;
    private Integer type;
    private String matchType;
    private Object[] matchResource;
    private String schemaKey;;
    private ObjectSchema parent;
    private List<ObjectSchema> children;

    public ObjectSchema() {
        this(NodeTypeEnum.SCHEMA.code);
    }

    public ObjectSchema(Integer type) {
        this.propertiesMap = new HashMap<>();
        this.type = type;
        this.children = new ArrayList<>();
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
        if (this.type != 1) {
            throw new FailToBuildSchemaException("if 只能去修饰 schema");
        }
        ifSchema.parent = this;
        return ifSchema;
    }

    public ObjectSchema endIf(){
        ObjectSchema p = this.parent;
        this.parent = null;
        p.children.add(this);
        return p;
    }


    public ObjectSchema then() {
        if (this.type != 2 && this.type != 3 && this.type != 4) {
            // 父节点只能是判断位
            throw new FailToBuildSchemaException("只有在有判断符的前提下才能使用判断条件");
        }
        ObjectSchema node = new ObjectSchema(6);
        Optional<ObjectSchema> otherThenSchema = this.children.stream().filter(item -> item.type.equals(6)).findFirst();
        otherThenSchema.ifPresent(value -> this.children.remove(value));
        node.parent = this;
        return node;
    }

    public ObjectSchema end(){
        ObjectSchema p = this.parent;
        this.parent = null;
        p.children.add(this);
        return p;
    }

    public ObjectSchema has(String... keyName) {
        if (this.type != 2 && this.type != 3 && this.type != 4) {
            // 父节点只能是判断位
            throw new FailToBuildSchemaException("只有在有判断符的前提下才能使用判断条件");
        }
        ObjectSchema match = buildJudgeNode(5);
        match.setMatchType("has");
        match.setMatchResource(keyName);
        this.children.add(match);
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
        if ( this.type != 2 && this.type != 3) {
            throw new FailToBuildSchemaException("else 前必须为 if 或者 else if");
        }
        elseSchema.parent = this;
        return this;
    }

    public ObjectSchema toElseIf() {
        ObjectSchema elseIfSchema = new ObjectSchema(3);
        // 如果是if那么子节点都是判断规则
        if (this.type != 2) {
            // 构建错误
            throw new FailToBuildSchemaException("else if 前必须为 if ");
        }
        elseIfSchema.parent = this;
        return elseIfSchema;
    }

    private ObjectSchema buildNode(String schemaKey) {
        return ObjectSchema.builder().schemaKey(schemaKey).type(1).build();
    }

    private ObjectSchema buildJudgeNode(Integer type) {
        return ObjectSchema.builder().type(type).build();
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
