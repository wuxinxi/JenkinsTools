package cn.xxstudy.jenkins.tools.view.parameter;

import com.intellij.openapi.vfs.VirtualFile;
import cn.xxstudy.jenkins.tools.model.JobParameter;
import cn.xxstudy.jenkins.tools.model.JobParameterType;
import cn.xxstudy.jenkins.tools.model.ProjectJob;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderer;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatchParameterRenderer implements JobParameterRenderer {

    static final JobParameterType TYPE = new JobParameterType("PatchParameterDefinition",
            "org.jenkinsci.plugins.patch.PatchParameterDefinition");

    @NotNull
    @Override
    public JobParameterComponent<VirtualFile> render(@NotNull JobParameter jobParameter, @Nullable ProjectJob projectJob) {
        return JobParameterRenderers.createFileUpload(jobParameter, jobParameter.getDefaultValue());
    }

    @Override
    public boolean isForJobParameter(@NotNull JobParameter jobParameter) {
        return TYPE.equals(jobParameter.getJobParameterType());
    }
}
