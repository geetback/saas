package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    public HttpServletRequest request;

    @Autowired
    public HttpSession session;

    public User getLoginUser(){
        return (User) session.getAttribute("loginUser");
    }

    public String getCompanyId(){
        return getLoginUser().getCompanyId();
    }

    public String getCompanyName(){
        return getLoginUser().getCompanyName();
    }
}
