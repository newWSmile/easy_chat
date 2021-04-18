package com.smile.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别 0未知 1男性 2女性
     */
    private int sex;

    /**
     * 用户状态 0正常 1异常
     */
    private int status;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 用户修改时间
     */
    private Date updateTime;



}
