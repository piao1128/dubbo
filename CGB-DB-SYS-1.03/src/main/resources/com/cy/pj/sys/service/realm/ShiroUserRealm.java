package com.cy.pj.sys.service.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.entity.SysUser;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
    @Autowired
	private SysUserDao sysUserDao;
    /**
          *  设置凭证匹配器，通过此对象指定加密算法
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
    	//构建凭证匹配器对象
    	HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher();
    	//设置算法
    	cMatcher.setHashAlgorithmName("MD5");
    	//设置加密次数
    	cMatcher.setHashIterations(1);
    	//传递凭证匹配器
    	super.setCredentialsMatcher(cMatcher);
    }
	/**
         * 负责认证信息的获取及封装
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
		AuthenticationToken token) throws AuthenticationException {
		//1.获取用户名(从token获取)
		UsernamePasswordToken upToken=
		(UsernamePasswordToken)token;
		String username=upToken.getUsername();
		//2.基于用户名查询用户对象
		SysUser user=
		sysUserDao.findUserByUserName(username);
		//3.判定用户是否存在
		if(user==null)
			throw new UnknownAccountException();
		//4.判定用户是否被禁用
		if(user.getValid()==0)
			throw new LockedAccountException();
		//5.封装用户信息并返回，传递给认证管理器进行认证
		ByteSource credentialsSalt=
		ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info=
		new SimpleAuthenticationInfo(
				user,//principal身份
				user.getPassword(),//hashedCredentials 已加密的凭证
				credentialsSalt,//credentialsSalt 
				getName());
		return info;//交给认证管理器
	}
	/**
	  * 负责授权信息的获取及封装
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

}
