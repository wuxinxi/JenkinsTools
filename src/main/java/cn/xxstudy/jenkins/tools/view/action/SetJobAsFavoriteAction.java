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

package cn.xxstudy.jenkins.tools.view.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import cn.xxstudy.jenkins.tools.model.Job;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import cn.xxstudy.jenkins.tools.view.JenkinsTreeRenderer;

import java.util.List;

public class SetJobAsFavoriteAction extends AnAction implements DumbAware {

    private BrowserPanel browserPanel;

    public SetJobAsFavoriteAction(BrowserPanel browserPanel) {
        super("Set as Favorite", "Set the selected job as favorite", JenkinsTreeRenderer.FAVORITE_ICON);
        this.browserPanel = browserPanel;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        List<Job> selectedJobs = browserPanel.getAllSelectedJobs();
        browserPanel.setAsFavorite(selectedJobs);
    }

    @Override
    public void update(AnActionEvent event) {
        Job selectedJob = browserPanel.getSelectedJob();
        event.getPresentation().setVisible(selectedJob != null && !browserPanel.isAFavoriteJob(selectedJob));
    }
}
