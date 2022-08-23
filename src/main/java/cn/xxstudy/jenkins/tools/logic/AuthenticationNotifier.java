package cn.xxstudy.jenkins.tools.logic;

import com.intellij.util.messages.Topic;
import cn.xxstudy.jenkins.tools.model.Jenkins;

public interface AuthenticationNotifier {
    Topic<AuthenticationNotifier> USER_LOGGED_IN = Topic.create("User Logged In", AuthenticationNotifier.class);

    void emptyConfiguration();

    void afterLogin(Jenkins jenkinsWorkspace);

    void loginCancelled();

    void loginFailed(Throwable ex);
}
