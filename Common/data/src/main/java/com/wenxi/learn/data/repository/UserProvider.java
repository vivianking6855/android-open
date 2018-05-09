package com.wenxi.learn.data.repository;

import com.wenxi.learn.data.entity.UserEntity;
import com.wenxi.learn.data.listener.IUserListener;

/**
 * The type User provider.
 */
public final class UserProvider {
    /**
     * Gets user.
     *
     * @return the user
     */
    public static void getUserAsync(IUserListener listener) {
        if (listener == null) {
            return;
        }
        try {
            Thread.sleep(2000);
            listener.onSuccess(new UserEntity("mockId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserEntity getUser() {
        try {
            Thread.sleep(2000);
            return new UserEntity("mockId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
