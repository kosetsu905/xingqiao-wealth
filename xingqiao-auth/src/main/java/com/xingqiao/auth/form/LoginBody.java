package com.xingqiao.auth.form;

/**
 * 用户登录对象
 * 
 * @author xingqiao
 */
public class LoginBody
{
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    public String getUserName()
    {
        return userName;
    }

    public void setUsername(String username)
    {
        this.userName = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
