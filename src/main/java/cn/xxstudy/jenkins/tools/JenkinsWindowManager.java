/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.xxstudy.jenkins.tools;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import cn.xxstudy.jenkins.tools.logic.*;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import cn.xxstudy.jenkins.tools.view.JenkinsWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class JenkinsWindowManager implements Disposable {

    private final Project project;

    @NotNull
    public static Optional<JenkinsWindowManager> getInstance(Project project) {
        return Optional.ofNullable(project.getService(JenkinsWindowManager.class));
    }

    public JenkinsWindowManager(Project project) {
        this.project = project;
    }

    public void reloadConfiguration() {
        LoginService.getInstance(project).performAuthentication();
        BrowserPanel.getInstance(project).reloadConfiguration(JenkinsAppSettings.getSafeInstance(project));
    }

    @Override
    public void dispose() {
        RssAuthenticationActionHandler.getInstance(project).dispose();
        BrowserPanelAuthenticationHandler.getInstance(project).dispose();
        JenkinsWidget.getInstance(project).dispose();
        ExecutorService.getInstance(project).dispose();
        RequestManager.getInstance(project).dispose();
    }
}
