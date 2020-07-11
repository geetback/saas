package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {
    List<Module> findAll();
    PageInfo<Module> findByPage(int page, int size);
    Module findById(String id);
    void save(Module module);
    void update(Module module);
    void deleteById(String id);


    List<Module> findModulesByRoleId(String roleId);

    List<Module> findModulesByUser(User loginUser);
}
