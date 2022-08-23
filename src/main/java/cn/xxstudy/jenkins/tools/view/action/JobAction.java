package cn.xxstudy.jenkins.tools.view.action;

import cn.xxstudy.jenkins.tools.model.Job;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface JobAction {

    void execute(@NotNull Job job);
}
