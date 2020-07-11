package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
    List<Role> findAll(String companyId);
    PageInfo<Role> findByPage(int page, int size, String companyId);
    Role findById(String id);
    void deleteById(String id);
    void save(Role role);
    void update(Role role);

    void updateRoleModule(String roleid, String moduleIds);
}
