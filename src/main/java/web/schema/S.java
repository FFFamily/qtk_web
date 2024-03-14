package web.schema;

import web.schema.obj.IntegerSchema;
import web.schema.obj.ObjectSchema;

public class S {
    public static ObjectSchema object(){
        return new ObjectSchema();
    }

    public static IntegerSchema integer(){
        return new IntegerSchema();
    }
}
