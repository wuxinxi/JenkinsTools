package cn.xxstudy.jenkins.tools.view;

import cn.xxstudy.jenkins.tools.model.BuildStatusEnum;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@FunctionalInterface
public interface BuildStatusRenderer {

    @NotNull
    Icon renderBuildStatus(@NotNull BuildStatusEnum buildStatus);
}
