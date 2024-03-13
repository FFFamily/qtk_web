package web.schema.obj;

import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
public class ObjectSchema extends AbstractSchema{
    private final HashMap<String, Schema> propertiesMap;
    private ObjectSchemaNode root;
    private String[] requires;
    private Schema ifSchema;
    public ObjectSchema(){
        this(1);
    }

    public ObjectSchema(Integer type){
        this.propertiesMap = new HashMap<>();
        root = ObjectSchemaNode
                .builder()
                .type(type)
                .children(new ArrayList<>())
                .build();
    }

    public ObjectSchema require(String... requireProperties){
        requires = requireProperties;
        return this;
    }
    public ObjectSchema properties(String keyName,Schema schema){
        propertiesMap.put(keyName,schema);
        root.children.add(buildNode(keyName));
        return this;
    }

    public ObjectSchema toIf(){
        ObjectSchema ifSchema = new ObjectSchema(2);
        // 如果是if那么子节点都是判断规则
        root.children.add(ifSchema.root);
        return ifSchema;
    }

    private ObjectSchemaNode buildNode(String schemaKey){
        return ObjectSchemaNode.builder().schemaKey(schemaKey).type(1).build();
    }

    private ObjectSchemaNode buildJudgeNode(Integer type){
        return ObjectSchemaNode.builder().type(type).build();
    }

    @Data
    @Builder
    static class ObjectSchemaNode{
        // schema 1 | if 2 | else if 3 | else 4
        private Integer type;
        private String schemaKey;
        private ObjectSchemaNode parent;
        private List<ObjectSchemaNode> children;
    }
}
