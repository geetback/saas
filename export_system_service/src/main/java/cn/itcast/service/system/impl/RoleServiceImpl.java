package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import cn.itcast.utils.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;
    @Override
    public List<Role> findAll(String companyId) {

        return roleDao.findAll(companyId);
    }

    @Override
    public PageInfo<Role> findByPage(int page, int size, String companyId) {
        PageHelper.startPage(page,size);
        List<Role> list =roleDao.findAll(companyId);
        return new PageInfo<>(list);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public void deleteById(String id) {
        roleDao.delete(id);

    }

    @Override
    public void save(Role role) {
        role.setId(UUIDUtils.getId());
        roleDao.save(role);

    }

    @Override
    public void update(Role role) {
        roleDao.update(role);

    }

    @Override
    public void updateRoleModule(String roleid, String moduleIds) {

        roleDao.deleteModulesByRoleId(roleid);


        if (StringUtils.isNotBlank(moduleIds)) {
            for (String moduleId : moduleIds.split(",")) {
                roleDao.saveRoleModule(roleid,moduleId);
            }
        }
    }
}
