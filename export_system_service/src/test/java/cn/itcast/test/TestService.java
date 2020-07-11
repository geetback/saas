package cn.itcast.test;

import cn.itcast.service.company.CompanyService;
import com.alibaba.druid.filter.AutoLoad;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.*;

@ContextConfiguration("classpath:spring/applicationContext_service.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {
    @Autowired
    CompanyService companyService;
    @Test
    public void test(){
        companyService.findAll();
    }

}
