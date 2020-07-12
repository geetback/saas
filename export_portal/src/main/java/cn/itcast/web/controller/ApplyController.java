package cn.itcast.web.controller;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {
    @Reference
    CompanyService companyService;
    @RequestMapping("apply")
    @ResponseBody
    public String apply(Company company){
        try {
            company.setState(0);
            companyService.save(company);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

}
