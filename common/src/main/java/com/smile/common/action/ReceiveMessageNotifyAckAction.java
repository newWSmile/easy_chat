package com.smile.common.action;

import lombok.Data;

import java.util.UUID;

@Data
public class ReceiveMessageNotifyAckAction extends Action{


    public ReceiveMessageNotifyAckAction() {
        this.setActionType("");
        this.setAction(ActionIdEnum.ACTION_RECEIVE_MESSAGE_NOTIFY_ACK.getAction());
        this.setRequestId(UUID.randomUUID().toString());
    }


    /**
     * 消息id
     */
    private String messageId;



}
