package cn.xxstudy.jenkins.tools.logic;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;
import com.offbytwo.jenkins.model.BaseModel;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public final class JenkinsBackgroundTaskFactory {
    @NotNull
    private final Project project;

    JenkinsBackgroundTaskFactory(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    public static JenkinsBackgroundTaskFactory getInstance(@NotNull Project project) {
        final JenkinsBackgroundTaskFactory jenkinsBackgroundTaskFactory = project.getService(
                JenkinsBackgroundTaskFactory.class);
        return jenkinsBackgroundTaskFactory == null ? new JenkinsBackgroundTaskFactory(project) :
                jenkinsBackgroundTaskFactory;
    }

    @NotNull
    public JenkinsBackgroundTask createBackgroundTask(@NlsContexts.ProgressTitle @NotNull String title,
                                                      @NotNull JenkinsBackgroundTask.JenkinsTask jenkinsTask) {
        return createBackgroundTask(title, false, jenkinsTask);
    }

    @NotNull
    public JenkinsBackgroundTask createBackgroundTask(@NlsContexts.ProgressTitle @NotNull String title,
                                                      boolean canBeCancelled,
                                                      @NotNull JenkinsBackgroundTask.JenkinsTask jenkinsTask) {

//        JenkinsHttpConnection();
        return new JenkinsBackgroundTask(project, title, canBeCancelled, jenkinsTask, RequestManager.getInstance(project));
    }
}
