package com.smile.client.client;

import com.alibaba.fastjson.JSONObject;
import com.smile.common.action.Action;
import com.smile.common.event.EventPool;
import com.smile.common.event.IEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import javax.swing.*;
import java.net.URI;

public class WebSocketClient {

    private URI uri;

    private Bootstrap bootstrap;

    private EventLoopGroup group;

    private ChannelPromise channelPromise;

    private Channel channel;

    public WebSocketClient(URI uri) {
        this.uri = uri;
        this.init();
    }

    public void send(Action action,String payload){
        if (null == action){
            System.out.println("action is empty");
            return;
        }
        if (null == payload|| payload.isEmpty()){
            System.out.println("payload is empty");
            return;
        }
        action.setPayload(payload);
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(action)));


    }

    public  void connect(){
        try {
            channel= bootstrap.connect(uri.getHost(),uri.getPort()).sync().channel();
            channelPromise.sync();
            System.out.println("connected success! and handshake complete ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init(){
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.option(ChannelOption.TCP_NODELAY,true);

        group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(64*1024));
                        pipeline.addLast(new WebSocketHandler(getHandshaker(uri)));
                    }

                    private WebSocketClientHandshaker getHandshaker(final URI uri){
                        return WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,null,false,null);
                    }

                });
    }

    private class WebSocketHandler extends SimpleChannelInboundHandler<Object>{

        private WebSocketClientHandshaker handshaker;

        public WebSocketHandler(WebSocketClientHandshaker handshaker){
            this.handshaker = handshaker;
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            channelPromise = ctx.newPromise();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            this.handshaker.handshake(ctx.channel());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            System.out.println("receive data :"+o+" from address: "+channelHandlerContext.channel().remoteAddress());
            if (! handshaker.isHandshakeComplete()){
                try {
                    handshaker.finishHandshake(channelHandlerContext.channel(),(FullHttpResponse) o);
                    channelPromise.setSuccess();
                    System.out.println("handshake success !");
                } catch (Exception e) {
                    e.printStackTrace();
                    channelPromise.setFailure(e);
                }
            }
            if (! (o instanceof TextWebSocketFrame)){
                System.out.println("receive no text data:"+o);
                return;
            }

            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) o;
            System.out.println("receive text :"+textWebSocketFrame.text());

            Action action =null;
            try {
                 action = JSONObject.parseObject(textWebSocketFrame.text(), Action.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("transfer to object from json string failed. data: " + textWebSocketFrame.text());
                return;
            }


            IEvent<Action,Action> event = EventPool.getInstance().find(action.getAction());

            if (null == event){
                System.out.println("no event found for key: " + action.getAction());
                return;
            }

            Action respAction = event.handle(action, channelHandlerContext.channel());

            if (null!=respAction){
                System.out.println("resp action: " + action);
                channelHandlerContext.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(respAction)));
            }


        }
    }
}
