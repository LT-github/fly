package com.lt.fly.web.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.lt.fly.annotation.PassToken;
import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IAdminRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Admin;
import com.lt.fly.entity.Authority;
import com.lt.fly.entity.Role;
import com.lt.fly.entity.User;
import com.lt.fly.utils.GlobalUtil;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.ResultCode;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * @author jinbin
 * @date 2018-07-08 20:41
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    IAdminRepository iAdminRepository;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        String uri = httpServletRequest.getRequestURI();
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (null == token || StringUtils.equals(token, "")) {
                    return error("无token，请重新登录", httpServletResponse);
                }
                // 获取 token 中的 user id
                String userId;
                String ip;
                try {
                	String tokenValue = JWT.decode(token).getAudience().get(0);
                	if(!tokenValue.contains("-"))
                		throw new RuntimeException("无效的token，请重新登录");

                	String[] split = tokenValue.split("-");
                	if(null == split || split.length != 2)
                		throw new RuntimeException("无效的token，请重新登录");

                	userId = split[0];
                	ip = split[1];
                }catch (Exception e) {
                	return error(e.getMessage(), httpServletResponse);
                }

                try {
	                String cliectIp = GlobalUtil.getCliectIp(httpServletRequest);
	                if(!StringUtils.equals(ip,cliectIp)) {
	                	throw new RuntimeException("无效的token，请重新登录");
	                }

	                String password = validation(Long.parseLong(userId),uri , cliectIp,object);
	                // 验证 token
	                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(password)).build();//;
                    jwtVerifier.verify(token);
                } catch (Exception e) {
                	return error(e.getMessage(), httpServletResponse);
                }
                return true;
            }
        }
        return true;
    }

    private String validation(Long id,String uri,String ip,Object object) {

    	Optional<User> optional = iUserRepository.findById(id);

    	if(!optional.isPresent()) {
    		throw new RuntimeException("用户不存在");
    	}

    	User user = optional.get();
    	String discriminator = user.getDiscriminator();

    	if(StringUtils.equals(discriminator, Admin.class.getSimpleName())) {
    		if(uri.contains("/operation")) {
    			throw new RuntimeException("权限不足");
    		}
    	}

//    	if(StringUtils.equals(discriminator, Store.class.getSimpleName())) {
//    		if(!uri.contains("/operation")) {
//    			throw new RuntimeException("权限不足");
//    		}
//    	}

    	user.setIp(ip);
    	user.setLastLoginTime(System.currentTimeMillis());
    	iUserRepository.save(user);

    	 // 验证是否有访问该方法的权限
        if (this.hasPermission(object,id)) {
            return user.getPassword();
        }else {
        	throw new RuntimeException("权限不足0");
        }


    	//return user.getPassword();
	}

    /**
     * 验证是否有访问该方法的权限
     */
    private boolean hasPermission(Object handler,Long userId) {


      if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethods = (HandlerMethod)handler;
            // 获取方法上的注解
            RequiredPermission requiredPermission = handlerMethods.getMethod().getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethods.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            // 如果注解为null, 说明不需要拦截, 直接放过
            if (requiredPermission == null) {
                return true;
            }
            // 如果标记了注解，则判断权限
            if (org.apache.commons.lang3.StringUtils.isNotBlank(requiredPermission.value())) {
                // 应该到 redis 或数据库 中获取该用户的权限信息 并判断是否有权限
            	if(userId==null)
            		throw new RuntimeException("无用户id");
            	Optional<User> optional = iUserRepository.findById(userId);
            	if(!optional.isPresent()) {
            		throw new RuntimeException("用户不存在");
           	}
            	// 这里测试使用 直接add
                Set<String> permissionSet  = new HashSet<>();
            	User user = optional.get();
            	if(user instanceof Admin) {
            		Admin admin =(Admin)user;
            	Set<Role> roles = admin.getRoles();

            	if(roles==null || roles.size()==0) {
            		throw new RuntimeException("您暂无任何角色权限");
            	}
            	for (Role role : roles) {
            		Set<Authority> authoritys = role.getAuthoritys();
            		if(authoritys==null || authoritys.size()==0)
            			continue;
            		for (Authority authority : authoritys) {
            			if(authority.getIdentifier()==null)
            				continue;
            			if(StringUtils.equals(authority.getIdentifier(), "/"))
            				return true;
            			permissionSet.add(authority.getIdentifier());

            		}

				}
            	  }
//<------------------------->
//            	if(user instanceof Store) {
//            		Store store=(Store)user;
//            		 Long liushuiId = store.getGroup().getLiushui().getId();
//            		 Long duijiangId = store.getGroup().getDuijiang().getId();
//            		 Long yingkuiId =store.getGroup().getYingkui().getId();
//            		 permissionSet.add(liushuiId.toString());
//            		 permissionSet.add(duijiangId.toString());
//            		 permissionSet.add(yingkuiId.toString());
//
//
//            	}
                if (CollectionUtils.isEmpty(permissionSet)) {
                	 return false;
                }

                return permissionSet.contains(requiredPermission.value());
            }
        }
        return true;

	}
	public boolean error(String msg,HttpServletResponse response) {
    	try {
    		//设置缓存区编码为UTF-8编码格式
        	response.setCharacterEncoding("UTF-8");
        	//在响应中主动告诉浏览器使用UTF-8编码格式来接收数据
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
          //可以使用封装类简写Content-Type，使用该方法则无需使用setCharacterEncoding

            Object json = JSONObject.toJSON(HttpResult.failure(ResultCode.CLIENT_ERROR.getCode(),msg));//;
        	response.getWriter().print(json.toString());
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
