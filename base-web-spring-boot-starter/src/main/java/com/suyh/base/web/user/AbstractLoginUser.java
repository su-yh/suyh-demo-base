package com.suyh.base.web.user;

import lombok.Getter;

/**
 * @author suyh
 * @since 2025-05-20
 */
@Getter
public abstract class AbstractLoginUser {
    protected final Long id;
    protected final String username;
    protected final String nickname;

    public AbstractLoginUser(Long id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }
}
