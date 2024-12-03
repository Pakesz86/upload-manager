package org.pakesz.upload.ftp;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FTPConfig {

    String server;
    int port;
    String user;
    String password;
    String distDirectory;
    String remoteDir;

}
