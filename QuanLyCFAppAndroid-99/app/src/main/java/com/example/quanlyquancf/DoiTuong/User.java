package com.example.quanlyquancf.DoiTuong;

import java.io.Serializable;

public class User implements Serializable {
    private String Loginname;
    private String Displayname;
    private String Password;
    private String Email;
    private Long Type;

    public User(String loginname, String displayname, String password, String email, Long type) {
        Loginname = loginname;
        Displayname = displayname;
        Password = password;
        Email = email;
        Type = type;
    }

    public String getLoginname() {
        return Loginname;
    }

    public void setLoginname(String loginname) {
        Loginname = loginname;
    }

    public String getDisplayname() {
        return Displayname;
    }

    public void setDisplayname(String displayname) {
        Displayname = displayname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Long getType() {
        return Type;
    }

    public void setType(Long type) {
        Type = type;
    }

    public  User(){}

}
