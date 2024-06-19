package io.github.xucancould.common;

import io.github.xucancould.model.UserDO;

/**
 * created by Xu on 2024/3/22 15:24.
 */
public class LocalUser {

    private LocalUser() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<UserDO> LOCAL = new ThreadLocal<>();

    /**
     * 获取当前登录(线程中)的用户
     * @return user | null
     */
    public static UserDO getLocalUser() {
        return LOCAL.get();
    }

    public static void setLocalUser(UserDO user) {
        LOCAL.set(user);
    }

    public void removeLocalUser() {
        LOCAL.remove();
    }

    public static void clearLocalUser() {
        LOCAL.remove();
    }
}
