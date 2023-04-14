# README

## Environment Configuration

### 1. Project Requirements

* JDK version: JDK 20（也许17也行？）
* Pre-installed Maven tools
* Use IDEA to open this project and change the Maven configuration path in the settings.



## 前后端交互

### 1.Jackson框架

对于新建类，我们使用驼峰命名法：

1. 包名：多单词组成时所有字每都小写:xxxyyyzzz
2. 类名、接口名：多单词组成时，所有单词的首字母大写：XxxYyyZzz（大驼峰命名法）
3. 变量名、方法名：多单词组成时，第一个字母的首字母小写，第二个字母开始每个字母首字母大写：xxxYyyZzz（小驼峰命名法）
4. 变量名：多单词组成时也可以，所有字母都大写，单词之间用下划线连接：XXX_YYY_ZZZ



注意：一定要写get，set方法，例如：

```java
public class User {
    private String mail;
    private String nickname;
    private String password;
    private Boolean isManager;

    public User(String mail, String nickname, String password, Boolean isManager){
        this.mail = mail;
        this.nickname = nickname;
        this.password = password;
        this.isManager = isManager;
    }

    public String getMail() {
        return this.mail;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public Boolean getIsManager() {
        return this.isManager;
    }

}
```



在路由函数里，加上@RestController注解，函数将自动将自定义类 or List or Map 转为JSON对象传为前端！



## Deployment

我们（暂时）使用docker在服务器上部署，当然完全可以直接运行。（使用docker有大炮打文字的嫌疑）

### 1.部署步骤

1. 当我们更新完代码后，可以在本地idea使用maven插件里的life-cycle，先clean在build，目的是更新项目jar包（和docker无关）
2. 在服务器上更新代码后，需要重新打包一下docker镜像，并且生成新的容器

```shell
# 先删除旧的镜像和容器
docker stop 容器id
docker rm 容器id 
docker rmi 镜像id 

docker build -t hemlock:v0.1 . #更新为新的镜像
docker run -d -p 15100:15100 --name hemlockbackend hemlock:v0.1 # 生成新的镜像

docker save -o hemlock.tar hemlock:v0.1# 导出为压缩包
docker load < hemlock.tar# 把压缩包解压
```





### 2.关于文档

BackendApplication中这一段代码是在项目中生成后端接口文档的，默认注释掉了，如果想使用记得改成自己的路径！

```java
        DocsConfig config = new DocsConfig();
        config.setProjectPath("D:\\study\\grade3.2\\大规模实验\\backend"); // 项目根目录
        config.setProjectName("backend"); // 项目名称
        config.setApiVersion("V0.2");       // 声明该API的版本
        config.setDocsPath("D:\\study\\grade3.2\\大规模实验\\backend\\files"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
```

