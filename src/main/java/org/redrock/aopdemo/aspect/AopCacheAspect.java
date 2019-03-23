
package org.redrock.aopdemo.aspect;
import java.security.Key;
import java.util.*;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopCacheAspect {


    private static Map<String, Object> aopCache = Collections.synchronizedMap(new HashMap<String, Object>());


    @Around("@annotation(org.redrock.aopdemo.annotation.GetCacheable)")
    public Object getCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        String key = generateKey(proceedingJoinPoint);

        result = aopCache.get(key);
        if (result == null) {
            result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            aopCache.put(key, result);
        }

        return result;
    }

    @Around("@annotation(org.redrock.aopdemo.annotation.UpdateCacheEvict)")
    public Object evict(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        String key = generateKey(proceedingJoinPoint);  //生成缓存key

        result = aopCache.get(key);
        if (result != null) {
            aopCache.remove(key);
        }
        return proceedingJoinPoint.proceed();
    }

    @Around("@annotation(org.redrock.aopdemo.annotation.ClearCache)")
    public void clear(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        aopCache = Collections.synchronizedMap(new HashMap<String, Object>());
    }

    // 生成缓存 key
    private String generateKey(ProceedingJoinPoint pJoinPoint)  {
        StringBuffer cacheKey = new StringBuffer();
        String type = pJoinPoint.getArgs().getClass().getSimpleName();
        Object[] params = pJoinPoint.getArgs();
        cacheKey.append(StringUtils.join(params[0]));
        cacheKey.append(type);
        return cacheKey.toString();

    }


}