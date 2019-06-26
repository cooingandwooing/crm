package com.github.qingyejiazhu.securityapp;

/*
 *
 * ${desc}
 * @author zhuqiang
 * @version 1.0.1 2018/8/7 13:15
 * @date 2018/8/7 13:15
 * @since 1.0
 * */

import com.github.qingyejiazhu.securityapp.social.openid.OpenIdAuthenticationSecurityConfig;
import com.github.qingyejiazhu.securitycore.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.github.qingyejiazhu.securitycore.authorize.AuthorizeConfigManager;
import com.github.qingyejiazhu.securitycore.properties.SecurityConstants;
import com.github.qingyejiazhu.securitycore.properties.SecurityProperties;
import com.github.qingyejiazhu.securitycore.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableResourceServer
public class MyResourcesServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    // 由下面的  .apply(smsCodeAuthenticationSecurityConfigs)方法添加这个配置
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfigs;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    /**
     * @see -SocialConfig #imoocSocialSecurityConfig()
     */
    @Autowired
    private SpringSocialConfigurer imoocSocialSecurityConfig;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    // 有三个configure的方法，这里使用http参数的
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 最简单的修改默认配置的方法
        // 在v5+中，该配置（表单登录）应该是默认配置了
        // basic登录（也就是弹框登录的）应该是v5-的版本默认

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
        ;
        http
                .apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfigs)
                .and()
                .apply(imoocSocialSecurityConfig)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .csrf()
                .disable();
        authorizeConfigManager.config(http.authorizeRequests());
    }
}

