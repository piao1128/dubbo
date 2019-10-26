package com.cy.pj.common.config;
import java.util.LinkedHashMap;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringShiroConfig {
	 @Bean
	 public SecurityManager securityManager(
			  @Autowired Realm realm) {
		 DefaultWebSecurityManager sManager=
		 new DefaultWebSecurityManager();
		 sManager.setRealm(realm);
		 return sManager;
	 }
	 
	 @Bean("shiroFilterFactory")//默认key为方法名
	 public ShiroFilterFactoryBean shiroFilterFactory( @Autowired SecurityManager securityManager) {
		 //构建bean对象，通过此对象创建过滤器工厂。
		 ShiroFilterFactoryBean fBean=
		 new ShiroFilterFactoryBean();
		 //注入SecurityManager
		 fBean.setSecurityManager(securityManager);
		 //设置登录url
		 fBean.setLoginUrl("/doLoginUI");
		 //设置过滤规则
		 LinkedHashMap<String,String> cMap=new LinkedHashMap<>();
		 cMap.put("/bower_components/**", "anon");
		 cMap.put("/build/**", "anon");
		 cMap.put("/dist/**", "anon");
		 cMap.put("/plugins/**", "anon");//anon表示允许匿名访问
		 cMap.put("/user/doLogin", "anon");//anon表示允许匿名访问
		 cMap.put("/doLogout", "logout");
		 cMap.put("/**", "authc");//表示需要认证以后访问
		 fBean.setFilterChainDefinitionMap(cMap);
		 return fBean;
	 } 
	 //授权配置:配置shiro框架中bean对象的生命周期管理器，
	 //此对象的编写要按照spring框架的标准进行实现
	
	 @Bean//默认bean的名字为方法名
	 public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		 return new LifecycleBeanPostProcessor();
	 }
	 /*
	  *此对象会在spring容器启动时，扫描所有的advisor通知对象
	  *给予adcisor对象中切入点pointcut的描述，进行代理对象的创建
	  */
	 @DependsOn("lifecycleBeanPostProcessor")
	 @Bean
		public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator() {
			 return new DefaultAdvisorAutoProxyCreator();
	}
	 /**
	  * 配置advisor对象  再次对象定义切入点以及要在此切入点进行实现功能扩展
	  * @param securityManager
	  * @return
	  */
	 @Bean
	 public AuthorizationAttributeSourceAdvisor 
	authorizationAttributeSourceAdvisor(
	 	    		    @Autowired SecurityManager securityManager) {
		 AuthorizationAttributeSourceAdvisor advisor=
	 				new AuthorizationAttributeSourceAdvisor();
	 advisor.setSecurityManager(securityManager);
	 	return advisor;
	 }
}






