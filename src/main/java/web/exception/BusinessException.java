package web.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class BusinessException extends RuntimeException{
    private Integer code;
    private String errorMessage;
    public BusinessException(Integer code,String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
