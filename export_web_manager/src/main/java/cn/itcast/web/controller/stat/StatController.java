package cn.itcast.web.controller.stat;


import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import stat.StatService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("stat")
public class StatController extends BaseController {

    @Reference
    StatService statService;

    @RequestMapping(value = "toCharts",name = "跳转到统计页面")
    public String toCharts(String chartsType){

        return "stat/stat-"+chartsType;
    }

    @RequestMapping(value = "getFactoryData",name = "获取厂家销售数据")
    @ResponseBody
    public List<Map> getFactoryData(){
        List<Map> list = statService.getFactoryData(getCompanyId());
        return list;
    }

    @RequestMapping(value = "getProductData",name = "获取商品销售top10")
    @ResponseBody
    public List<Map> getProductData(){
        List<Map> list = statService.getProductData(getCompanyId());
        return list;
    }

    @RequestMapping(value = "getOnLineData",name = "获取时间段访问量")
    @ResponseBody
    public List<Map> getOnLineData(){
        List<Map> list = statService.getOnLineData(getCompanyId());
        return list;
    }


}
