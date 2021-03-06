/**
 * 
 */
package com.github.qingyejiazhu.securitybrowser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.qingyejiazhu.securitycore.support.SimpleResponse;
import com.github.qingyejiazhu.securitycore.support.SocialUserInfo;
import com.github.qingyejiazhu.securitycore.properties.SecurityConstants;
import com.github.qingyejiazhu.securitycore.properties.SecurityProperties;
import com.github.qingyejiazhu.securitycore.social.SpringSocialConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author zhailiang
 *
 */
@RestController
public class BrowserSecurityController {
	Logger logger = LoggerFactory.getLogger(getClass());
	// 封装了引发跳转请求的工具类，看实现类应该是从session中获取的
	private RequestCache requestCache = new HttpSessionRequestCache();

	// spring的工具类：封装了所有跳转行为策略类
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * see {@link SpringSocialConfig#providerSignInUtils(org.springframework.social.connect.ConnectionFactoryLocator, org.springframework.social.connect.UsersConnectionRepository)}
	 */
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	/**
	 * 当需要身份认证时跳转到这里
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL) //("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse requirAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

		SavedRequest savedRequest = requestCache.getRequest(request, response);
		// 如果有引发认证的请求
		// spring 在跳转前应该会把信息存放在某个地方？
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			logger.info("引发跳转的请求：" + targetUrl);
			// 如果是html请求，则跳转到登录页
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
			}
		}
		// 否则都返回需要认证的json串
		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
	}
	@GetMapping("/social/user")
	public SocialUserInfo getSocialUserInfo(javax.servlet.http.HttpServletRequest request) {
		SocialUserInfo userInfo = new SocialUserInfo();
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		userInfo.setProviderId(connection.getKey().getProviderId());
		userInfo.setProviderUserId(connection.getKey().getProviderUserId());
		userInfo.setNickname(connection.getDisplayName());
		userInfo.setHeadimg(connection.getImageUrl());
		return userInfo;
	}
	// session 失效的 处理地址
	@GetMapping("/session/invalid")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public SimpleResponse sessionInvalid() {
		String message = "session失效";
		return new SimpleResponse(message);
	}
}