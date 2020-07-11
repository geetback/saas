package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.utils.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    DeptDao deptDao;
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    @Override
    public void save(Dept dept) {

        dept.setId(UUIDUtils.getId());
        deptDao.save(dept);
    }

    @Override
    public void update(Dept dept) {
        deptDao.update(dept);

    }

    @Override
    public void deleteById(String id) {
        deptDao.updateParentIsNull4Delete(id);
        deptDao.deleteById(id);

    }

    @Override
    public PageInfo<Dept> findByPage(int page, int size, String companyId) {
        //设置分页参数
        PageHelper.startPage(page,size);
        //查询所有
        List<Dept> list=deptDao.findAll(companyId);
        //使用pageinfo封装上面的list

        return new PageInfo<Dept>(list);
    }
}
