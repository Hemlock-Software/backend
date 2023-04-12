package com.hemlock.www.backend;


import com.hemlock.www.backend.Redis.ClusterRedisIO;
import com.hemlock.www.backend.Redis.SingleRedisIO;
import com.hemlock.www.backend.Token.TokenManager;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendApplication {
    public static SingleRedisIO ColdData = null;
    public static ClusterRedisIO HotData = null;
    public static TokenManager TokenServer = null;
    public static void main(String[] args) {
        //生成api文档
        DocsConfig config = new DocsConfig();
        config.setProjectPath("D:\\study\\grade3.2\\大规模实验\\backend"); // 项目根目录
        config.setProjectName("backend"); // 项目名称
        config.setApiVersion("V0.2");       // 声明该API的版本
        config.setDocsPath("D:\\study\\grade3.2\\大规模实验\\backend\\files"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档

        ColdData = new SingleRedisIO("10.214.241.121",15000);
        HotData = new ClusterRedisIO("10.214.241.121",15010);
        TokenServer = new TokenManager();
        SpringApplication.run(BackendApplication.class, args);
    }

}
