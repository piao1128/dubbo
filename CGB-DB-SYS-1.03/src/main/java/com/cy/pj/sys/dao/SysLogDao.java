package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.entity.SysLog;

@Mapper
public interface SysLogDao {
	List<SysLog> findPageObjects(
		      @Param("username")String  username,
		      @Param("startIndex")Integer startIndex,
		      @Param("pageSize")Integer pageSize);
	int getRowCount(@Param("username")String name);
	int deleteObjects(@Param("ids")Integer... ids);
	int insertObject(SysLog entity);
	
	
}
