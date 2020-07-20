package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.domain.vo.ExportProductResult;
import cn.itcast.domain.vo.ExportResult;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.utils.UUIDUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    ExportDao exportDao;
    @Autowired
    ContractDao contractDao;

    @Autowired
    ContractProductDao contractProductDao;

    @Autowired
    ExportProductDao exportProductDao;

    @Autowired
    ExtCproductDao extCproductDao;

    @Autowired
    ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        export.setId(UUIDUtils.getId());
        String[] contractIdArr = export.getContractIds().split(",");
        List<String> contractIdList = Arrays.asList(contractIdArr);
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andIdIn(contractIdList);
        List<Contract> contractList = contractDao.selectByExample(contractExample);

        String cno = "";
        if (contractList != null && contractList.size() > 0) {
            for (Contract contract : contractList) {
                cno += (contract.getContractNo() + "");
                contract.setState(2);
                contractDao.updateByPrimaryKeySelective(contract);
            }
        }
        export.setCustomerContract(cno);
        export.setCreateTime(new Date());
        export.setState(0);

        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria contractProductExampleCriteria = contractProductExample.createCriteria();
        contractProductExampleCriteria.andContractIdIn(contractIdList);
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(contractProductExample);

        if (contractProductList != null && contractProductList.size() > 0) {
            for (ContractProduct cp : contractProductList) {
                ExportProduct ep = new ExportProduct();
                BeanUtils.copyProperties(cp, ep);
                ep.setExportId(export.getId());
                exportProductDao.insertSelective(ep);
            }
        }
        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria exampleCriteria = extCproductExample.createCriteria();
        exampleCriteria.andContractIdIn(contractIdList);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);

        if (extCproductList != null && extCproductList.size() > 0) {
            for (ExtCproduct ecp : extCproductList) {
                ExtEproduct eep = new ExtEproduct();
                BeanUtils.copyProperties(ecp, eep);
                eep.setExportProductId(ecp.getContractProductId());
                eep.setExportId(export.getId());
                extEproductDao.insertSelective(eep);
            }
        }
        export.setProNum(contractProductList.size());
        export.setExtNum(extCproductList.size());
        exportDao.insertSelective(export);

    }

    @Override
    public void update(Export export) {
        exportDao.updateByPrimaryKeySelective(export);
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct ep : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(ep);
            }
        }

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(int page, int size, ExportExample example) {
        PageHelper.startPage(page, size);
        return new PageInfo(exportDao.selectByExample(example));
    }

    @Override
    public void updateByExportResult(ExportResult exportResult) {
        Export export = new Export();
        export.setId(exportResult.getExportId());
        export.setState(exportResult.getState());
        export.setRemark(exportResult.getRemark());
        exportDao.updateByPrimaryKeySelective(export);

        Set<ExportProductResult> products = exportResult.getProducts();
        if (products!=null && products.size()>0) {
            for (ExportProductResult product : products) {
                ExportProduct exportProduct = new ExportProduct();
                exportProduct.setId(product.getExportProductId());
                exportProduct.setTax(product.getTax());
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }
}
