package com.cy.pj.sys.service.realm;

import java.util.HashSet;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
    @Autowired
	private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysMenuDao sysMenuDao;
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
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//1.获取登录用户信息
		SysUser user =(SysUser)principals.getPrimaryPrincipal();
		//2.基于用户id查询用户角色id
		List<Integer> roleIds= sysUserRoleDao.findRoleIdsByUserId(user.getId());
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
		//3.基于用户角色id菜单id
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		if(menuIds==null||menuIds.size()==0)
			throw new AuthorizationException();
		//4.基于菜单id获取菜单权限标识
		List<String> permissions = sysMenuDao.findPermissions(menuIds.toArray(new Integer[] {}));
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//5.封装查询结果并返回（交给授权管理器进行授权检测）。
		HashSet<String> permissionSet = new HashSet<>();
		for(String p:permissions) {
			if(StringUtils.isEmpty(p)) {
				permissionSet.add(p); 
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permissionSet );
		return info;
	}

}
