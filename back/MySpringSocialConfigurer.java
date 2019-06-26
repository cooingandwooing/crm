package com.github.qingyejiazhu.securitycore.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author : gaoxiaofeng
 * @version : V1.0
 * @date : 2018/8/6 12:12
 */
public class MySpringSocialConfigurer extends SpringSocialConfigurer {
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    @Override
    protected <T> T postProcess(T object) {
        // org.springframework.security.config.annotation.SecurityConfigurerAdapter.postProcess()
        // 在SocialAuthenticationFilter中配置死的过滤器拦截地址
        // 这样的方法可以更改拦截的前缀
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
//        filter.setFilterProcessesUrl("/oaths");
//        filter.setAuthenticationSuccessHandler();
        // 让使用处自己获取token成功的逻辑
        if (socialAuthenticationFilterPostProcessor != null) {
            socialAuthenticationFilterPostProcessor.process(filter);
        }
        return (T) filter;
    }

    public SocialAuthenticationFilterPostProcessor getSocialAuthenticationFilterPostProcessor() {
        return socialAuthenticationFilterPostProcessor;
    }

    public void setSocialAuthenticationFilterPostProcessor(SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor) {
        this.socialAuthenticationFilterPostProcessor = socialAuthenticationFilterPostProcessor;
    }
}
