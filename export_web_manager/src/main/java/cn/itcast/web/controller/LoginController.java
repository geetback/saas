package cn.itcast.web.controller;


import cn.itcast.domain.company.Company;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.company.CompanyService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import cn.itcast.utils.Encrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    ModuleService moduleService;

    @RequestMapping("/login")
    public String login(String email,String password) {
        //1 判断邮箱或密码是否为空 若为空则跳转到登录页进行提示
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            request.setAttribute("error","用户名和密码不能为空");
            return "forward:/login.jsp";
        }

        try {
            Subject subject = SecurityUtils.getSubject();

            UsernamePasswordToken token = new UsernamePasswordToken(email, password);

            subject.login(token);

            User loginUser  = (User) subject.getPrincipal();

            Company company = companyService.findById(loginUser.getCompanyId());
            if (loginUser.getDegree() != 0 && company.getState() != 1) {
                request.setAttribute("error","请联系贵公司的管理员");
                return "forward:/login.jsp";
            }

            session.setAttribute("loginUser",loginUser);

            List<Module> modules = moduleService.findModulesByUser(loginUser);
            session.setAttribute("modules",modules);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            request.setAttribute("error","用户名或密码错误");
            return "forward:/login.jsp";
        }

        return "home/main";
    }

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
