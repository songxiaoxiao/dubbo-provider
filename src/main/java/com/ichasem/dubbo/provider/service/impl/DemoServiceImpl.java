package com.ichasem.dubbo.provider.service.impl;

import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements com.winter.api.DemoService{
    public String sayHello(String name){
        return "hello 啊 " + name;
    }
}
