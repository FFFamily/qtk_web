package web.schema.obj;

import lombok.Data;

import java.util.HashMap;
@Data
public class ObjectSchema extends AbstractSchema{
    private final HashMap<String, Schema> propertiesMap;
    private String[] requires;

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
}
