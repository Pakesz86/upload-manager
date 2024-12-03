package org.pakesz.upload;

import org.pakesz.upload.angular.AngularProcessBuilder;
import org.pakesz.upload.angular.AngularProcessConfig;
import org.pakesz.upload.config.AppConfig;
import org.pakesz.upload.ftp.FTPConfig;
import org.pakesz.upload.ftp.FTPProcessBuilder;

public class UploadManager {

    public static void main(String[] args) {
        try {
            AppConfig config = new AppConfig();

            AngularProcessConfig angularConfig = AngularProcessConfig.builder()
                    .mainRootPath(config.getSysDir())
                    .angularProjectPath(config.getAngularProjectPath())
                    .angularCompiledPath(config.getAngularCompiledPath())
                    .build();

            AngularProcessBuilder angularProcessBuilder = new AngularProcessBuilder();
            angularProcessBuilder.clearBefore(angularConfig.getAngularCompiledPath());
            angularProcessBuilder.runBuild(angularConfig.getAngularProjectPath());


            FTPProcessBuilder ftpProcessBuilder = new FTPProcessBuilder(
                    FTPConfig.builder()
                            .server(config.getFtpServer())
                            .port(config.getPort())
                            .user(config.getFtpUser())
                            .password(config.getFtpPassword())
                            .distDirectory(angularConfig.getAngularCompiledPath())
                            .remoteDir(config.getRemotePath())
                            .build());

            ftpProcessBuilder.clear();
            ftpProcessBuilder.upload();

            System.out.println("Successfully uploaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
