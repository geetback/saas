package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.utils.QiniuYunUtils;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("cargo/contractProduct")
public class ContractProductController extends BaseController {
    @Reference
    ContractProductService contractProductService;
    @Reference
    FactoryService factoryService;

    @RequestMapping(value = "list",name = "货物列表")
    public String findAll(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "10")int size,
            String contractId){
        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria contractProductExampleCriteria = contractProductExample.createCriteria();

        contractProductExampleCriteria.andContractIdEqualTo(contractId);
        PageInfo pageInfo = contractProductService.findAll(page, size, contractProductExample);
        request.setAttribute("page",pageInfo);

        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("货物");
        factoryExample.setOrderByClause("factory_name asc");
        List<Factory> list = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",list);
        request.setAttribute("contractId",contractId);

        return "cargo/product/product-list";
    }
    @RequestMapping(value = "edit",name = "保存或更新货物")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws IOException {
        if (StringUtils.isBlank(contractProduct.getId())){
            //文件上传
            if (!productPhoto.isEmpty()) {
                String url = QiniuYunUtils.upload(productPhoto.getBytes());
                contractProduct.setProductImage(url);
            }
            contractProductService.save(contractProduct);
        }else{
            contractProductService.update(contractProduct);
        }
    return"redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }
    @RequestMapping(value = "toUpdate",name = "跳转到货物更新页面")
    public String toUpdate(String id){
        //查询该货物信息
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct",contractProduct);
        //查询所有生产货物的厂家信息
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("货物");
        //设置排序
        factoryExample.setOrderByClause("factory_name asc");
        List<Factory> list = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",list);
        return "cargo/product/product-update";
    }
    @RequestMapping(value = "delete",name = "删除货物")
    public String deleteById(String id,String contractId){
        contractProductService.deleteById(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
    @RequestMapping(value = "toImport",name = "跳转到批量上传货物页面")
    public String toImport(String contractId){
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }


    @RequestMapping(value = "import",name = "批量上传货物")
    public String batchSave(String contractId,MultipartFile file) throws IOException {
        Workbook wk = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = wk.getSheetAt(0);
        List<ContractProduct> list = new ArrayList<>();
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Object[] arr = new Object[10];

            for (int j = 1; j <  row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                arr[j] = getCellValue(cell);
            }

            ContractProduct contractProduct = new ContractProduct(arr,getCompanyId(),getCompanyName());
            contractProduct.setContractId(contractId);
            list.add(contractProduct);
        }
        contractProductService.batchSave(list);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
    private static Object getCellValue(Cell cell) {
        Object obj = null;

        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                obj = cell.getStringCellValue();
                break;
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = cell.getDateCellValue();
                } else {
                    obj = cell.getNumericCellValue();
                }
                break;
            default:
                obj = cell.getStringCellValue();
        }
        return obj;
    }
}
