package org.pakesz.upload.angular;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AngularProcessBuilder {

    public void clearBefore(String directory) {
        File[] files = Optional.of(new File(directory))
                .map(File::listFiles)
                .orElse(null);

        if (files == null) {
            return;
        }

        for (File file : files)
            if (!file.isDirectory())
                file.delete();
    }


    public void runBuild(String angularProjectPath) throws IOException, InterruptedException {
        System.out.println("Angular build futtatása...");
        System.out.println(angularProjectPath);
        ProcessBuilder processBuilder = new ProcessBuilder("C:\\Program Files\\nodejs\\npm.cmd", "run", "build");
        processBuilder.directory(new File(angularProjectPath));
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        process.waitFor();

        if (process.exitValue() != 0) {
            System.err.println("Hiba történt az Angular build során!");
            return;
        }
        System.out.println("Build sikeresen befejeződött!");
    }
}
