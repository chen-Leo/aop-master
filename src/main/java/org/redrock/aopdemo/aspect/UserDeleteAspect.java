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
public class UserDeleteAspect {

    @Autowired
    UrMappingRepository urMappingRepository;


    @Pointcut("execution(public * org.redrock.aopdemo.controller.MainController.Delete())")
    public void deleteIf() {
    }

    @Around("deleteIf()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if ( user.IsSuper(urMappingRepository.findByUserId(user.getUserId()).getRoleId())) return pjp.proceed();
        else if (2 == urMappingRepository.findByUserId(user.getUserId()).getRoleId())
            return pjp.proceed();
        else
            return false;
    }



        @Before("deleteIf()")
        public void before() {

        }

        @After("deleteIf()")
        public void after() {

        }

        @AfterReturning(value = "deleteIf()", returning = "obj")
        public void afterReturning(JoinPoint joinPoint, Object obj) {

        }


        @AfterThrowing(value = "deleteIf()", throwing = "e")
        public void afterThrowing(Throwable e) {
            e.printStackTrace();
        }
}
