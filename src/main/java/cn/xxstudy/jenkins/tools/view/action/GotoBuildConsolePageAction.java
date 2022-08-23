package cn.xxstudy.jenkins.tools.view.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import org.jetbrains.annotations.NotNull;

public class GotoBuildConsolePageAction extends AbstractGotoWebPageAction {

    public GotoBuildConsolePageAction(BrowserPanel browserPanel) {
        super("Go to the build console page", "Open the build console page in a web browser", browserPanel);
    }


    @NotNull
    @Override
    protected String getUrl() {
        return browserPanel.getSelectedBuild().getUrl() + "/console";
    }


    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(browserPanel.getSelectedBuild() != null);
    }
}
