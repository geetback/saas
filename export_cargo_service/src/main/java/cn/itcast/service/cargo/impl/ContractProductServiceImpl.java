package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.utils.UUIDUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ContractProductServiceImpl implements ContractProductService {
    @Autowired
    ContractProductDao contractProductDao;
    @Autowired
    ContractDao contractDao;
    @Override
    public void save(ContractProduct contractProduct) {

        contractProduct.setId(UUIDUtils.getId());

        Double amount = 0d;
        if (contractProduct.getCnumber()!=null && contractProduct.getPrice()!=null) {
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);

        contractProductDao.insertSelective(contractProduct);

        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        contract.setProNum(contract.getProNum() + 1);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //更新货物表中的数据
        Double oldAmount=contractProductDao.selectByPrimaryKey(contractProduct.getId()).getAmount();
        //设置最新的小计
        Double newAmount=0d;
        if (contractProduct.getCnumber()!=null&&contractProduct.getPrice()!=null){
            newAmount =contractProduct.getCnumber()*contractProduct.getPrice();
        }
        contractProduct.setAmount(newAmount);
        //更新货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        //更新合同表中的数据
        Contract contract =contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //修改合同的总金额 = 原来总金额 - 修改货物的原来金额 + 最新的金额
        contract.setTotalAmount(contract.getTotalAmount()-oldAmount+newAmount);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);


    }

    @Override
    public void deleteById(String id) {

        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Double amount = contractProduct.getAmount();
        contractProductDao.deleteByPrimaryKey(id);

        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setProNum(contract.getProNum() - 1);
        contract.setTotalAmount(contract.getTotalAmount() - amount);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(int page, int size, ContractProductExample example) {
        PageHelper.startPage(page,size);
        return new PageInfo(contractProductDao.selectByExample(example));
    }
}
