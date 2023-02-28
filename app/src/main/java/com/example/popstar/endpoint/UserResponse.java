package com.example.popstar.endpoint;

public class UserResponse {
    private Boolean status;
    private String msg;
    private User data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
    public void show(){
        System.out.println(status+msg+data.getToken());
    }
}
