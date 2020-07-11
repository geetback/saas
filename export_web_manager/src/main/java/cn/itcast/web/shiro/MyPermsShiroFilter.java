package cn.itcast.web.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义过滤器
 *      若在配置文件中这样写  /system/dept/list.do=myperms["部门管理"]
 *          若当前用户的权限列表包含这个权限则放行,否则不放行
 *
 *      若在配置文件中这样写  /system/dept/list.do=myperms["部门管理","删除部门"]
 *          若当前用户的权限列表不包含配置中任意一个不放行
 *          若当前用户的权限列表包含配置中任意一个则放行
 *
 */
public class MyPermsShiroFilter extends AuthorizationFilter {
    @Override
    //true 放行; false 不放行
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //1.获取subject对象
        Subject subject = getSubject(request, response);

        //2.获取配置文件中的配置信息
        String[] perms = (String[]) mappedValue;

        //3.判断配置有没有
        if (perms != null && perms.length > 0) {
            //有配置,就需要满足规则
            //继续判断有一个配置还是多个配置
            if (perms.length == 1) {
                //有一个配置  ["部门管理"]
                return subject.isPermitted(perms[0]);
            }else{
                //有多个配置  ["部门管理","删除部门"]
                boolean flag = false;

                for (String perm : perms) {
                    if (subject.isPermitted(perm)) {
                        flag = true;
                        break;
                    }
                }

                return flag;
            }
        }else{
            //若没有陪孩子,就放行
            return true;
        }
    }
}
