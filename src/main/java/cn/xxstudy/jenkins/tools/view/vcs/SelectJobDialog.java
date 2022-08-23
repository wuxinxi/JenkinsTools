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

package cn.xxstudy.jenkins.tools.view.vcs;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diff.impl.patch.FilePatch;
import com.intellij.openapi.diff.impl.patch.IdeaTextPatchBuilder;
import com.intellij.openapi.diff.impl.patch.UnifiedDiffWriter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeList;
import com.intellij.openapi.vcs.changes.patch.PatchWriter;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBList;
import cn.xxstudy.jenkins.tools.logic.RunBuildWithPatch;
import cn.xxstudy.jenkins.tools.model.Job;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class SelectJobDialog extends JDialog {

    private static final Logger LOG = Logger.getInstance(SelectJobDialog.class.getName());
    private static String FILENAME_PREFIX = "jenkins";
    private static String FILENAME_SUFFIX = ".diff";
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> jobsList;
    private JBList<String> changedFilesList;
    private JScrollPane changedFilesPane;

    private Project project;

    @NotNull
    private final Collection<ChangeList> changeLists;

    public SelectJobDialog(Collection<ChangeList> changeLists, @NotNull List<Job> jobs, Project project) {
        this.project = project;
        this.changeLists = changeLists;

        fillJobList(jobs);
        fillChangedFilesList();

        setContentPane(contentPane);
        setModal(true);

        setTitle("Create Patch and build on Jenkins");
        setResizable(false);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(event -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        SelectJobDialog dialog = new SelectJobDialog(Collections.emptyList(), Collections.emptyList(), null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void fillJobList(@NotNull List<Job> jobs) {
        final DefaultComboBoxModel<String> jobListModel = new DefaultComboBoxModel<>();
        jobs.stream().filter(Job::hasParameters)
                .filter(job -> job.hasParameter(RunBuildWithPatch.PARAMETER_NAME))
                .map(Job::getNameToRenderSingleJob)
                .forEach(jobListModel::addElement);
        jobsList.setModel(jobListModel);
    }

    private void fillChangedFilesList() {
        final DefaultListModel<String> model = new DefaultListModel<>();

        if (hasChangelist()) {
            StringBuilder builder = new StringBuilder();

            int count = 1;
            for (ChangeList changeList : changeLists) {
                builder.append(changeList.getName());
                if (count < changeLists.size()) {
                    builder.append(", ");
                }
                if (!changeList.getChanges().isEmpty()) {
                    for (Change change : changeList.getChanges()) {
                        VirtualFile virtualFile = change.getVirtualFile();
                        if (null != virtualFile) {
                            model.addElement(virtualFile.getPath());
                        }
                    }
                }
                count++;
            }
            changedFilesPane.setBorder(IdeBorderFactory.createTitledBorder(String.format("Changelists: %s",
                    builder.toString()), true));
        }

        changedFilesList.setModel(model);
    }

    private boolean hasChangelist() {
        return !changeLists.isEmpty();
    }

    @NotNull
    private File createPatchFile() throws IOException, VcsException {
        final File patchFile = FileUtil.createTempFile(FILENAME_PREFIX, FILENAME_SUFFIX);
        try (FileWriter writer = new FileWriter(patchFile)) {
            final ArrayList<Change> changes = new ArrayList<>();
            for (ChangeList changeList : changeLists) {
                changes.addAll(changeList.getChanges());
            }
            final Path base = PatchWriter.calculateBaseDirForWritingPatch(project, changes);
            final List<FilePatch> patches = IdeaTextPatchBuilder.buildPatch(project, changes, base, false);
            UnifiedDiffWriter.write(project, patches, writer, CodeStyle.getProjectOrDefaultSettings(project).getLineSeparator(), null);
        }
        return patchFile;
    }

    private void watchJob(BrowserPanel browserPanel, Job job) {
        for (ChangeList list : changeLists) {
            browserPanel.addToWatch(list.getName(), job);
        }
    }

    private void onOK() {
        BrowserPanel browserPanel = BrowserPanel.getInstance(project);
        Optional<File> patchFile = Optional.empty();
        try {
            patchFile = Optional.of(createPatchFile());
            final Optional<VirtualFile> virtualFile = patchFile.map(LocalFileSystem.getInstance()::refreshAndFindFileByIoFile);
            String selectedJobName = (String) jobsList.getSelectedItem();
            if (selectedJobName != null && !selectedJobName.isEmpty() && virtualFile.isPresent()) {
                final Job selectedJob = browserPanel.getJob(selectedJobName)
                        .filter(Job::hasParameters)
                        .filter(job -> job.hasParameter(RunBuildWithPatch.PARAMETER_NAME))
                        .orElse(null);
                if (selectedJob != null) {
                    RunBuildWithPatch.getInstance(project).runBuild(project, selectedJob, virtualFile.get());
                    watchJob(browserPanel, selectedJob);
                }
            }
        } catch (Exception e) {
            final String message = String.format("Build from patch cannot be run: %1$s", e.getMessage());
            LOG.info(message, e);
            browserPanel.notifyErrorJenkinsToolWindow(message);
        } finally {
            patchFile.ifPresent(FileUtil::delete);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
