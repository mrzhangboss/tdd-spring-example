package io.github.mrzhangboss.web.demo;

public class ResBean<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResBean<String> LoginError() {
        ResBean<String> res = new ResBean<>();
        res.setCode(500);
        res.setMsg("用户名或者密码错误");
        return res;
    }

    public static ResBean<String> LoginCaptchaError() {
        ResBean<String> res = new ResBean<>();
        res.setCode(500);
        res.setMsg("验证码错误");
        return res;
    }

    public static ResBean<String> LoginSuccess() {
        ResBean<String> res = new ResBean<>();
        res.setCode(200);
        res.setMsg("ok");
        return res;
    }

}
