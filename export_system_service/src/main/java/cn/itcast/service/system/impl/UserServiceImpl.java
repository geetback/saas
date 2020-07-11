package cn.itcast.service.system.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import cn.itcast.utils.Encrypt;
import cn.itcast.utils.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;


    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void save(User user) {
        //设置id
        user.setId(UUIDUtils.getId());
        user.setPassword(Encrypt.md5(user.getPassword(),user.getEmail()));
        userDao.save(user);

    }

    @Override
    public void update(User user) {
        userDao.update(user);

    }

    @Override
    public void deleteById(String id) {
        userDao.delete(id);

    }

    @Override
    public PageInfo findByPage(int page, int size, String companyId) {
        //设置分页参数
        PageHelper.startPage(page,size);
        //查询所有
        List<User>list= userDao.findAll(companyId);
        //封装list到pageinfo
        return new PageInfo<>(list);
    }
    @Override
    public List<String> findRolesByUid(String id) {
        return userDao.findRolesByUid(id);
    }

    @Override
    public void changeRole(String userid, String[] roleIds) {
        //- 调用userDao.deleteRolesByUid(id),删除中间表中用户原来的角色
        userDao.deleteRolesByUid(userid);

        if (roleIds!=null && roleIds.length>0) {
            //- 遍历角色数组,获取到每个角色id
            for (String roleId : roleIds) {
                //- 调用userDao.saveUserRole(用户id,角色id),完成最新的角色保存即可
                userDao.saveUserRole(userid,roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
