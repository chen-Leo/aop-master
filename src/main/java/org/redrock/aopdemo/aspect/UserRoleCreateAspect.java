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
public class UserRoleCreateAspect {

     @Autowired
     UrMappingRepository urMappingRepository;

    @Pointcut( "execution(public * org.redrock.aopdemo.controller.MainController.UserRoleCreate())")
    public void roleChange() {
    }


    @Around("roleChange()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if ( user.IsSuper(urMappingRepository.findByUserId(user.getUserId()).getRoleId())) return pjp.proceed();
        else if( 5 == urMappingRepository.findByUserId(user.getUserId()).getRoleId())
            return pjp.proceed();
        else
            return false;
    }


    @Before("roleChange()")
    public void before() {

    }

    @After("roleChange()")
    public void after() {

    }

    @AfterReturning(value = "roleChange()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {

    }


    @AfterThrowing(value = "roleChange()", throwing = "e")
    public void afterThrowing(Throwable e) {
        e.printStackTrace();
    }

}
