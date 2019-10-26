package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.annotation.RequiredTime;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

import io.micrometer.core.instrument.util.StringUtils;
@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private	SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	public SysRoleServiceImpl(SysRoleDao sysRoleDao, SysRoleMenuDao sysRoleMenuDao, SysUserRoleDao sysUserRoleDao) {
		super();
		this.sysRoleDao = sysRoleDao;
		this.sysRoleMenuDao = sysRoleMenuDao;
		this.sysUserRoleDao = sysUserRoleDao;}
	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码不正确");
		int rowCount=sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("记录不存在");
		int pageSize=2;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysRole> records=sysRoleDao.findPageObjects(name, startIndex, pageSize);
		PageObject<SysRole> pageObject=new PageObject<>();
		  //4.2)封装数据
		  pageObject.setPageCurrent(pageCurrent);
		  pageObject.setPageSize(pageSize);
		  pageObject.setRowCount(rowCount);
		  pageObject.setRecords(records);
        pageObject.setPageCount((rowCount-1)/pageSize+1);
		  //5.返回封装结果。
		  return pageObject;
	}

	@Override
	public int deleteObject(Integer id) {
		if(id==null||id<1)
			throw new IllegalArgumentException("id的值不正确，id="+id);
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		sysUserRoleDao.deleteObjectsByRoleId(id);
		int rows=sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("数据有可能已经不存在");
		
		return rows;
	}
	@RequiredTime
	@Override
	 public int saveObject(SysRole entity,Integer[] menuIds) {
	    	//1.合法性验证
	    	if(entity==null)
	    throw new ServiceException("保存数据不能为空");
	    	if(StringUtils.isEmpty(entity.getName()))
	    	throw new ServiceException("角色名不能为空");
	   	if(menuIds==null||menuIds.length==0)
	    	throw new ServiceException("必须为角色赋予权限");
	    	//2.保存数据
	    	int rows=sysRoleDao.insertObject(entity);
	    	sysRoleMenuDao.insertObjects(
	    			entity.getId(),menuIds);
	    	//3.返回结果
	    	return rows;
	 }
	 @Override
	    public SysRoleMenuVo findObjectById(Integer id) {
	    	//1.合法性验证
	    	if(id==null||id<=0)
	    	throw new ServiceException("id的值不合法");
	    	//2.执行查询
	    	SysRoleMenuVo result=sysRoleDao.findObjectById(id);
	  	//3.验证结果并返回
	    	if(result==null)
	    	throw new ServiceException("此记录已经不存在");
	    	return result;
	    }
	@Override
	public List<CheckBox> findRoles() {
		
		return sysRoleDao.findRoles();
	}
	
		
	

}
