package cn.xxstudy.jenkins.tools.view.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import org.jetbrains.annotations.NotNull;

public class GotoBuildPageAction extends AbstractGotoWebPageAction {

    public GotoBuildPageAction(BrowserPanel browserPanel) {
        super("Go to the build page", "Open the build page in a web browser", browserPanel);
    }


    @NotNull
    @Override
    protected String getUrl() {
        return browserPanel.getSelectedBuild().getUrl();
    }


    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(browserPanel.getSelectedBuild() != null);
    }
}
