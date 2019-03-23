package org.redrock.aopdemo.annotation;

//自定义缓存注解 清空所有缓存
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClearCache {

}

