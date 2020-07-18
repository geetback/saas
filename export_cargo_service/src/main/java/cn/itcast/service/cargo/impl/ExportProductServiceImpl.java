package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ExportProductDao;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.service.cargo.ExportProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExportProductServiceImpl implements ExportProductService {
    @Autowired
    ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return null;
    }

    @Override
    public void save(ExportProduct exportProduct) {

    }

    @Override
    public void update(ExportProduct exportProduct) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(int page, int size, String companyId) {
        return null;
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample exportProductExample) {
        return exportProductDao.selectByExample(exportProductExample);
    }
}
