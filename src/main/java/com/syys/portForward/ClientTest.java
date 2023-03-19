package com.syys.portForward;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        LoggingHandler logger = new LoggingHandler(LogLevel.DEBUG);
        try {
            Bootstrap bootstrap = new Bootstrap();
            Bootstrap b = bootstrap.group(workerGroup)
                                   .channel(NioSocketChannel.class)
                                   .option(ChannelOption.SO_KEEPALIVE, true)
                                   .handler(new ChannelInitializer<Channel>() {
                                       @Override
                                       protected void initChannel(Channel ch) throws Exception {
                                           ch.pipeline().addLast(logger);
                                       }
                                   });
            Channel channel = b.connect("127.0.0.1", 8080).sync().channel();



            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                ChannelPromise promise = channel.newPromise();
                promise.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        System.out.println("Message send successfully.");
                    } else {
                        System.err.println("Failed to send message: " + future.cause().getMessage());
                        future.cause().printStackTrace();
                    }
                });

                if ("bye".equals(line.toLowerCase())) {
                    channel.writeAndFlush(line + "\r\n", promise).addListener(ChannelFutureListener.CLOSE);
                } else {
                    channel.writeAndFlush(line + "\r\n", promise);
                }
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

