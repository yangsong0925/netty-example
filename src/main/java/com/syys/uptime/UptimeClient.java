package com.syys.uptime;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/12/21 10:32
 */
/**
 * 定期连接到服务器以测量和打印服务器的正常运行时间。本示例演示如何在 Netty 中实现可靠的重连机制。
 */
public class UptimeClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

    /**
     * 在重新连接尝试之前休眠 5 秒。
     */
    static final int RECONNECT_DELAY = Integer.parseInt(System.getProperty("reconnectDelay", "5"));
    /**
     * 当服务器 10 秒未发送任何内容时重新连接。
     */
    private static final int READ_TIMEOUT = Integer.parseInt(System.getProperty("readTimeout", "10"));
    private static final UptimeClientHandler handler = new UptimeClientHandler();
    private static final Bootstrap bs = new Bootstrap();


    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        bs.group(group)
            .channel(NioSocketChannel.class)
            .remoteAddress(HOST, PORT)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IdleStateHandler(READ_TIMEOUT, 0, 0), handler);
                }
            });
        bs.connect();
    }

    static void connect() {
        bs.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.cause() != null) {
                    handler.startTime = -1;
                    handler.println("Failed to connect: " + future.cause());
                }
            }
        });
    }

}
