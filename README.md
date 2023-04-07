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
