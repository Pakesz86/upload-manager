package org.pakesz.upload.angular;


import lombok.Getter;

@Getter
public class AngularProcessConfig {

    String mainRootPath;
    String angularProjectPath;
    String angularCompiledPath;

    public static AngularProcessConfig builder() {
        return new AngularProcessConfig();
    }

    public AngularProcessConfig build() {
        return this;
    }


    public AngularProcessConfig mainRootPath(String path) {
        mainRootPath = path;
        return this;
    }

    public AngularProcessConfig angularProjectPath(String path) {
        angularProjectPath = mainRootPath + path;
        return this;
    }

    public AngularProcessConfig angularCompiledPath(String path) {
        angularCompiledPath = angularProjectPath + path;
        return this;
    }
}
