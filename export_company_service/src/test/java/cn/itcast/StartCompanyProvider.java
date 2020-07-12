package cn.itcast;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StartCompanyProvider {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        System.in.read();
    }
}
