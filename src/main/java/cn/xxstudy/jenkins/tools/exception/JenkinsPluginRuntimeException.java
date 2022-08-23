package cn.xxstudy.jenkins.tools.exception;

public class JenkinsPluginRuntimeException extends RuntimeException {

    public JenkinsPluginRuntimeException(String message) {
        super(message);
    }

    public JenkinsPluginRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
