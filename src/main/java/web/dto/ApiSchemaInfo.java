package web.dto;

import lombok.Builder;
import lombok.Data;
import web.schema.obj.Schema;

@Data
@Builder
public class ApiSchemaInfo {
    private Schema request;
    public Schema response;
}
