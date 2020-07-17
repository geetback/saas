package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.cargo.ContractProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


public interface ContractService {

	//根据id查询
    Contract findById(String id);

    //保存
    void save(Contract contract);

    //更新
    void update(Contract contract);

    //删除
    void deleteById(String id);

    //分页查询
	PageInfo findAll(int page, int size,ContractExample example);
    //根据船期查询合同和货物的信息
    List<ContractProductVo> findByShipTime(String shipTime, String companyId);
}
