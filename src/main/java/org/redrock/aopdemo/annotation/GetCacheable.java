package org.redrock.aopdemo.annotation;

//自定义缓存注解 获取缓存

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface GetCacheable {

}
