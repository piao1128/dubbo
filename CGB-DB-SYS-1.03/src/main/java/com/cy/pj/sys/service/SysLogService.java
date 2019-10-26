package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysRole;

public interface SysLogService {
	PageObject<SysLog> findPageObjects(
			 String username,
			 Integer pageCurrent);
	int deleteObjects(Integer...ids);
	int saveObject(SysLog entity);
	
}

