package web.ops;

import lombok.Builder;
import lombok.Data;

/**
 * web 服务启动参数配置
 */
@Data
@Builder
public class ServerOptions {
    // 端口
    private Integer port;
    // 地址
    private String host;
    // handler 地址
    private String handlerPackage;
}
