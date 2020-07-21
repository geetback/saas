package itcast;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StartStatProvider {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath*:spring/applicationContext_*.xml");
        System.in.read();
    }
}
