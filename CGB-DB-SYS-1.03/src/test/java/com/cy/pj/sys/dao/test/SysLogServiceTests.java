package com.cy.pj.sys.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysLogServiceTests {
	@Autowired
	private SysLogService sysService;
	@Test
	public void testFindPageObjects() {
		PageObject<SysLog> page = sysService.findPageObjects("admin", 1);
		System.out.println(page);
	}
}
