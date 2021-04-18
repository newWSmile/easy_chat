package com.smile.server.event;

import com.alibaba.fastjson.JSONObject;
import com.smile.common.action.Action;
import com.smile.common.action.LoginReqAction;
import com.smile.common.action.LoginRespAction;
import com.smile.common.event.IEvent;
import com.smile.server.connection.ConnectionPool;
import com.smile.server.model.User;
import com.smile.server.service.UserService;
import com.smile.server.util.SpringContextUtil;
import io.netty.channel.Channel;

public class LoginEvent implements IEvent<Action,Action> {

    @Override
    public Action handle(Action action, Channel channel) {
        System.out.println("receive action: " + action);
        LoginReqAction loginReqAction = JSONObject.parseObject(action.getPayload(), LoginReqAction.class);
        System.out.println("received login action resp: " + loginReqAction);

        //根据手机号 ，密码查询用户
        UserService userService = SpringContextUtil.getBean(UserService.class);
        if (null == userService){
            System.out.println("can not get userService ...");
            return null;
        }
        User user = userService.findUser(loginReqAction.getMobile(), loginReqAction.getPassword());
        //返回登陆结果
        LoginRespAction loginRespAction = new LoginRespAction();
        loginRespAction.setResult(false);
        if (null ==user){
            System.out.println("user not found with mobile :"+loginReqAction.getMobile()+",and password :"+loginReqAction.getPassword());
            loginRespAction.setPayload(JSONObject.toJSONString(loginRespAction));
            return loginRespAction;
        }
        //连接池添加用户id对应的链接对象
        System.out.println("login success with user mobile :"+user.getMobile());

        ConnectionPool.getInstance().add(user.getId(),channel);

          loginRespAction.setUserId(user.getId());
        loginRespAction.setResult(true);
        loginRespAction.setPayload(JSONObject.toJSONString(loginRespAction));
        return loginRespAction;
    }
}
