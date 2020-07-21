package cn.itcast.dao.stat;

import java.util.List;
import java.util.Map;

public interface StatDao {
    List<Map> getFactoryData(String companyId);

    List<Map> getProductData(String companyId);

    List<Map> getOnLineData(String companyId);
}
