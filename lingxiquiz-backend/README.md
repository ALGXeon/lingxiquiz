# 后端部分

## 智谱AI API key设置

两种方式

- 获取到自己的AI API key之后，将API key配置到电脑的环境变量中  
`lingxiquizZhiPuAIAPI_KEY: {your_api_key}`  
- 在src/main/resources/application.properties中配置  
将`{lingxiquizZhiPuAIAPI_KEY}`替换为你的API key  

## 数据库连接信息
```yml
spring:
    datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/lingxiquiz
    username: root
    password: ${MYSQLROOTPASSWORD}
```

替换成你自己的mysql  