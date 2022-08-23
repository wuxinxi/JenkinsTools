package cn.xxstudy.jenkins.tools.view;

public interface JenkinsTreeNodeVisitor {

    void visit(JenkinsTreeNode.RootNode jenkinsServer);

    void visit(JenkinsTreeNode.BuildNode build);

    void visit(JenkinsTreeNode.JobNode job);

    void visit(JenkinsTreeNode.BuildParameterNode buildParameterNode);
}
