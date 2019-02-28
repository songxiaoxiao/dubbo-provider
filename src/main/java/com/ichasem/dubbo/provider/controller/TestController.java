package com.ichasem.dubbo.provider.controller;

import com.ichasem.dubbo.provider.service.IDemoService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller

public class TestController {
    @Resource
    private IDemoService demoService;

    public void test(){
        String winter = demoService.sayHello("winter");
        System.out.println(winter);
    }
}
