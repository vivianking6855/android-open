package com.wenxi.learn.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEntity implements Parcelable {
    public String userId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
    }

    public UserEntity(String id) {
        userId = id;
    }

    protected UserEntity(Parcel in) {
        this.userId = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}
