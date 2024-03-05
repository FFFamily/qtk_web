package web.schema;

import java.util.HashMap;

public class ObjectSchema extends AbstractSchema{
    private final HashMap<String,Schema> propertiesMap;
    public ObjectSchema(){
        this.propertiesMap = new HashMap<>();
    }
    public ObjectSchema properties(String keyName,Schema schema){
        this.propertiesMap.put(keyName,schema);
        return this;
    }
}
