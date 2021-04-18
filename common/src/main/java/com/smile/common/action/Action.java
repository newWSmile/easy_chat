package com.smile.common.action;

import lombok.Data;

@Data
public class Action {

    private String actionType;

    private String action;

    private String requestId;

    /**
     * payload为json格式
     */
    private String payload;

}
