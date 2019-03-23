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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Aspect
public class UserFindAspect {

    @Autowired
    UrMappingRepository urMappingRepository;

    @Pointcut("execution(public * org.redrock.aopdemo.controller.MainController.Find())")
    public void userFind() {
    }


    @Around("userFind()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Object[]>  objects = new ArrayList<Object[]>(Collections.singleton(pjp.getArgs())) ;

        if (objects.size() == 1 ){
            if ( objects.get(0).equals(user.getUserId())) return pjp.proceed();
        }

        if ( user.IsSuper(urMappingRepository.findByUserId(user.getUserId()).getRoleId())) return pjp.proceed();
        else if( 3 == urMappingRepository.findByUserId(user.getUserId()).getRoleId())
            return pjp.proceed();
        else
            return false;
    }


    @Before("userFind()")
    public void before() {

    }

    @After("userFind()")
    public void after() {

    }

    @AfterReturning(value = "userFind()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {

    }


    @AfterThrowing(value = "userFind()", throwing = "e")
    public void afterThrowing(Throwable e) {
        e.printStackTrace();
    }

}
