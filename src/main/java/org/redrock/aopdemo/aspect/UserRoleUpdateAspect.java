package org.redrock.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.redrock.aopdemo.dao.UrMappingRepository;
import org.redrock.aopdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@Aspect
public class UserRoleUpdateAspect {

    @Autowired
    UrMappingRepository urMappingRepository;

    @Pointcut("execution(public * org.redrock.aopdemo.controller.MainController.UserRoleChange())")
    public void roleUpdate() {
    }


    @Around("roleUpdate()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if ( user.IsSuper(urMappingRepository.findByUserId(user.getUserId()).getRoleId())) return pjp.proceed();
        else if( 4 == urMappingRepository.findByUserId(user.getUserId()).getRoleId())
            return pjp.proceed();
        else
            return false;
    }


    @Before("roleUpdate()")
    public void before() {

    }

    @After("roleUpdate()")
    public void after() {

    }

    @AfterReturning(value = "roleUpdate()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {

    }


    @AfterThrowing(value = "roleUpdate()", throwing = "e")
    public void afterThrowing(Throwable e) {
        e.printStackTrace();
    }

}
