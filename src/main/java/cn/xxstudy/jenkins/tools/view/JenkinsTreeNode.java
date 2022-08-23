package cn.xxstudy.jenkins.tools.view;

import com.intellij.ide.util.treeView.NodeDescriptorProvidingKey;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import lombok.Data;
import lombok.Value;
import cn.xxstudy.jenkins.tools.model.Build;
import cn.xxstudy.jenkins.tools.model.BuildParameter;
import cn.xxstudy.jenkins.tools.model.Jenkins;
import cn.xxstudy.jenkins.tools.model.Job;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JenkinsTreeNode extends NodeDescriptorProvidingKey, NavigationItem {

    @NotNull
    String getUrl();

    @NotNull
    @Override
    default NavigationItem getKey() {
        return this;
    }

    @Nullable
    @Override
    default String getName() {
        return getUrl();
    }

    @Nullable
    @Override
    default ItemPresentation getPresentation() {
        return null;
    }

    @Override
    default void navigate(boolean requestFocus) {
        // do nothing
    }

    @Override
    default boolean canNavigate() {
        return false;
    }

    @Override
    default boolean canNavigateToSource() {
        return false;
    }

    void render(JenkinsTreeNodeVisitor treeNodeRenderer);

    @Data
    class BuildParameterNode implements JenkinsTreeNode {
        @NotNull
        private final BuildParameter buildParameter;

        @NotNull
        @Override
        public String getUrl() {
            return buildParameter.getBuildUrl();
        }

        @NotNull
        @Override
        public String getName() {
            return buildParameter.getName();
        }

        @Override
        public void render(JenkinsTreeNodeVisitor treeNodeRenderer) {
            treeNodeRenderer.visit(this);
        }

        public boolean hasValue() {
            return buildParameter.getValue() != null;
        }
    }

    @Value
    class BuildNode implements JenkinsTreeNode {

        private final Build build;

        @NotNull
        @Override
        public String getUrl() {
            return build.getUrl();
        }

        @Override
        public void render(JenkinsTreeNodeVisitor treeNodeRenderer) {
            treeNodeRenderer.visit(this);
        }
    }

    @Value
    class JobNode implements JenkinsTreeNode {

        private final Job job;

        @NotNull
        @Override
        public String getUrl() {
            return job.getUrl();
        }

        @Override
        public void render(JenkinsTreeNodeVisitor treeNodeRenderer) {
            treeNodeRenderer.visit(this);
        }
    }

    @Value
    class RootNode implements JenkinsTreeNode {

        private final Jenkins jenkins;

        @NotNull
        @Override
        public String getUrl() {
            return jenkins.getServerUrl();
        }

        @Override
        public void render(JenkinsTreeNodeVisitor treeNodeRenderer) {
            treeNodeRenderer.visit(this);
        }
    }
}
