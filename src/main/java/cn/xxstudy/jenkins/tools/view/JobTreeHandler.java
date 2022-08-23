package cn.xxstudy.jenkins.tools.view;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import lombok.Value;
import cn.xxstudy.jenkins.tools.JenkinsTree;
import cn.xxstudy.jenkins.tools.model.Job;
import cn.xxstudy.jenkins.tools.model.JobType;
import cn.xxstudy.jenkins.tools.view.action.LoadBuildsAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import java.util.Optional;

@Value
public class JobTreeHandler implements TreeWillExpandListener {

    private final Project project;

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        getJobForNode(event).filter(job -> job.getLastBuilds().isEmpty())
                .filter(job -> job.getJobType() == JobType.JOB)
                .ifPresent(this::expandNode);
    }

    private void expandNode(@NotNull Job jobNode) {
        if (jobNode.getJobType() == JobType.JOB) {
            final AnAction action = ActionManager.getInstance().getAction(LoadBuildsAction.ACTION_ID);
            if (action instanceof LoadBuildsAction) {
                final LoadBuildsAction loadBuildsAction = (LoadBuildsAction) action;
                Optional.of(jobNode).filter(job -> job.getLastBuilds().isEmpty())
                        .ifPresent(job -> loadBuildsAction.loadBuilds(project,  job));
            }
        }
    }

    @NotNull
    private Optional<Job> getJobForNode(TreeExpansionEvent event) {
        return JenkinsTree.getJob(event.getPath());
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        // nothing to do
    }
}
