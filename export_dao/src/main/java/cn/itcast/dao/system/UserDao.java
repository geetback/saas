package cn.itcast.dao.system;

import cn.itcast.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    List<User> findAll(String companyId);
    void save(User user);
    void update(User user);
    User findById(String id);
    void delete(String id);

    List<String> findRolesByUid(String id);

    void deleteRolesByUid(String userid);

    void saveUserRole(@Param("userId") String userid, @Param("roleId") String roleId);

    User findByEmail(String email);
}
