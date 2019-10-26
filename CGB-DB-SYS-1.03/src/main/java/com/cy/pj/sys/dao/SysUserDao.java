package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {
	int insertObject(SysRole entity);
	List<SysUserDeptVo> findPageObjects(
			@Param("username")String username,
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize);
	int getRowCount(@Param("username")String username);
	int validById(@Param("id")Integer id,@Param("valid")Integer valid,
				  @Param("modifiedUser")String modifiedUser);
	int insertObject(SysUser entity);
	SysUserDeptVo findObjectById(Integer id);
	int updateObject(SysUser user);
	SysUser findUserByUserName(String username);
}
