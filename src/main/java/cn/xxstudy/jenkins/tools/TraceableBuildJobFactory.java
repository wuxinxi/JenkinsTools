package cn.xxstudy.jenkins.tools;

import cn.xxstudy.jenkins.tools.logic.RequestManagerInterface;
import cn.xxstudy.jenkins.tools.model.Job;

import java.text.MessageFormat;
import java.util.Map;

public class TraceableBuildJobFactory {
    private static final int RETRY_LIMIT = 10;

    private TraceableBuildJobFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static TraceableBuildJob newBuildJob(Job job, JenkinsAppSettings configuration, Map<String, ?> paramValueMap,
                                                RequestManagerInterface requestManager) {
        final int numBuildRetries = configuration.getNumBuildRetries();
        ensureRetryLimit(numBuildRetries);
        final Runnable runBuild = () -> requestManager.runBuild(job, configuration, paramValueMap);
        return new TraceableBuildJob(job, runBuild, numBuildRetries);
    }

    private static void ensureRetryLimit(int numBuildRetries) {
        if (numBuildRetries > RETRY_LIMIT) {
            throw new IllegalArgumentException(MessageFormat.format("can't retry more than {0} times", RETRY_LIMIT));
        }
    }
}
