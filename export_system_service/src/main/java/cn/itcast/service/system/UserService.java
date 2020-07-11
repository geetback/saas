package cn.itcast.service.system;

import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    User findById(String id);
    void save(User user);
    void update(User user);
    void deleteById(String id);
    PageInfo<User> findByPage(int page, int size, String companyId);
    List<String> findRolesByUid(String id);

    void changeRole(String userid, String[] roleIds);

    User findByEmail(String email);
}
