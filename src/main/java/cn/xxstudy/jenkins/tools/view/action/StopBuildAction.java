package cn.xxstudy.jenkins.tools.view.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import lombok.Value;
import cn.xxstudy.jenkins.tools.logic.JenkinsBackgroundTask;
import cn.xxstudy.jenkins.tools.logic.JenkinsBackgroundTaskFactory;
import cn.xxstudy.jenkins.tools.logic.RequestManagerInterface;
import cn.xxstudy.jenkins.tools.model.Build;
import cn.xxstudy.jenkins.tools.model.BuildStatusEnum;
import cn.xxstudy.jenkins.tools.model.Job;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Optional;

import static cn.xxstudy.jenkins.tools.view.BrowserPanel.POPUP_PLACE;

public class StopBuildAction extends AnAction implements DumbAware {

    private static final Icon STOP_ICON = AllIcons.Actions.Suspend;

    private final BrowserPanel browserPanel;

    public StopBuildAction(BrowserPanel browserPanel) {
        super("Stop on Jenkins", "Stop a build on Jenkins Server", STOP_ICON);
        this.browserPanel = browserPanel;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        ActionUtil.getProject(event).ifPresent(this::actionPerformed);
    }

    private void actionPerformed(Project project) {
        final LastSelection lastSelection = calculateLastSelection();
        Optional.ofNullable(lastSelection.getBuild()).ifPresent(build -> stopBuild(project, build,
                lastSelection.getJob()));
    }

    private void stopBuild(Project project, Build build, @Nullable Job job) {
        JenkinsBackgroundTaskFactory.getInstance(project).createBackgroundTask("Stopping build",
                new JenkinsBackgroundTask.JenkinsTask() {
                    @Override
                    public void run(@NotNull RequestManagerInterface requestManager) {
                        requestManager.stopBuild(build);
                    }

                    @Override
                    public void onSuccess() {
                        JenkinsBackgroundTask.JenkinsTask.super.onSuccess();
                        if (job == null) {
                            browserPanel.refreshCurrentView();
                        } else {
                            browserPanel.loadJob(job);
                        }
                    }
                }).queue();
    }

    @Override
    public void update(AnActionEvent event) {
        update(event, calculateLastSelection());
    }

    private void update(AnActionEvent event, LastSelection lastSelection) {
        final Optional<Job> job = Optional.ofNullable(lastSelection.getJob());
        final boolean isInQueue = job.map(Job::isInQueue).orElse(Boolean.FALSE);
        final boolean isBuilding = Optional.ofNullable(lastSelection.getBuild())
                .map(Build::isBuilding).orElse(Boolean.FALSE);
        final BuildStatusEnum lastBuildStatus = Optional.ofNullable(lastSelection.getBuild())
                .map(Build::getStatus)
                .orElse(BuildStatusEnum.NULL);
        final boolean isStoppable = lastBuildStatus == BuildStatusEnum.RUNNING || isBuilding || isInQueue;
        if (event.getPlace().equals(POPUP_PLACE)) {
            event.getPresentation().setVisible(isStoppable);
        } else {
            event.getPresentation().setEnabled(isStoppable);
        }
    }

    @Nonnull
    private LastSelection calculateLastSelection() {
        final Optional<Job> job = Optional.ofNullable(browserPanel).map(BrowserPanel::getSelectedJob);
        final Optional<Build> build = Optional.ofNullable(browserPanel).map(BrowserPanel::getSelectedBuild)
                .or(() -> job.map(Job::getLastBuild));
        // TODO find job for selected Build
        return new LastSelection(job.orElse(null), build.orElse(null));
    }

    @Value
    private static class LastSelection {
        @Nullable
        private Job job;
        @Nullable
        private Build build;
    }
}
