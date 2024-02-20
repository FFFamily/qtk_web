package ops;

import lombok.Data;

/**
 * web 服务启动参数配置
 */
@Data
public class ServerOptions {
    // 端口
    private Integer port;
    // 地址
    private String host;
}
