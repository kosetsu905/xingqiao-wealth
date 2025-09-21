# RocketMQ ACL 认证配置指南

## 问题背景

在使用RocketMQ发送消息时，出现了以下错误：
```
org.apache.rocketmq.client.exception.MQBrokerException: CODE: 1  DESC: org.apache.rocketmq.acl.common.AclException: No accessKey is configured
```

这表明RocketMQ服务器启用了ACL（访问控制列表）认证，但客户端没有正确配置相应的accessKey和secretKey。

## 解决方案

我们对`xingqiao-common-rocketmq`模块进行了全面更新，以确保ACL认证能够正确生效：

1. 在`RocketMQProperties.java`中添加了`accessKey`和`secretKey`属性
2. 重写了`RocketMQACLConfig.java`，采用更直接的方式配置RocketMQTemplate的ACL认证信息
3. 更新了`RocketMQAutoConfiguration.java`以导入新的ACL配置类

## 配置说明

请在您的应用配置文件（如`application.yml`或`bootstrap.yml`）中添加以下配置：

```yaml
rocketmq:
  # NameServer地址
  nameServer: 47.115.48.217:9876
  # 生产者组
  producerGroup: your-producer-group
  # ACL认证信息（重要）
  accessKey: your-access-key
  secretKey: your-secret-key
  # 可选配置
  sendMessageTimeout: 3000
  retryTimesWhenSendFailed: 2
```

请将`your-access-key`和`your-secret-key`替换为您从RocketMQ管理员处获取的实际认证信息。

## 新的工作原理

我们改进了配置方式，不再通过间接设置Spring Boot的RocketMQProperties来实现，而是直接创建并配置RocketMQTemplate：

1. 当配置文件中同时包含`rocketmq.accessKey`和`rocketmq.secretKey`时，`RocketMQACLConfig`会自动激活
2. 该配置类会直接创建`DefaultMQProducer`实例并设置ACL认证信息（accessKey和secretKey）
3. 然后创建`RocketMQTemplate`实例，并将配置好的`DefaultMQProducer`设置进去
4. 添加了`@ConditionalOnMissingBean`注解，确保只有在没有其他`RocketMQTemplate`实例时才会创建

## 为什么之前的配置不生效

之前的实现方式存在一个问题：我们试图通过创建一个新的`RocketMQProperties`对象来覆盖Spring Boot自动配置的属性，但这并不总是能正确地被RocketMQ Spring Boot Starter获取到。

新的实现方式更加直接和可靠，通过直接创建和配置`RocketMQTemplate`实例，确保ACL认证信息能够正确应用。

## 注意事项

1. 请确保在生产环境中妥善保管您的accessKey和secretKey，不要将它们硬编码在代码中
2. 建议将敏感信息存储在环境变量或配置中心中
3. 如果不需要ACL认证（例如在开发环境中），可以不配置`accessKey`和`secretKey`，系统会使用默认的无认证配置

如有任何问题，请联系系统管理员获取帮助。