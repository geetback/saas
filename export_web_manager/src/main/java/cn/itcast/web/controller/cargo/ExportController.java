package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.domain.vo.ExportProductVo;
import cn.itcast.domain.vo.ExportResult;
import cn.itcast.domain.vo.ExportVo;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("cargo/export")
public class ExportController extends BaseController {
    @Reference
    ContractService contractService;
    @Reference
    ExportService exportService;

    @Reference
    ExportProductService exportProductService;

    @RequestMapping(value = "contractList", name = "查询已上报的合同")
    public String contractList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andCompanyIdEqualTo(getCompanyId());
        criteria.andStateEqualTo(1);

        contractExample.setOrderByClause("ship_time desc");

        PageInfo pageInfo = contractService.findAll(page, size, contractExample);
        request.setAttribute("page", pageInfo);
        return "cargo/export/export-contractList";

    }

    @RequestMapping(value = "list", name = "报运单列表")
    public String findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        criteria.andCompanyIdEqualTo(getCompanyId());
        exportExample.setOrderByClause("create_time desc");

        PageInfo pageInfo = exportService.findAll(page, size, exportExample);
        request.setAttribute("page", pageInfo);
        return "cargo/export/export-list";

    }

    @RequestMapping(value = "toExport", name = "跳转到报运页面")
    public String toExport(String id) {
        request.setAttribute("id", id);
        return "cargo/export/export-toExport";
    }

    @RequestMapping(value = "edit", name = "新增修改页面")
    public String edit(Export export) {
        if (StringUtils.isBlank(export.getId())) {
            export.setCompanyId(getCompanyId());
            export.setCompanyName(getCompanyName());
            exportService.save(export);
            return "redirect:/cargo/export/contractList.do";
        } else {
            exportService.update(export);
            return "redirect:/cargo/export/contractList.do";
        }
    }

    @RequestMapping(value = "toUpdate", name = "跳转到报运单更新页面")
    public String toUpdate(String id) {
        Export export = exportService.findById(id);
        request.setAttribute("export", export);

        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria criteria = exportProductExample.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> epList = exportProductService.findAll(exportProductExample);
        request.setAttribute("eps", epList);

        return "cargo/export/export-update";

    }
    @RequestMapping(value = "submit",name = "提交报运单")
    public String submit(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(1);

        exportService.update(export);

        return "redirect:/cargo/export/list.do";
    }


    @RequestMapping(value = "cancel",name = "取消报运单")
    public String cancel(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(0);

        exportService.update(export);

        return "redirect:/cargo/export/list.do";
    }
    @RequestMapping(value = "exportE",name = "海关电子报运")
    public String exportE(String id){
    
        Export export = exportService.findById(id);
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export,exportVo);

        exportVo.setExportId(id);

        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria criteria = exportProductExample.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);

        List<ExportProductVo> exportProductVoList = new ArrayList<>();

        if (list!=null&&list.size()>0) {
            for (ExportProduct ep : list) {
                ExportProductVo epvo = new ExportProductVo();
                BeanUtils.copyProperties(ep,epvo);
                epvo.setExportProductId(ep.getId());
                exportProductVoList.add(epvo);
            }
        }
        exportVo.setProducts(exportProductVoList);

        WebClient webClient = WebClient.create("http://localhost:9999/ws/export/user");
        webClient.post(exportVo);

        webClient = WebClient.create("http://localhost:9999/ws/export/user/"+id);
        ExportResult exportResult = webClient.get(ExportResult.class);
        exportService.updateByExportResult(exportResult);

        return "redirect:/cargo/export/list.do";
    }
}
