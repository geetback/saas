package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("system/module")
public class ModuleController extends BaseController {
    @Autowired
    ModuleService moduleService;
    @RequestMapping(value = "list",name = "查询所有模块")
    public String findAll(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int size){
        PageInfo<Module> pageInfo = moduleService.findByPage(page, size);
        request.setAttribute("page",pageInfo);
        return"system/module/module-list";
    }
    @RequestMapping(value = "toAdd",name = "跳转到添加模块页面")
    public String toAdd(){
        List<Module> list = moduleService.findAll();
        request.setAttribute("menus",list);
        return "system/module/module-add";
    }
    @RequestMapping(value = "edit",name = "保存模块")
    public String edit(Module module){
        if (StringUtils.isBlank(module.getId())){
            moduleService.save(module);
        }else {
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }
    @RequestMapping(value = "toUpdate",name = "跳转到更新模块")
    public String toUpdate(String id){
        List<Module> list = moduleService.findAll();
        request.setAttribute("menus",list);
        Module module = moduleService.findById(id);
        request.setAttribute("module",module);
        return "system/module/module-update";
    }
    @RequestMapping(value = "delete",name = "删除模块")
    public String delete(String id){
        moduleService.deleteById(id);
        return "redirect:/system/module/list.do";
    }

}
