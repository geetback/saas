package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    List<Role> findAll(String companyId);

    void save(Role role);

    void update(Role role);

    Role findById(String id);

    void delete(String id);

    void saveRoleModule(@Param("roleId") String roleid, @Param("moduleId") String moduleId);

    void deleteModulesByRoleId(String roleid);
}
