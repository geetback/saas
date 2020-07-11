package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("system/dept")
public class DeptController extends BaseController {
    @Autowired
    DeptService deptService;


    @RequestMapping(value = "list",name = "查询所有部门")
    public String findAll(@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "5")int size){

        PageInfo<Dept> pageInfo=deptService.findByPage(page,size,getCompanyId());
        request.setAttribute("page",pageInfo);
        return "system/dept/dept-list";
    }

    @RequestMapping(value = "toAdd",name = "跳转到添加部门的页面")
    public String toAdd(){
        //查询当前企业中的所有部门
        List<Dept> list = deptService.findAll(getCompanyId());
        request.setAttribute("deptList",list);
        return "system/dept/dept-add";
    }
    @RequestMapping(value = "edit",name = "添加或更新部门")
    public String edit(Dept dept){
        dept.setCompanyName(getCompanyName());
        dept.setCompanyId(getCompanyId());
        //判断id是否存在
        if (StringUtils.isBlank(dept.getId())){
            deptService.save(dept);
        }else {
            deptService.update(dept);
        }
        return"redirect:/system/dept/list.do";
    }
    @RequestMapping(value = "toUpdate",name = "跳转到更新部门的页面")
    public String toUpdate(String id){

        Dept dept =deptService.findById(id);
        List<Dept> list=deptService.findAll(getCompanyId());
        request.setAttribute("deptList",list);
        request.setAttribute("dept",dept);
        return "system/dept/dept-update";
    }
    @RequestMapping(value = "delete",name = "删除部门")
    public String delete(String id){
        deptService.deleteById(id);
        return "redirect:/system/dept/list.do";
    }
}
