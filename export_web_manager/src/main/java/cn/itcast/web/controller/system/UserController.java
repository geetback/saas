package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
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
@RequestMapping("system/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;
    @Autowired
    RoleService roleService;
    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "list",name = "查询所有用户")
    public String findAll(@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "5")int size){

        PageInfo<User> pageInfo = userService.findByPage(page, size, getCompanyId());
        request.setAttribute("page",pageInfo);
        return "system/user/user-list";
    }
    @RequestMapping(value = "toAdd",name = "跳转到添加用户页面")
    public String toAdd(){

        //查询当前企业所有部门
        List<Dept> list = deptService.findAll(getCompanyId());
        request.setAttribute("deptList",list);
        return"system/user/user-add";
    }
    @RequestMapping(value = "edit",name = "添加或者更新用户")
    public String edit(User user){

        user.setCompanyId(getCompanyId());
        user.setCompanyName(getCompanyName());
        //判读添加或者更新
        if (StringUtils.isBlank(user.getId())){
            userService.save(user);
        }else{
            userService.update(user);
        }
        return"redirect:/system/user/list.do";

    }
    @RequestMapping(value = "toUpdate",name = "跳转到用户编辑页面")
    public String toUpdate(String id){

        List<Dept> list = deptService.findAll(getCompanyId());
        request.setAttribute("deptList",list);
        //查询用户信息,放入域中
        User user =userService.findById(id);
        request.setAttribute("user",user);
        return "system/user/user-update";
    }
    @RequestMapping(value = "delete",name = "删除用户")
    public String delte(String id){
        userService.deleteById(id);
        return"redirect:/system/user/list.do";
    }
    @RequestMapping(value="roleList",name = "进入角色分配页面")
    public String roleList(String id){
        User user = userService.findById(id);
        request.setAttribute("user",user);
        List<Role> list = roleService.findAll(getCompanyId());
        request.setAttribute("roleList",list);
        List<String> roleIdList = userService.findRolesByUid(id);
        request.setAttribute("userRoleStr", roleIdList.toString());

        return "system/user/user-role";
    }
    @RequestMapping(value = "changeRole",name = "分配角色")
    public String changeRole(String userid ,String[] roleIds){
        userService.changeRole(userid,roleIds);
        return "redirect:/system/user/list.do";
    }

}
