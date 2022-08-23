package cn.xxstudy.jenkins.tools.logic;

import com.intellij.openapi.progress.PerformInBackgroundOption;

public class JenkinsBackgroundLoadingOption implements PerformInBackgroundOption {

    public static final JenkinsBackgroundLoadingOption INSTANCE = new JenkinsBackgroundLoadingOption();

    public boolean shouldStartInBackground() {
        return true;
    }

}
