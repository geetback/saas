package cn.itcast.web.controller.company;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import net.sf.ehcache.pool.Size;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("company")
public class CompanyController extends BaseController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    CompanyService companyService;

    @RequestMapping(value = "list",name="查询所有企业")
    public String findAll(@RequestParam(defaultValue = "1")int page,
                          @RequestParam(defaultValue ="5") int size){
        //int i=1/0;

        PageInfo pageInfo = companyService.findByPage(page, size);
        //将pageinfo翻入request域中
        request.setAttribute("page",pageInfo);
        return "company/company-list";
    }
    @RequestMapping(value = "toAdd",name="跳转到新增页面")
    public String toAdd(){
        return "company/company-add";
    }
    @RequestMapping(value = "edit",name="新增更新企业")
    public String edit(Company company){
        if (StringUtils.isBlank(company.getId())){
            companyService.save(company);
        }else {
            companyService.update(company);
        }
        return "redirect:/company/list.do";
    }
    @RequestMapping(value = "toUpdate",name="跳转到更新页面")
    public String toUpdate(String id){
        Company company = companyService.findById(id);
        request.setAttribute("company",company);
        return "company/company-update";
    }
    @RequestMapping(value = "delete",name="删除")
    public String deleteById(String id){
        companyService.deleteById(id);
        return "redirect:/company/list.do";
    }
}
