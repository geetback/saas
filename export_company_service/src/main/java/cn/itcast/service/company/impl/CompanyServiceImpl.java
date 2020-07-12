package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import cn.itcast.utils.UUIDUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyDao companyDao;
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void save(Company company) {
        company.setId(UUIDUtils.getId());
        companyDao.save(company);
    }

    @Override
    public void update(Company company) {
        companyDao.update(company);

    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void deleteById(String id) {
        companyDao.deleteById(id);

    }

    @Override
    public PageInfo findByPage(int page, int size) {
        //调用pagehelper静态方式设置参数
        PageHelper.startPage(page,size);
        //查询所有
        List<Company>list= companyDao.findAll();
        //将上个查询的list集合使用pageinfo封装

        return new PageInfo<Company>(list);
    }
}
