package cn.xxstudy.jenkins.tools.view.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import org.jetbrains.annotations.NotNull;

public class GotoAllureReportPageAction extends AbstractGotoWebPageAction {

    public GotoAllureReportPageAction(BrowserPanel browserPanel) {
        super("Go to the allure report page", "Open the allure report page in a web browser", browserPanel);
    }


    @NotNull
    @Override
    protected String getUrl() {
        return browserPanel.getSelectedBuild().getUrl() + "/allure";
    }


    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setVisible(browserPanel.getSelectedBuild() != null);
    }
}
