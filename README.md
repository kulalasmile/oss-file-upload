# OSS对象云存储

## 介绍

基于阿里云OSS和Minio实现的文件管理接口，通过配置文件控制生效，支持文件上传和下载，文件信息管理，可作为后台系统的文件管理。

## 环境依赖

- 版本

  spring-boot 2.2.8.RELEASE  knife4j 2.0.4  mysql5.7或8

- 依赖

  - 阿里云OSS

    ```xml
    <dependency>
        <groupId>com.aliyun.oss</groupId>
        <artifactId>aliyun-sdk-oss</artifactId>
        <version>3.9.1</version>
    </dependency>
    <dependency>
        <groupId>com.aliyun</groupId>
        <artifactId>aliyun-java-sdk-core</artifactId>
        <version>4.5.9</version>
    </dependency>
    ```

  - minio

    ```xml
    <dependency>
        <groupId>io.minio</groupId>
        <artifactId>minio</artifactId>
        <version>7.1.4</version>
    </dependency>
    ```

    

## 数据库设计

文件信息表用来保存文件基本信息，文件fileName和OSS的objectName保持一致，用于关联文件信息和文件

使用id做业务文件id可实现业务表单一对一保存文件

![image-20201118094156769](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118094156769.png)

使用relateId做业务id可实现业务表单一对多保存文件

![image-20201118094310811](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118094310811.png)





## 使用方式

- 修改application.yml，通过修改oss.type来控制storage bean的注入，显式的进行开关对应的OSS.

- 修改数据库连接配置，启动完成后会自动创建数据表

- 进入localhost:8080/doc.html接口文档，进行测试

  

  ![image-20201104184939926](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201104184939926.png)

1. 上传minio

   1. 选择文件，输入关联编号(可选)，上传单个文件

      ![image-20201118085558931](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118085558931.png)

      ![image-20201118085620760](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118085620760.png)

   2. 根据编号查询url

      ![image-20201118090041513](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090041513.png)

      ![image-20201118090113891](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090113891.png)

   3. 根据关联编号查询url

      ![image-20201118090229456](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090229456.png)

      ![image-20201118090113891](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090113891.png)

   4. 根据编号下载文件

      ![image-20201118090526165](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090526165.png)

   5. 根据文件编号查询文件信息

      ![image-20201118090744309](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090744309.png)

   6. 根据关联编号查询文件信息

      ![image-20201118090659751](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/image-20201118090659751.png)

## 流程

- 上传流程

![](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/20201104183035.png)

- 下载流程

![](https://kulalasmile.oss-cn-hangzhou.aliyuncs.com/blog/20201104183804.png)



## 注意

- 使用springboot 3.0.4版本和knife4j最新版本时，会报错无法启动，可能是依赖冲突原因(查询资料，都说是Guaua版本的原因，我排除knife4j或minio的Guaua依赖都无法解决) 
- 查询文件url时，只能对公共读权限的bucket生效



## 参考

参考了[felord](https://gitee.com/felord)大神的 [oss-spring-boot](https://gitee.com/felord/oss-spring-boot/tree/master)项目，在此基础上更换了新的minio版本，使用新的API，添加数据库保存文件信息。

菜鸟码农，可能会有不少错误，请大家多多指教