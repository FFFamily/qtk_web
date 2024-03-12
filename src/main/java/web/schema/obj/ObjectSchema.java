package web.schema.obj;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

@Data
public class ObjectSchema extends AbstractSchema{
    private final HashMap<String, Schema> propertiesMap;
    private String[] requires;
    private Queue<SchemaItem> query = new LinkedList<>();
    public ObjectSchema(){
        this.propertiesMap = new HashMap<>();
    }

    public ObjectSchema require(String... requireProperties){
        requires = requireProperties;
        return this;
    }
    public ObjectSchema properties(String keyName,Schema schema){
        this.propertiesMap.put(keyName,schema);
        return this;
    }

    public ObjectSchema toIf(Boolean flag){
        query.add(SchemaItem.builder()
                .type(0)
                        .sing("if")
                .build());
        return this;
    }

    @Data
    @Builder
    static class SchemaItem{
        // 判断符号 0 || schema 1
        private Integer type;
        private String sing;
        private Schema schema;
    }
}
