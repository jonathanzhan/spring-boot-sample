/*
 * Copyright  2014-2016 whatlookingfor@gmail.com(Jonathan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whatlookingfor.sample.config;

import com.google.common.collect.Maps;
import com.whatlookingfor.sample.security.FormAuthenticationFilter;
import com.whatlookingfor.sample.security.SystemAuthorizingRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro的权限配置
 *
 * @author Jonathan
 * @version 2016/6/1 14:38
 * @since JDK 7.0+
 */
@Configuration
public class ShiroConfiguration {


	@Bean(name = "realm")
	public SystemAuthorizingRealm systemAuthorizingRealm(){
		return new SystemAuthorizingRealm();
	}

	@Bean(name = "cacheManager")
	public EhCacheManager cacheManager(){
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:cache/ehcache-local.xml");
		return ehCacheManager;
	}

	@Bean(name= "securityManager")
	public DefaultWebSecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(systemAuthorizingRealm());
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}


	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager());
		return aasa;
	}


	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的连接
		shiroFilterFactoryBean.setSuccessUrl("/user");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		// 添加casFilter到shiroFilter中
		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("authc",new FormAuthenticationFilter());
		shiroFilterFactoryBean.setFilters(filters);

		Map<String,String> shiroFilterChainDefinitions = Maps.newHashMap();
		shiroFilterChainDefinitions.put("/static/**","anon");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinitions);
		return shiroFilterFactoryBean;
	}




}
