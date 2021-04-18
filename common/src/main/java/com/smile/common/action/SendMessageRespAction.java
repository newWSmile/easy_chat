package com.smile.common.action;

import lombok.Data;

import java.util.UUID;
@Data
public class SendMessageRespAction extends Action{

    public SendMessageRespAction() {
        this.setActionType("");
        this.setAction(ActionIdEnum.ACTION_SEND_MESSAGE_RESP.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }




}
