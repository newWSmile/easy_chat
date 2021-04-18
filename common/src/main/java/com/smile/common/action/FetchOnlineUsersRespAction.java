package com.smile.common.action;

import lombok.Data;

import java.util.UUID;
@Data
public class FetchOnlineUsersRespAction extends Action{

    public FetchOnlineUsersRespAction() {
        this.setActionType("");
        this.setAction(ActionIdEnum.ACTION_FETCH_ONLINE_USERS_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    //TODO

}
