package cn.itcast.service.stat.impl;

import cn.itcast.dao.stat.StatDao;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import stat.StatService;

import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    StatDao statDao;

    @Override
    public List<Map> getFactoryData(String companyId) {
        return statDao.getFactoryData(companyId);
    }

    @Override
    public List<Map> getProductData(String companyId) {
        return statDao.getProductData(companyId);
    }

    @Override
    public List<Map> getOnLineData(String companyId) {
        return statDao.getOnLineData(companyId);
    }
}
