package com.smile.common.action;

import com.smile.common.vo.UserItem;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
public class FetchOnlineUsersReqAction extends Action{


    public FetchOnlineUsersReqAction() {
        this.setActionType("");
        this.setAction(ActionIdEnum.ACTION_FETCH_ONLINE_USERS_REQ.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }

    @Setter
    private List<UserItem> users;

}
