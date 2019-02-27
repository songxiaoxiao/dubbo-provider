package com.winter.dubbo.provider.service;

public class DemoServiceImpl implements DemoService {
    public String sayHello(String name){
        return "hello " + name;
    }
}
