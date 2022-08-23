package cn.xxstudy.jenkins.tools.view.parameter;

import cn.xxstudy.jenkins.tools.model.JobParameter;
import cn.xxstudy.jenkins.tools.model.JobParameterType;
import cn.xxstudy.jenkins.tools.model.ProjectJob;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderer;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderers;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DateParameterRenderer implements JobParameterRenderer {

    @NonNls
    private static final String TYPE_CLASS = "me.leejay.jenkins.dateparameter.DateParameterDefinition";

    static final JobParameterType DATE_PARAMETER = new JobParameterType("DateParameterDefinition", TYPE_CLASS);

    @NotNull
    @Override
    public JobParameterComponent<String> render(@NotNull JobParameter jobParameter, @Nullable ProjectJob projectJob) {
        return JobParameterRenderers.createTextField(jobParameter, jobParameter.getDefaultValue());
    }

    @Override
    public boolean isForJobParameter(@NotNull JobParameter jobParameter) {
        return DATE_PARAMETER.equals(jobParameter.getJobParameterType());
    }
}
