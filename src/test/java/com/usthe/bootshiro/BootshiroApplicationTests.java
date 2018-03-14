package com.usthe.bootshiro;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BootshiroApplicationTests {

	@Autowired
    private StringRedisTemplate template = new StringRedisTemplate();

	@Test
	public void contextLoads() {
		template.opsForValue().set("hahahaha","lallal");
		Assert.assertEquals("lallal",template.opsForValue().get("hahahaha"));
	}

}