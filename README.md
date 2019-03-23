# AOP

> 面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。
>
> github：

## 基本概念

面向对象的程序设计(OOP)一般是垂直的各个模块，各个模块中间或多或少需要一些共同的功能，这些功能是横向的，为了提升开发效率与代码可复用性，我们把这些横向的逻辑抽离出来，称为：切面。

![横向与竖向](https://lc-gold-cdn.xitu.io/efa47f28e884f98d3a0c?imageslim)

其实面向切面编程的思想很早就已经被使用了，比如：

- 过滤器

## Spring Aop

### 术语介绍

#### PointCut

切点，表示要在从哪里切入。

比如：我需要给所有controller里的方法添加一个参数校验切面，这些方法就是切点。

#### Aspect

切面，抽离出来的横向的逻辑。

#### JoinPoint

连接点，方法被拦截后，针对于方法和参数的封装。

#### Advice

拦截后要执行的代码，切面的具体逻辑。

### 一个小Demo

```java
@RestController
public class MainController {
    @GetMapping("/hello")
    public String hello(){
        System.out.println("hello");
        return "hello world!";
    }
}

@Component
@Aspect
public class TestAspect {
    @Pointcut("execution(public * org.redrock.aopdemo.controller.*.*(..))")
    public void test() {
    }

    @Before("test()")
    public void before() {
        System.out.println("aop-----before");
    }

    @After("test()")
    public void after() {
        System.out.println("aop-----after");
    }

    @AfterReturning(value = "test()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {
        System.out.println("aop-----afterReturning");
    }

    @Around("test()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object res = null;
        System.out.println("aop-----around 1");
        res = pjp.proceed();
        System.out.println("aop-----around 2");
        return res;
    }

    @AfterThrowing(value = "test()", throwing = "e")
    public void afterThrowing(Throwable e) {
        e.printStackTrace();
    }

}
```

我们运行并对'/hello'发送一个请求可以看到结果为：

```
aop-----around 1
aop-----before
hello
aop-----around 2
aop-----after
aop-----afterReturning
```

这里具体是怎么实现的呢？这里我给一个思路：

```java
Method target;
    Method before;
    Method after;
    Method around;
    Method afterReturning;
    Handle handle;

    Object res;

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        res = around.invoke(this);
        after.invoke(this);
        afterReturning.invoke(this);
        return res;
    }

    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        before.invoke(this);
        Object obj = target.invoke(handle.getArgs());
        return obj;
    }
```

我们复制`TestAspect`的代码，并再添加一个切面，命名为`TestAspect2`，运行，可以得到：

```
aop2-----around 1
aop2-----before
aop-----around 1
aop-----before
hello
aop-----around 2
aop-----after
aop-----afterReturning
aop2-----around 2
aop2-----after
aop2-----afterReturning
```

我们可以发现，aop是原方法的代理，aop2是aop的代理。这里是如何实现的呢？。

### 链式代理

我们曾经写过滤器(Filter)的时候可能会写以下几个类型的过滤器：

- 字符类型转换
- 权限控制

着就会造成有一些uri对应的方法需要接连调用这两个。你应该会发现，这里和我们刚才提到的两个AOP是同样的逻辑。这里我们拥有两个解决方案：

- 创建目标的代理对象aop，然后再创建aop的代理对象aop2(本质也是一条链)
- 创建一条链，将目标对象至于链尾，代理的逻辑放在目标对象的前面。(这里是一个责任链模式)

## 作业及其他内容

- level 1

  使用`springboot` + `springAOP`，完成以下两个模块之一：

  - 权限控制模块

    要求：

    - 采用rbac权限控制
    - 实现方法不限
    - 不能照抄demo

  - 缓存模块 (*解释见下文)

    要求：

    - 缓存模块不得与关系型数据库交互(如：Mysql)
    - 可定义缓存时间
    - 不能把照抄demo

- level 2

  同时完成`level 1`中的两个模块

  (*注意切面顺序)

- level 3

  根据上节课的内容与[代码](https://github.com/MashiroC/IOC-AOP-teaching)，联系这节课的内容，自己实现一个拦截器

  要求：

  - 使用方法和Servlet原生的Filter相似即可。
  - 多个Filter可以拦截同一个uri。
  - 可以控制是否访问对应的controller

### 缓存

通常我们收到的每次请求，都会进行一定的计算或对关系型数据库的访问。当一个接口短时间会收到大量参数相同的请求时，每次请求都会消耗一定的性能，返回同样的结果。在这种情况下，我们就可以使用`缓存`来存储请求对应的结果，来减小系统开销。例如：

```java
	@GetMapping("/welcome")
    public String welcome(String name){
        System.out.println("我需要计算!");
        try {
            Thread.sleep(5 * Timer.ONE_SECOND);//模拟计算和对关系型数据库访问的开销
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "welcome!"+name;
    }
```

假如我们在短时间内对该接口发送大量请求，由于访问数的增多，关系型数据库的压力增加，每个请求都会至少在5s后返回结果，**这些结果都是一模一样的** 。这里，我们就可以使用缓存。

当一个请求进来时，首先去缓存器里查找请求是否有对应的缓存，若有，不执行方法，直接从缓存器获取返回值写到响应里。若没有，则执行方法，拦截返回值，获取并写入缓存器。

```java
@Around("cache()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Cache isCache = method.getDeclaredAnnotation(Cache.class);
        if (isCache != null) {
            String key = method.getName() + "/" + Arrays.toString(pjp.getArgs());
            Object value = cacheMap.get(key);
            if (value != null) {
                return value;
            }
        }
        return pjp.proceed();
    }

    @AfterReturning(value = "cache()", returning = "object")
    public void after(JoinPoint joinPoint, Object object) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Cache isCache = method.getDeclaredAnnotation(Cache.class);
        if (isCache != null) {
            String key = method.getName() + "/" + Arrays.toString(joinPoint.getArgs());
            cacheMap.putIfAbsent(key, object);
        }
    }
```

