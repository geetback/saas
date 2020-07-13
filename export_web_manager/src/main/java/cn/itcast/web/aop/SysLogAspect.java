package cn.itcast.web.aop;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLoService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Component
@Aspect
public class SysLogAspect {

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpSession session;

    @Autowired
    SysLoService sysLoService;

    @Around("execution(* cn.itcast.web.controller..*.*(..))")
    public Object saveSysLog(ProceedingJoinPoint pjp) throws Throwable {

        //让目标方法执行
        Object result = pjp.proceed();

        //先获取方法对象
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();//获取方法的签名
        Method method = methodSignature.getMethod();

        //给有@RequestMapping注解的方法添加日志
        if (method.isAnnotationPresent(RequestMapping.class)) {
            //封装日志
            SysLog sysLog = new SysLog();

            //获取当前登陆者
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser != null) {
                sysLog.setUserName(loginUser.getUserName());
                sysLog.setIp(request.getLocalAddr());
                sysLog.setCompanyId(loginUser.getCompanyId());
                sysLog.setCompanyName(loginUser.getCompanyName());
            }else {
                sysLog.setUserName("匿名");
            }

            sysLog.setMethod(method.getName());

            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            sysLog.setAction(annotation.name());
            sysLoService.save(sysLog);
        }

        return result;
    }
}
