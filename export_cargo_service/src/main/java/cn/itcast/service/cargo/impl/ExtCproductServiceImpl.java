package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.utils.UUIDUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    ExtCproductDao extCproductDao;

    @Autowired
    ContractDao contractDao;

    @Override
    public void save(ExtCproduct extCproduct) {

        extCproduct.setId(UUIDUtils.getId());
        Double amount = 0d;
        if (extCproduct.getPrice()!=null && extCproduct.getCnumber()!=null) {
            amount =extCproduct.getPrice()*extCproduct.getCnumber();
        }
        extCproduct.setAmount(amount);
        extCproductDao.insertSelective(extCproduct);
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());

        contract.setExtNum(contract.getExtNum() + 1);

        contract.setTotalAmount(contract.getTotalAmount() + amount);

        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        Double oldAmount = extCproductDao.selectByPrimaryKey(extCproduct.getId()).getAmount();

        Double newAmount = 0d;
        if (extCproduct.getPrice()!=null && extCproduct.getCnumber()!=null) {
            newAmount =extCproduct.getPrice()*extCproduct.getCnumber();
        }

        extCproduct.setAmount(newAmount);

        extCproductDao.updateByPrimaryKeySelective(extCproduct);

        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-oldAmount+newAmount);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void deleteById(String id) {
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double amount = extCproduct.getAmount();

        extCproductDao.deleteByPrimaryKey(id);

        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setExtNum(contract.getExtNum() - 1);
        contract.setTotalAmount(contract.getTotalAmount() - amount);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(int page, int size, ExtCproductExample example) {
        PageHelper.startPage(page,size);
        return new PageInfo(extCproductDao.selectByExample(example));
    }
}
