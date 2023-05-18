## Web-bbs-Server项目后端

![https://github.com/AlanKafka/Web-bbs-Server/blob/master/img/springboot.png](https://github.com/AlanKafka/Web-bbs-Server/blob/master/img/springboot.png)

此项目有通用的论坛后端接口，设计了通用响应类，对信息的脱敏设计类，使用jwt进行鉴权，生成jwt,发送给前端，前端存在window.localStoragelocalStorage,然后在前端的响应头设置，当用户涉及发布文章时，则会鉴权，或者修改用户资料进行鉴权，若未登录，则会转发到登录页面，例如文件上传的功能，页面的动态数据展示，Vue的前后端对接，适合小白，刚刚学完开发项目。

前端 采用 Vue + Ant Design Vue  [前端链接](https://github.com/AlanKafka/we-bbs)

此项目 用到技术栈是: springboot2 、mysql8.0、mybatis、hutool工具包、lombok、java-jwt、log4j、commons-fileupload

```xml
  		<dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>4.2.1</version>
        </dependency>
		<dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
        <dependency>
           <groupId>commons-fileupload</groupId>
           <artifactId>commons-fileupload</artifactId>
            <version>1.3.1</version>
        </dependency>
```

对于mybatis的多对一查询、一对多查询，都有涉猎，mybatis的if标签，等都有运用。

springboot 的 文件上传 MultipartFile ,以及拦截器，针对一Vue访问文件，由于没有图床和服务器，则将后端设置了虚拟目录，让其访问上传的文件。

该项目还在开发中，有些许功能未开发完毕，由于发现 ant design vue 有bug 已反馈，待修复好在进行扩展。

若觉得不错的话，请留下你的starts。
