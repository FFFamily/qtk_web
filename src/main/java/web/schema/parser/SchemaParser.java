package web.schema.parser;

import web.schema.obj.Schema;

public interface SchemaParser<T extends Schema> {
    public boolean check(T schema,Object resource);
}
