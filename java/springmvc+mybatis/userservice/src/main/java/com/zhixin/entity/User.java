package com.zhixin.entity;

import org.springframework.context.annotation.Bean;

import java.io.Serializable;

/**
 * 用户的实体类
 */

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private String serialaNumber;

    public User(){

    }
    public User(String username, String password, String serialaNumber) {
        this.username = username;
        this.password = password;
        this.serialaNumber = serialaNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSerialaNumber() {
        return serialaNumber;
    }

    public void setSerialaNumber(String serialaNumber) {
        this.serialaNumber = serialaNumber;
    }

}
