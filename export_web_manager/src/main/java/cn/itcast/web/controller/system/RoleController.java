package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("system/role")
public class RoleController extends BaseController {
    @Autowired
    RoleService roleService;
    @Autowired
    ModuleService moduleService;

    @RequestMapping(value = "list",name = "查询所有角色")
    public String findAll(@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "10")int size){
        PageInfo<Role> pageInfo =roleService.findByPage(page,size,getCompanyId());
        request.setAttribute("page",pageInfo);
        return "system/role/role-list";
    }
    @RequestMapping(value = "toAdd" ,name = "添加或更新角色")
    public String toAdd(){
        return "system/role/role-add";
    }
    @RequestMapping(value = "edit",name = "添加或者更新角色")
    public String edit(Role role){
        role.setCompanyId(getCompanyId());
        role.setCompanyName(getCompanyName());
        if (StringUtils.isBlank(role.getId())){
            roleService.save(role);
        }else {
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }
    @RequestMapping(value = "toUpdate",name = "跳转到角色更新页面")
    public String toUpdate(String id){
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return"system/role/role-update";
    }
    @RequestMapping(value = "delete",name = "删除角色")
    public  String delete(String id){
        roleService.deleteById(id);
        return "redirect:/system/role/list.do";
    }
    @RequestMapping(value = "roleModule",name = "跳转到分配权限的页面")
    public String roleModule(String roleid){
        Role role = roleService.findById(roleid);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }
    @RequestMapping(value = "moduleList",name = "权限分配页面的数据")
    @ResponseBody//将返回值转成json字符串数据返回给浏览器
    public List<Map> moduleList(String roleId){
        //- 声明一个List\<Map>,用来存放返回值的数据
        List<Map> list = new ArrayList<>();

        //- 调用moduleService.findModulesByRoleId(roleId),查询当前角色拥有的模块,返回值:List
        List<Module> roleModules = moduleService.findModulesByRoleId(roleId);

        //- 调用moduleService.findAll(),查询所有的模块
        List<Module> moduleList = moduleService.findAll();

        //- 遍历所有模块列表,获取到每个模块,
        for (Module module : moduleList) {
            //  - 将每个模块封装成Map
            Map map = new HashMap();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());

            //    - 判断当前的模块是否在当前的角色的模块集合中,若有给map添加cheched属性
            // 注意:module必须重写hashcode和equals方法  (使用id字段)
            if (roleModules.contains(module)) {
                map.put("checked",true);
            }

            //  - 将每个map放入返回值的list中
            list.add(map);
        }

        return list;
    }
    @RequestMapping(value = "updateRoleModule",name = "分配权限")
    public String updateRoleModule(String roleid,String moduleIds){
        roleService.updateRoleModule(roleid,moduleIds);
        return "redirect:/system/role/list.do";
    }
}
