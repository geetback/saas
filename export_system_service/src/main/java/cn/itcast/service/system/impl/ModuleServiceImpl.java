package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.utils.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    ModuleDao moduleDao;
    @Override
    public List<Module> findAll() {

        return moduleDao.findAll();
    }

    @Override
    public PageInfo<Module> findByPage(int page, int size) {
        PageHelper.startPage(page,size);
        List<Module> list = moduleDao.findAll();
        return new PageInfo<>(list);
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public void save(Module module) {
        module.setId(UUIDUtils.getId());
        moduleDao.save(module);

    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);

    }

    @Override
    public void deleteById(String id) {
        moduleDao.delete(id);

    }

    @Override
    public List<Module> findModulesByRoleId(String roleId) {
        return moduleDao.findModulesByRoleId(roleId);
    }

    @Override
    public List<Module> findModulesByUser(User loginUser) {
        if (loginUser.getDegree() == 0) {
            return moduleDao.findByBelong(0);
        }else if (loginUser.getDegree() == 1){
            return moduleDao.findByBelong(1);
        }else{
            return moduleDao.findModulesByUserId(loginUser.getId());
        }
    }


}
