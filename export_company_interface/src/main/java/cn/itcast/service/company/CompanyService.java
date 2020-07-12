package cn.itcast.service.company;


import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyService {
    List<Company> findAll();

    void save(Company company);

    void update(Company company);

    Company findById(String id);

    void deleteById(String id);
    PageInfo findByPage(int page, int size);
}
