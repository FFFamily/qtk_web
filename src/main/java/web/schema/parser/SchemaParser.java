package web.schema.parser;

import web.schema.obj.Schema;

public interface SchemaParser {
    public void check(Schema schema,Object resource,String path);

    public Boolean support(Schema schema);
}
