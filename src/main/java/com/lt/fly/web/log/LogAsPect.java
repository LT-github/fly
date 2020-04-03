package com.lt.fly.web.log;

import com.lt.fly.dao.ISysLogRepository;
import com.lt.fly.entity.SysLog;
import com.lt.fly.entity.User;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.ResultCode;
import com.lt.fly.web.controller.BaseController;
import com.lt.fly.web.req.UserLogin;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static com.lt.fly.utils.GlobalConstant.LogType.LOGIN;


/**
 * @author Promise
 * @createTime 2018年12月18日 下午9:33:28
 * @description 切面日志配置
 */
@Aspect
@Component
public class LogAsPect extends BaseController {

    private final static Logger log = org.slf4j.LoggerFactory.getLogger(LogAsPect.class);

    @Autowired
    private ISysLogRepository iSysLogRepository;


    @Autowired
    private IdWorker idWorker;

    //表示匹配带有自定义注解的方法
    @Pointcut("@annotation(com.lt.fly.web.log.Log)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
    	long beginTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
            long endTime = System.currentTimeMillis();
			insertLog(point,endTime-beginTime);
        } catch (Throwable e) {
            afterThrowing(point, e);
            return HttpResult.failure(ResultCode.SERVER_ERROR.getCode(), e.getMessage());
        }
        return result;
    }

    /**
     * 在目标方法出现异常时会执行的代码.
     * 可以访问到异常对象; 且可以指定在出现特定异常时在执行通知代码
     */
    @AfterThrowing(value = "pointcut()",
            throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        String methodName = joinPoint.getSignature().getName();
        log.error("The method " + methodName + " occurs excetion:" + e);
    }

    private void insertLog(JoinPoint point,long time) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        SysLog sys_log = new SysLog();

        User user = null;

        Parameter[] parameters = method.getParameters();

        Log userAction = method.getAnnotation(Log.class);
        if (userAction != null) {
            // 注解上的描述
            sys_log.setUserAction(userAction.value());
            if (userAction.type().equals(LOGIN)) {//登录操作
				UserLogin req = (UserLogin)point.getArgs()[0];
				user = iUserRepository.findByUsername(req.getUsername());
            } else {
                user = getLoginUser();
            }
        }

        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        // 请求的方法参数值
        String args = Arrays.toString(point.getArgs());

        sys_log.setId(idWorker.nextId());

        sys_log.setCreateUser(user);


        sys_log.setCreateTime(System.currentTimeMillis());

        log.info("当前登陆人：{},类名:{},方法名:{},参数：{},耗时 : {}", user.getUsername(), className, methodName, args, time);

        iSysLogRepository.save(sys_log);
    }
}
