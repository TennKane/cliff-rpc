package com.cliff.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.cliff.rpc.RpcApplication;
import com.cliff.rpc.model.RpcRequest;
import com.cliff.rpc.model.RpcResponse;
import com.cliff.rpc.model.ServiceMetaInfo;
import com.cliff.rpc.protocol.ProtocolConstant;
import com.cliff.rpc.protocol.ProtocolMessage;
import com.cliff.rpc.protocol.ProtocolMessageSerializerEnum;
import com.cliff.rpc.protocol.ProtocolMessageTypeEnum;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Vertx TCP 请求客户端
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class VertxTcpClient {

    public void start(){
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888,"localhost",result->{
            if(result.succeeded()){
                System.out.println("Connect");
                NetSocket socket = result.result();
                socket.write("hello");
                socket.handler(buffer -> {
                    System.out.println("Recived"+buffer.toString());
                });
            }else {
                System.out.println("Fail");
            }
        });
    }
}
