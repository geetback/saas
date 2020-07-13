package cn.itcast.web.controller.system;

import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.SysLoService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("system/log")
public class SysLogController extends BaseController {

    @Autowired
    SysLoService sysLoService;

    @RequestMapping(value = "list",name = "分页查询列表")
    public String findByPage(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "10")int size){

        PageInfo<SysLog> byPage = sysLoService.findByPage(page, size, getCompanyId());
        request.setAttribute("page",byPage);
        return "system/log/log-list";
    }
}
