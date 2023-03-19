package com.syys.portForward;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerPortForwarder {

    private final String remoteHost;
    private final int remotePort;

    public ServerPortForwarder(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        LoggingHandler logger = new LoggingHandler(LogLevel.DEBUG);

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .handler(new ChannelInitializer<Channel>() {
                 @Override
                 protected void initChannel(Channel ch) throws Exception {
                     ch.pipeline().addLast(logger);
                     ch.pipeline().addLast(new IdleStateHandler(0, 0, 10));
                     ch.pipeline().addLast(new RemoteForwardHandler());
                 }
             });

            Channel remoteChannel = b.connect(remoteHost, remotePort).sync().channel();
            System.out.println("Connected to remote server " + remoteHost + ":" + remotePort);

            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(new ChannelInitializer<Channel>() {
                  @Override
                  protected void initChannel(Channel ch) throws Exception {
                      ch.pipeline().addLast(logger);
                      ch.pipeline().addLast(new LocalForwardHandler(remoteChannel));
                  }
              });

            ChannelFuture future = sb.bind(8080).sync();
            System.out.println("Server started and listening on port 8080");

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private class RemoteForwardHandler extends ChannelInboundHandlerAdapter {

        private Channel inboundChannel;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            inboundChannel = ctx.channel();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            // TODO: 对收到的数据进行处理，例如加密、解密等操作
            inboundChannel.writeAndFlush(in).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    future.channel().close();
                }
            });
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            inboundChannel.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            closeOnFlush(ctx.channel());
        }

        private void closeOnFlush(Channel channel) {
            if (channel.isActive()) {
                channel.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
            } else {
                channel.close();
            }
        }
    }

    private class LocalForwardHandler extends ChannelInboundHandlerAdapter {

        private final Channel remoteChannel;

        public LocalForwardHandler(Channel remoteChannel) {
            this.remoteChannel = remoteChannel;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client connected from " + ctx.channel().remoteAddress());
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            // TODO: 对收到的数据进行处理，例如加密、解密等操作
            if (!remoteChannel.isOpen()) {
                //  会经过OutboundHandler
                ctx.writeAndFlush("remote channel is close!").addListener(ChannelFutureListener.CLOSE);
                // TODO 怎么写 才不会经过 out链路
                return;
            }
            remoteChannel.writeAndFlush(in).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client disconnected from " + ctx.channel().remoteAddress());
            remoteChannel.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            closeOnFlush(ctx.channel());
        }

        private void closeOnFlush(Channel channel) {
            if (channel.isActive()) {
                channel.writeAndFlush("").addListener(ChannelFutureListener.CLOSE);
            } else {
                channel.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ServerPortForwarder forwarder = new ServerPortForwarder("www.baidu.com", 80);
        // ServerPortForwarder forwarder = new ServerPortForwarder("127.0.0.1", 9999);
        forwarder.run();
    }
}
