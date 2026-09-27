package com.imooc.managerAgent;


import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.imooc"})
public class App {

    public static void main(String[] args) throws NacosException
    {

        SpringApplication.run(App.class, args);
    }
}
