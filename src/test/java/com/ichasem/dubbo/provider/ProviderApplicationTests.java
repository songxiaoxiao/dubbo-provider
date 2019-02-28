package com.ichasem.dubbo.provider;

import com.ichasem.dubbo.provider.service.IDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProviderApplicationTests {

	@Resource
	private IDemoService demoService;
	@Test
	public void contextLoads() {
		String winter = demoService.sayHello("winter");
		System.out.println(winter);
	}

}
