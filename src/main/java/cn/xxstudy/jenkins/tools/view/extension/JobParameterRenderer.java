package cn.xxstudy.jenkins.tools.view.extension;

import com.intellij.openapi.extensions.ExtensionPointName;
import org.apache.commons.lang.StringUtils;
import cn.xxstudy.jenkins.tools.model.JobParameter;
import cn.xxstudy.jenkins.tools.model.ProjectJob;
import cn.xxstudy.jenkins.tools.view.parameter.JobParameterComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Optional;

public interface JobParameterRenderer {

    ExtensionPointName<JobParameterRenderer> EP_NAME = ExtensionPointName.create("cn.xxstudy.jenkins.tools.buildParameterRenderer");

    @NotNull
    static Optional<JobParameterRenderer> findRenderer(@NotNull JobParameter jobParameter) {
        return JobParameterRenderer.EP_NAME.extensions()
                .filter(jobParameterRenderer -> jobParameterRenderer.isForJobParameter(jobParameter))
                .findFirst();
    }

    /**
     * If non-empty Optional return then a label will be shown.
     */
    @Nonnull
    default Optional<JLabel> createLabel(@NotNull JobParameter jobParameter) {
        final String name = jobParameter.getName();
        final JLabel label;
        if (StringUtils.isEmpty(name)) {
            label = JobParameterRenderers.createErrorLabel(JobParameterRenderers.MISSING_NAME_LABEL);
        } else {
            label = new JLabel(name);
        }
        return Optional.of(label);
    }

    @SuppressWarnings({"rawtypes", "java:S3740"})
    @NotNull
    JobParameterComponent render(@NotNull JobParameter jobParameter, @Nullable ProjectJob projectJob);

    boolean isForJobParameter(@NotNull JobParameter jobParameter);

}
