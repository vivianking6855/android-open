package com.wenxi.learn.data.listener;

import com.wenxi.learn.data.entity.UserEntity;

public interface IUserListener {
    void onSuccess(UserEntity user);

    void onError();
}
