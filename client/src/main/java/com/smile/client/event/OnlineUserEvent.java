package com.smile.client.event;

import com.alibaba.fastjson.JSONObject;
import com.smile.common.action.Action;
import com.smile.common.action.FetchOnlineUsersRespAction;
import com.smile.common.event.IEvent;
import io.netty.channel.Channel;

public class OnlineUserEvent implements IEvent<Action,Action> {

    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("received action: " + action);
        FetchOnlineUsersRespAction respAction = JSONObject.parseObject(action.getPayload(), FetchOnlineUsersRespAction.class);
        System.out.println("receive online users: " + respAction);
        return null;
    }
}
