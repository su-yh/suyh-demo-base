package com.suyh.base.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author suyh
 * @since 2024-09-14
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable();

        http.logout().disable();

        // 不使用spring security 的认证系统
        // permitAll 所有接口都不需要认证，这里是不使用spring scurity 的认证实现，而使用 自定义的拦截器实现认证的判断。
        http.authorizeRequests().antMatchers("/**").permitAll().and().csrf().disable();   // 关闭csrf 防护
    }
}
