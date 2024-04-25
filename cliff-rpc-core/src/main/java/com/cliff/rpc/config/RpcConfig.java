package com.cliff.rpc.config;

import lombok.Data;

/**
 * @Author: TravisKey
 */
@Data
public class RpcConfig {
    private String name = "cliff-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;
}
