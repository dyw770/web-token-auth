# EASY-AUTH-SYSTEM

## 项目简介

该项目是一个基于 `spring`、`spring-boot`、`spring-security` 的权限管理系统。核心功能是简化登陆、角色、菜单管理以及授权，同时能够动态配置系统API的访问权限，颗粒度到请求的方法。系统使用`token`作为访问凭据，实现了`token`的自动续期，内置了`map`和`redis`两种`token`存储的实现，开箱即用。

## 模块说明

1. `auth-core` 授权核心模块，实现`token`鉴权的核心逻辑，封装了`token`的生成、存储、验证、续期、销毁等核心功能。导入该模块后只需少量的配置即可实现`token`模式的登陆登出。模块内置`map`和`redis`两种`token`存储模式，可以通过配置切换，也可以实现自己的token存储。
2. `auth-db` 数据库模块，实现权限数据存储，目前仅支持`mysql`。提供了一个`spring-security`的配置，将数据库中的权限配置加载到`spring-security`中，同时也对外提供了权限的编辑接口，将该模块加载后即可实权限的动态配置。
3. `auth-cache` 缓存模块，实现权限缓存，目前仅支持`redis`。由于使用`token`访问系统时每次都需要去数据库中加载权限和角色信息数据库，如果想减轻数据库压力可以引入此模块，引入后配置缓存相关配置后`auth-db`模块会自动使用缓存。
4. `auth-sync` 当权限变动时`auth-db`模块会发布 [AuthChangedApplicationEvent.java](auth-common/src/main/java/cn/dyw/auth/event/AuthChangedApplicationEvent.java) 事件，在水平扩展情况下其他节点无法感知到权限数据发生了改变。如果需要多实例部署则可引入该模块，该模块使用`redis`的发布订阅来实现权限改变事件的监听，当权限数据发生改变时，会通知其他节点，其他节点收到消息后，会重新加载权限数据。
5. `auth-support` 模块提供了一些系统中常用的功能，如使用 [SystemEvent.java](auth-annotation/src/main/java/cn/dyw/auth/annotation/SystemEvent.java) 注解来记录系统日志，以及使用 [EnableSystemAccess.java](auth-support/src/main/java/cn/dyw/auth/support/access/EnableSystemAccess.java) 注解来开启访问日志记录。
6. `auth-ui` 模块提供权限管理界面，基于 [Fantastic-admin](https://fantastic-admin.hurui.me) 实现，实现了常用的用户管理、角色管理、菜单管理、权限管理、日志查询、API授权管理功能
7. `auth-demo` demo示例

## 快速开始

新建`springboot`项目，引入`auth-core`模块，添加如下依赖：

```xml
<dependency>
    <groupId>cn.dyw</groupId>
    <artifactId>auth-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

在启动类中使用`@EnableAuthCore`启用`auth-core`模块：

```java
import cn.dyw.auth.security.configuration.EnableAuthCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAuthCore
@SpringBootApplication
public class AuthDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthDemoApplication.class, args);
    }

}

```

配置`spring security`需要忽略的`url`:

```java
@Bean
public AuthorizeHttpRequestsCustomizer authorizeHttpRequestsCustomizer() throws Exception {
    return (authorize) -> authorize
            .requestMatchers("/user/login").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/doc/**").permitAll()
            .requestMatchers("/favicon.ico").permitAll();
}
```

在`application.yml`中配置`token`存储方式和token的请求头：

```yaml
app:
  auth:
    token-repository: local
    auth-header-name: Authorization # 默认值为 Authorization
```

注入自己项目的`UserDetailsService`(`auth-db`模块提供了`mysql`的实现，如果引入该模块则无需配置自定义的`UserDetailsService`)：

```java
@Bean
@SuppressWarnings("deprecation")
@ConditionalOnMissingBean(UserDetailsService.class)
public UserDetailsService userDetailsService() {
    // 仅测试示例
    UserDetails userDetails = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(userDetails);
}
```


新建`LoginController`实现登陆和登出逻辑：

```java
import cn.dyw.auth.message.Result;
import cn.dyw.auth.security.LoginLogoutHandler;
import cn.dyw.auth.security.TokenAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;


@RestController
@RequestMapping("/user")
public class LoginController {

    private final LoginLogoutHandler loginLogoutHandler;


    public LoginController(LoginLogoutHandler loginLogoutHandler) {
        this.loginLogoutHandler = loginLogoutHandler;
    }

    /**
     * 登录
     *
     * @param loginRq 账号和密码
     * @return 结果
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Validated LoginRq loginRq, HttpServletRequest request) {
        TokenAuthenticationToken login = loginLogoutHandler.login(loginRq.username(), loginRq.password(), request);
        return Result.createSuccess("登录成功", login.getToken().token());
    }

    /**
     * 登出
     *
     * @param authentication 授权信息
     * @param request        request
     * @param response       response
     * @return 结果
     */
    @GetMapping("/logout")
    public Result<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        loginLogoutHandler.logout(authentication, request, response);
        return Result.createSuccess("登出成功");
    }

    /**
     * @param username 用户名
     * @param password 密码
     */
    public record LoginRq(
            @NotBlank
            String username,
            @NotBlank
            String password
    ) {

    }
}
```

`LoginLogoutHandler`类封装了登陆和登出逻辑，调用`login()`方法会返回用户登陆后的token，将token返回给前台即可。如果登陆失败则会抛出异常，只需要注册自己的全局异常处理即可自定义错误处理结果，可以参考`auth-demo`模块中的[GlobalDefaultExceptionHandler.java](auth-demo/src/main/java/cn/dyw/auth/demo/configuration/GlobalDefaultExceptionHandler.java)

完成以上配置后，访问`/user/login`接口即可进行登陆，登陆成功后访问其他接口需要携带登陆获取到的`token`，如果`token`无效则会抛出异常，可以通过全局异常处理捕获到该异常，可以参考可以参考`auth-demo`模块中的[GlobalDefaultExceptionHandler.java](auth-demo/src/main/java/cn/dyw/auth/demo/configuration/GlobalDefaultExceptionHandler.java)。同时该模块还内置了一个[UserManageSupportController.java](auth-core/src/main/java/cn/dyw/auth/security/controller/UserManageSupportController.java)接口，用来获取用户的登陆信息，以及强制下线功能。

默认`token`要求放在请求头中，可以通过`app.auth.auth-header-name`属性修改请求头名称，默认值为`Authorization`， 请求示例：`Authorization: Bearer ${token}`。可以通过自定义`TokenResolve`来实现自己的`token`获取逻辑。

`auth-core`模块只提供了基础授权逻辑，并无权限相关实现，但是该模块完全基于`spring security`，因此`spring security`中的注解依然生效。如果不需要`auth-db`模块提供的动态权限功能，只需要实现`token`访问则只需要引入该模块即可，可以通过`@PreAuthorize`等`spring security`注解控制权限。

## 动态权限控制

如果需要权限动态控制则可以引入`auth-db`模块，该模块提供了用户的角色管理、权限管理、菜单管理、`api`访问权限管理。在上述项目中引入如下依赖并在启动类中增加`@EnableJdbcAuth`注解启用。

```xml
<dependency>
    <groupId>cn.dyw</groupId>
    <artifactId>auth-db</artifactId>
    <version>1.0.0</version>
