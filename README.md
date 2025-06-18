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

