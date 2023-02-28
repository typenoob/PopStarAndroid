package com.example.popstar.endpoint;

import java.util.List;

public class ScoreResponse {
    private Boolean status;
    private String msg;
    private List<Score> data;

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

    public List<Score> getData() {
        return data;
    }

    public void setScore(List<Score> data) {
        this.data = data;
    }

    public void show() {
        System.out.println(status + msg + data.get(0).getName());
    }

}
