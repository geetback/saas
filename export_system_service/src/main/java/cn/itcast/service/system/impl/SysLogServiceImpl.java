package cn.itcast.service.system.impl;

import cn.itcast.dao.system.SysLogDao;
import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.SysLoService;
import cn.itcast.utils.UUIDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLoService {
    @Autowired
    SysLogDao sysLogDao;
    @Override
    public PageInfo<SysLog> findByPage(int page, int size, String companyId) {
        //设置分页参数
        PageHelper.startPage(page,size);
        //查询所有
        List<SysLog> list = sysLogDao.findAll(companyId);
        //封装查询的list
        return new PageInfo<>(list);


    }

    @Override
    public void save(SysLog sysLog) {
        //设置id
        sysLog.setId(UUIDUtils.getId());
        //设置时间
        sysLog.setTime(new Date());
        sysLogDao.save(sysLog);

    }
}
