package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    ExtCproductService extCproductService;

    @Reference
    FactoryService factoryService;

    @RequestMapping(value = "list",name="附件列表页面")
    public String findAll(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "10")int size,
            String contractProductId,String contractId){


        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria extCproductExampleCriteria = extCproductExample.createCriteria();
        extCproductExampleCriteria.andContractProductIdEqualTo(contractProductId);

        PageInfo pageInfo = extCproductService.findAll(page, size, extCproductExample);
        request.setAttribute("page",pageInfo);

        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("附件");
        factoryExample.setOrderByClause("factory_name asc");

        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

        return "cargo/extc/extc-list";
    }

    @RequestMapping(value = "edit",name = "保存或者更新附件")
    public String edit(ExtCproduct extCproduct){

        if (StringUtils.isBlank(extCproduct.getId())) {
            extCproductService.save(extCproduct);
        }else {
            extCproductService.update(extCproduct);
        }

        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }

    @RequestMapping(value = "toUpdate",name = "跳转到附件更新页面")
    public String toUpdate(String id,String contractProductId,String contractId){

        //1.查询附件信息
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct",extCproduct);

        //2.查询生产附件的厂家信息
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("附件");
        factoryExample.setOrderByClause("factory_name asc");

        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

        //3.将合同id和货物id放入域中
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        return "cargo/extc/extc-update";
    }

    @RequestMapping(value = "delete",name = "删除附件")
    public String deleteById(String id,String contractProductId,String contractId){
        extCproductService.deleteById(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
