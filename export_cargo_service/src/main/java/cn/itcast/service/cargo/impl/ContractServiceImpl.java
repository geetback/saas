package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.utils.UUIDUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service
public class ContractServiceImpl implements ContractService{

    @Autowired
    ContractDao contractDao;

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {

        contract.setId(UUIDUtils.getId());
        contract.setTotalAmount(0d);
        contract.setProNum(0);
        contract.setExtNum(0);
        contract.setCreateTime(new Date());

        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {

        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void deleteById(String id) {

        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(int page, int size, ContractExample example) {
        PageHelper.startPage(page,size);
        return new PageInfo(contractDao.selectByExample(example));
    }
}
