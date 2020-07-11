package cn.itcast.web.shiro;

import cn.itcast.utils.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;


public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{
    @Override

    public boolean doCredentialsMatch(AuthenticationToken token1, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) token1;
        String email = token.getUsername();
        String password = new String(token.getPassword());

        String dbPassword = (String) info.getCredentials();


        return dbPassword.equals(Encrypt.md5(password,email));
    }
}
