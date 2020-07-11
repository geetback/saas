package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyDao {
    List<Company>findAll();

    void save(Company company);

    void update(Company company);

    Company findById(String id);

    void deleteById(String id);
    long findTotal();
    List<Company> findByPage(@Param("begin") int begin, @Param("size") int size);
}
