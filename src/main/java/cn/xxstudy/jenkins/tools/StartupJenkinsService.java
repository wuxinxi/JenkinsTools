package cn.xxstudy.jenkins.tools;

import com.intellij.ide.plugins.CannotUnloadPluginException;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.ui.AppUIUtil;
import com.intellij.util.messages.MessageBusConnection;
import cn.xxstudy.jenkins.tools.logic.BrowserPanelAuthenticationHandler;
import cn.xxstudy.jenkins.tools.logic.LoginService;
import cn.xxstudy.jenkins.tools.logic.RssAuthenticationActionHandler;
import org.jetbrains.annotations.NotNull;

public class StartupJenkinsService implements StartupActivity.Background, DynamicPluginListener {
    private static final Logger LOG = Logger.getInstance(JenkinsTree.class);

    @Override
    public void runActivity(@NotNull Project project) {
        RssAuthenticationActionHandler.getInstance(project).subscribe();
        BrowserPanelAuthenticationHandler.getInstance(project).subscribe();
        final LoginService loginService = LoginService.getInstance(project);
        AppUIUtil.invokeLaterIfProjectAlive(project, loginService::performAuthentication);
        final MessageBusConnection busConnection = ApplicationManager.getApplication().getMessageBus().connect();
        busConnection.subscribe(DynamicPluginListener.TOPIC, this);
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        if (isJenkinsPlugin(pluginDescriptor)) {
            LOG.info("Unload Jenkins Control plugin");
        }
    }

    @Override
    public void checkUnloadPlugin(@NotNull IdeaPluginDescriptor pluginDescriptor) throws CannotUnloadPluginException {
        if (isJenkinsPlugin(pluginDescriptor)) {
            LOG.debug("check if Jenkins Control plugin could be unloaded");
        }
    }

    private static boolean isJenkinsPlugin(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        return pluginDescriptor.getPluginId() == PluginId.getId(Version.PLUGIN_ID);
    }
}
