package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cargo/contract")
public class ContractController extends BaseController {
    @Reference
    ContractService contractService;

    @RequestMapping(value = "list",name = "购销合同列表")
    public String findAll(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int size){
        ContractExample contractExample=new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andCompanyIdEqualTo(getCompanyId());
        User loginUser = getLoginUser();
        if (loginUser.getDegree() == 4) {
            //普通员工,根据创建者id查询
            criteria.andCreateByEqualTo(loginUser.getId());
        }else if(loginUser.getDegree() == 3){
            //管理,根据部门的id查询
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        }
        //排序
        contractExample.setOrderByClause("create_time desc");
        //查询操作
        PageInfo pageInfo = contractService.findAll(page, size, contractExample);
        request.setAttribute("page",pageInfo);
        return"cargo/contract/contract-list";

    }
    @RequestMapping(value = "toAdd",name = "跳转到合同添加页面")
    public String toAdd(){
        return"cargo/contract/contract-add";
    }
    @RequestMapping(value = "toUpdate",name = "跳转合同更新页面")
    public String toUpdate(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        return"cargo/contract/contract-update";
    }
    @RequestMapping(value = "edit",name = "合同添加或更新")
    public String edit(Contract contract){
        if (StringUtils.isNotBlank(contract.getId())){
            contractService.update(contract);
        }else {
            contract.setState(0);
            contract.setCreateBy(getLoginUser().getId());
            contract.setCreateDept(getLoginUser().getDeptId());
            contract.setCompanyId(getCompanyId());
            contract.setCompanyName(getCompanyName());
            contractService.save(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }
    @RequestMapping(value = "delete",name = "删除")
    public String deleteById(String id){
        contractService.deleteById(id);
        return "redirect:/cargo/contract/list.do";
    }
    @RequestMapping(value = "submit",name = "提交")
    public String submit(String id){
        Contract contract=new Contract();
        contract.setId(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }
    @RequestMapping(value = "cancel",name = "取消")
    public String cancel(String id){
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }




}
