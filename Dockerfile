FROM openjdk:21-oraclelinux8
# 指定维护者名称
MAINTAINER ly 3052791719@qq.com

VOLUME /tmp
WORKDIR /data
# 将targer目录下的jar包复制到docker容器/home/springboot目录下面目录下面
ADD ./target/backend-0.0.1-SNAPSHOT.jar /data
# 声明服务运行在15000端口
EXPOSE 15100
# 执行命令
ENTRYPOINT ["java","-jar","/data/backend-0.0.1-SNAPSHOT.jar"]


## 该镜像需要依赖的基础镜像
#FROM ubuntu:latest
##FROM lhl-docker-java8:1.0
## 指定维护者名称
#MAINTAINER ly 3052791719@qq.com
##WORKDIR /javaxl_docker/jdk
#
## 拷贝jdk20到容器，使用ADD命令可以直接解压
#ADD jdk-20_linux-x64_bin.tar.gz /javaxl_docker/jdk/
##5.配置环境变量
#ENV JAVA_HOME=/javaxl_docker/jdk/jdk-20
#ENV CLASSPATH=.:$JAVA_HOME/lib
#ENV PATH=$JAVA_HOME/bin:$PATH