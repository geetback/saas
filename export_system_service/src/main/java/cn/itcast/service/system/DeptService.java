package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeptService {
    List<Dept> findAll(String companyId);
    Dept findById(String id);
    void save(Dept dept);
    void update(Dept dept);
    void deleteById(String id);
    PageInfo findByPage(int page, int size, String companyId);
}
