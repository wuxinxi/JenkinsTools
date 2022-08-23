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

package cn.xxstudy.jenkins.tools.view.action.settings;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;

import javax.swing.*;

public class SortByStatusAction extends ToggleAction implements DumbAware {

    private static final Icon SORT_ICON = AllIcons.ObjectBrowser.SortByType;

    private final BrowserPanel browserPanel;

    private boolean sortedByStatus = false;

    public SortByStatusAction(BrowserPanel browserPanel) {
        super("Sort by Build Status", "Fail, Unstable, Success, ...", SORT_ICON);
        this.browserPanel = browserPanel;
    }

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        return sortedByStatus;
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean sorted) {
        sortedByStatus = sorted;
        browserPanel.setSortedByStatus(sorted);
    }

}
