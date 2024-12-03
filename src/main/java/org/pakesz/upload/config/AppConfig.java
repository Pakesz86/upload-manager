package org.pakesz.upload.config;

import lombok.Getter;
import org.pakesz.upload.UploadManager;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
public class AppConfig {

    public AppConfig() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = UploadManager.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find application.yml");
            }
            Map<String, Object> config = yaml.load(inputStream);

            Map<String, Object> angularConfig = (Map<String, Object>) config.get("angular");
            angularProjectPath = angularConfig.get("project-path").toString();
            angularCompiledPath = angularConfig.get("compiled-path").toString();

            Map<String, Object> ftpConfig = (Map<String, Object>) config.get("ftp");
            ftpServer = ftpConfig.get("server").toString();
            port = Integer.parseInt(ftpConfig.get("port").toString());
            ftpUser = ftpConfig.get("user").toString();
            ftpPassword = ftpConfig.get("password").toString();
            remotePath = ftpConfig.get("remote-path").toString();

            sysDir = System.getProperty("user.dir");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String sysDir;

    String angularProjectPath;
    String angularCompiledPath;


    String ftpServer;
    int port;
    String ftpUser;
    String ftpPassword;
    String remotePath;

}

