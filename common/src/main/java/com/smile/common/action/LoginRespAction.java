package com.smile.common.action;

import lombok.Data;

import java.util.UUID;
@Data
public class LoginRespAction extends Action{

    public LoginRespAction() {
        this.setActionType("");
        this.setAction(ActionIdEnum.ACTION_LOGIN_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    /**
     * 登陆结果
     */
    private Boolean result;

    /**
     * 当前用户id
     */
    private Long userId;

}
