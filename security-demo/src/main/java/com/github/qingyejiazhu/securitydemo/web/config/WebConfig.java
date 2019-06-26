package com.github.qingyejiazhu.securitydemo.web.config;

import com.github.qingyejiazhu.securitydemo.web.filter.TimeFilter;
import com.github.qingyejiazhu.securitydemo.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

/**
 * ${desc}
 * @author zhuqiang
 * @version 1.0.1 2018/8/2 14:50
 * @date 2018/8/2 14:50
 * @since 1.0
 */
//@Configuration
// org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter 5.0+已过时
// 使用了jdk8 的接口默认方法
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 异步支持
//        configurer.registerCallableInterceptors(); // callable 拦截器
//        configurer.registerDeferredResultInterceptors(); // deferredResult拦截器
//        configurer.setTaskExecutor(); // 应该是自定义线程池
//        configurer.setDefaultTimeout() // 超时设置
    }

    @Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TimeFilter());
        // 可以自定义拦截路径
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可以添加多个不同的拦截器
        registry.addInterceptor(timeInterceptor);
    }
}
