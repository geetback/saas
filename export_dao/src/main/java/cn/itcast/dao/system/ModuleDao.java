package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;

import java.util.List;

public interface ModuleDao {
    List<Module> findAll();

    void save(Module module);

    void update(Module module);

    Module findById(String id);

    void delete(String id);

    List<Module> findModulesByRoleId(String roleId);

    List<Module> findByBelong(int i);

    List<Module> findModulesByUserId(String id);
}
