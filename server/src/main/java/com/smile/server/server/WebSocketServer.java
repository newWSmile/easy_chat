package com.smile.server.server;

import com.smile.server.connection.ConnectionPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Setter;

public class WebSocketServer {

    @Setter
    private String contextPath;

    private ServerBootstrap bootstrap;

    private EventLoopGroup boss;

    private EventLoopGroup worker;


    public WebSocketServer(final String contextPath) {
        this.contextPath = contextPath;
    }


    private void init() {
        bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

        //初始化线程池
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup(5);
        //初始化bootstrap的配置
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));
                        pipeline.addLast(new WebSocketHandler());

                    }
                });

    }

    public  void start(final Integer port) {
        this.init();
        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("server start success... listen on : "+port+" !");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("connected from address:" + ctx.channel().remoteAddress());
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("connection closed with address:" + ctx.channel().remoteAddress());
            ConnectionPool.getInstance().removeByChannelId(ctx.channel().id().asLongText());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            System.out.println("receive data : " + o + " from address:" + channelHandlerContext.channel().remoteAddress());

            if (!(o instanceof TextWebSocketFrame)){
                System.out.println("receive error message ,o: "+o);
                return ;
            }

            TextWebSocketFrame textWebSocketFrame = ((TextWebSocketFrame) o);
            System.out.println("receive text :"+ textWebSocketFrame.text());
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame("服务端返回: "+textWebSocketFrame.text()));


        }
    }

}
