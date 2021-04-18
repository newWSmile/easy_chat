package com.smile.client.event;

import com.alibaba.fastjson.JSONObject;
import com.smile.common.action.Action;
import com.smile.common.action.LoginRespAction;
import com.smile.common.event.IEvent;
import io.netty.channel.Channel;

public class LoginEvent implements IEvent<Action,Action> {

    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("receive action: " + action);
        LoginRespAction respAction = JSONObject.parseObject(action.getPayload(), LoginRespAction.class);
        System.out.println("received login action resp: " + respAction);
        return null;
    }
}
