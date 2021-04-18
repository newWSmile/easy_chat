package com.smile.common.action;

import lombok.Data;

import java.util.UUID;
@Data
public class LoginReqAction extends Action{

    public LoginReqAction() {
        this.setActionType("");
        this.setAction(ActionIdEnum.ACTION_LOGIN_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    private String mobile ;

    private String password;

}
