package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;

public interface SysLoService {
    PageInfo<SysLog> findByPage(int page,int size,String companyId);
    void save(SysLog sysLog);

}