</dependency>
```

配置`mysql`数据库连接，并且执行`auth-db`模块提供的数据库脚本[db.sql](auth-db/src/main/resources/db.sql)，数据库内置了`admin`用户，密码为`123456`。

如果需要水平扩展部署还需要引入`auth-sync`模块，同时需要`redis`组件，该模块依赖`redis`来发布订阅权限改变事件来实现各节点之间的权限数据同步。引入该模块并配置`redis`连接信息即可。

```xml
<dependency>
    <groupId>cn.dyw</groupId>
    <artifactId>auth-sync</artifactId>
    <version>1.0.0</version>
</dependency>
```

权限信息在每次访问时都需要从`db`中加载角色信息和权限信息，如果访问比较频繁可以引入`auth-cache`模块来缓存角色信息和权限信息以减少数据库的压力，该模块只支持`redis`缓存，如果需要其他缓存只需要注入`CacheManager`实现即可切换缓存的存储实现。

## 管理UI

基于`auth-db`模块提供的权限管理API`auth-ui`模块实现了一个UI管理界面。部分界面如图：

![角色管理](assets/%E8%A7%92%E8%89%B2%E7%AE%A1%E7%90%86.png)

![用户管理](assets/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

![API资源管理](assets/API%E8%B5%84%E6%BA%90%E7%AE%A1%E7%90%86.png)

![API授权](assets/API%E6%8E%88%E6%9D%83.png)

![权限管理](assets/%E6%9D%83%E9%99%90%E7%AE%A1%E7%90%86.png)

![菜单管理](assets/%E8%8F%9C%E5%8D%95%E7%AE%A1%E7%90%86.png)

![菜单权限](assets/%E8%8F%9C%E5%8D%95%E6%9D%83%E9%99%90.png)

![访问日志](assets/%E8%AE%BF%E9%97%AE%E6%97%A5%E5%BF%97.png)

![系统事件](assets/%E7%B3%BB%E7%BB%9F%E4%BA%8B%E4%BB%B6.png)


## 其他功能

`auth-support`模块提供了两个注解`EnableSystemAccess`、`EnableSystemEvent`。

`EnableSystemAccess`注解用来记录系统的访问日志，默认由[DefaultSystemAccessHandler.java](auth-support/src/main/java/cn/dyw/auth/support/access/DefaultSystemAccessHandler.java)记录到日志中，可以自定义实现来记录。在`auth-db`模块提供了[JdbcSystemAccessHandler.java](auth-db/src/main/java/cn/dyw/auth/db/support/JdbcSystemAccessHandler.java)jdbc实现，可以直接注入该类。

`EnableSystemEvent`注解用来启用`SystemEvent`注解，该注解用来记录系统事件，默认由[DefaultSystemEventHandler.java](auth-support/src/main/java/cn/dyw/auth/support/system/DefaultSystemEventHandler.java)来记录，可以自定义实现来记录。在`auth-db`模块提供了[JdbcSystemEventHandler.java](auth-db/src/main/java/cn/dyw/auth/db/support/JdbcSystemEventHandler.java)jdbc实现，可以直接注入该类。`SystemEvent`注解提供了`spel`能力，可以通过`spel`获取参数或者调用方法，详细用法可以参考[UserController.java](auth-demo/src/main/java/cn/dyw/auth/demo/controller/UserController.java)中的`login()`方法。