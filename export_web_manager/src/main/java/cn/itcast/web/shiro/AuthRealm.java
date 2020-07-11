package cn.itcast.web.shiro;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AuthRealm extends AuthorizingRealm{

    @Autowired
    UserService userService;

    @Autowired
    ModuleService moduleService;

    @Override

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User loginUser = (User) principalCollection.getPrimaryPrincipal();

        List<Module> moduleList = moduleService.findModulesByUser(loginUser);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> set = new HashSet<>();

        for (Module module : moduleList) {
            set.add(module.getName());
        }

        info.setStringPermissions(set);
        System.out.println(set);
        return info;
    }

    @Override
  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();

        User loginUser = userService.findByEmail(email);

        if (loginUser!=null) {

            return  new SimpleAuthenticationInfo(loginUser,loginUser.getPassword(),this.getName());
        }
        return null;
    }
}
