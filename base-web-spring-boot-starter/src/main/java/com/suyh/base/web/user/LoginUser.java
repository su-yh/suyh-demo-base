package com.suyh.base.web.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 当前登录用户必须是该类的子类
 */
@Data
@Slf4j
public class LoginUser {
    public static final String NICK_NAME_KEY = "nickname";

    private Long id;
    private String username;
    private String nickname;
}
