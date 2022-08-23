package cn.xxstudy.jenkins.tools.view.parameter;

import cn.xxstudy.jenkins.tools.model.BuildInJobParameter;
import cn.xxstudy.jenkins.tools.model.JobParameter;
import cn.xxstudy.jenkins.tools.model.JobParameterType;
import cn.xxstudy.jenkins.tools.model.ProjectJob;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderer;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class BuiltInJobParameterRenderer implements JobParameterRenderer {

    private final Map<JobParameterType, BiFunction<JobParameter, String, JobParameterComponent<?>>> converter = new HashMap<>();

    public BuiltInJobParameterRenderer() {
        converter.put(BuildInJobParameter.ChoiceParameterDefinition, JobParameterRenderers::createComboBox);
        converter.put(BuildInJobParameter.BooleanParameterDefinition, JobParameterRenderers::createCheckBox);
        converter.put(BuildInJobParameter.StringParameterDefinition, JobParameterRenderers::createTextField);
        converter.put(BuildInJobParameter.PasswordParameterDefinition, JobParameterRenderers::createPasswordField);
        converter.put(BuildInJobParameter.TextParameterDefinition, JobParameterRenderers::createTextArea);
        converter.put(BuildInJobParameter.FileParameterDefinition, JobParameterRenderers::createFileUpload);
    }

    @NotNull
    @Override
    public JobParameterComponent<?> render(@NotNull JobParameter jobParameter, @Nullable ProjectJob projectJob) {
        final JobParameterType jobParameterType = jobParameter.getJobParameterType();
        final String defaultValue = jobParameter.getDefaultValue();
        return converter.getOrDefault(jobParameterType, JobParameterRenderers::createErrorLabel).apply(jobParameter, defaultValue);
    }

    @Override
    public boolean isForJobParameter(@NotNull JobParameter jobParameter) {
        return converter.containsKey(jobParameter.getJobParameterType());
    }

    @Nonnull
    @Override
    public Optional<JLabel> createLabel(@NotNull JobParameter jobParameter) {
        final JobParameterType jobParameterType = jobParameter.getJobParameterType();
        final Optional<JLabel> label = JobParameterRenderer.super.createLabel(jobParameter);
        if (BuildInJobParameter.TextParameterDefinition.equals(jobParameterType)) {
            label.ifPresent(textAreaLabel -> textAreaLabel.setVerticalAlignment(SwingConstants.TOP));
        }
        return label;
    }
}
