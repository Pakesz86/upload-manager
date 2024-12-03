package org.pakesz.upload.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;


public class FTPProcessBuilder {

    FTPConfig ftpConfig;

    FTPClient ftpClient;

    public FTPProcessBuilder(FTPConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public void clear() throws IOException {
        FTPClient ftpClient = null;
        try {
            ftpClient = createFTPClient();
            deleteFiles(ftpClient, ftpConfig.getRemoteDir());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logoutFTP(ftpClient);
        }
    }

    public void upload() throws IOException {
        FTPClient ftpClient = null;
        try {
            ftpClient = createFTPClient();
            File buildDir = new File(ftpConfig.getDistDirectory());
            uploadDirectory(ftpClient, buildDir, ftpConfig.getRemoteDir());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logoutFTP(ftpClient);
        }
    }

    private void logoutFTP(FTPClient ftpClient) throws IOException {
        if (ftpClient == null) {
            return;
        }
        ftpClient.logout();
        ftpClient.disconnect();
    }

    private FTPClient createFTPClient() throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(ftpConfig.getServer(), ftpConfig.getPort());
        ftpClient.login(ftpConfig.getUser(), ftpConfig.getPassword());
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    private static void uploadDirectory(FTPClient ftpClient, File localDir, String remoteDir) throws IOException {
        if (!localDir.isDirectory()) {
            throw new IllegalArgumentException("A megadott lokális könyvtár nem érvényes.");
        }

        for (File file : Objects.requireNonNull(localDir.listFiles())) {
            String remoteFilePath = remoteDir + "/" + file.getName();
            if (file.isFile()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    System.out.println("Fájl feltöltése: " + remoteFilePath);
                    ftpClient.storeFile(remoteFilePath, fis);
                }
            } else if (file.isDirectory()) {
                System.out.println("Könyvtár létrehozása: " + remoteFilePath);
                if (!ftpClient.changeWorkingDirectory(remoteFilePath)) {
                    ftpClient.makeDirectory(remoteFilePath);
                }
                uploadDirectory(ftpClient, file, remoteFilePath);
            }
        }
    }

    public void deleteFiles(FTPClient ftpClient, String remoteDir) {
        try {
            FTPFile[] files = ftpClient.listFiles(remoteDir);

            if (files == null || files.length == 0) {
                return;
            }

            for (FTPFile file : files)
                if (!file.isDirectory()) {
                    ftpClient.deleteFile(remoteDir + "/" + file.getName());
                }
        } catch (IOException ex) {
            System.out.println("FTP fájl törlés sikertelen: " + remoteDir);
            ex.printStackTrace();
        }
    }
}
