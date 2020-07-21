package stat;

import java.util.List;
import java.util.Map;

public interface StatService {
    List<Map> getFactoryData(String companyId);

    List<Map> getProductData(String companyId);

    List<Map> getOnLineData(String companyId);
}
