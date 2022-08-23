package cn.xxstudy.jenkins.tools.view.parameter;

import cn.xxstudy.jenkins.tools.model.JobParameter;
import cn.xxstudy.jenkins.tools.model.JobParameterType;
import cn.xxstudy.jenkins.tools.model.ProjectJob;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderer;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderers;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ExtendedChoiceParameterRenderer implements JobParameterRenderer {

    @NonNls
    private static final String TYPE_CLASS = "com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition";

    static final JobParameterType PT_SINGLE_SELECT = new JobParameterType("PT_SINGLE_SELECT", TYPE_CLASS);

    static final JobParameterType PT_MULTI_SELECT = new JobParameterType("PT_MULTI_SELECT", TYPE_CLASS);

    static final JobParameterType PT_CHECKBOX = new JobParameterType("PT_CHECKBOX", TYPE_CLASS);

    static final JobParameterType PT_RADIO = new JobParameterType("PT_RADIO", TYPE_CLASS);

    static final JobParameterType PT_TEXTBOX = new JobParameterType("PT_TEXTBOX", TYPE_CLASS);

    static final JobParameterType PT_HIDDEN = new JobParameterType("PT_HIDDEN", TYPE_CLASS);

    private final Map<JobParameterType, BiFunction<JobParameter, String, JobParameterComponent<String>>> converter =
            new HashMap<>();

    public ExtendedChoiceParameterRenderer() {
        converter.put(PT_SINGLE_SELECT, JobParameterRenderers::createComboBoxIfChoicesExists);
        converter.put(PT_MULTI_SELECT, JobParameterRenderers::createComboBoxIfChoicesExists);
        converter.put(PT_CHECKBOX, JobParameterRenderers::createCheckBoxChoices);
        converter.put(PT_RADIO, JobParameterRenderers::createErrorLabel);
        converter.put(PT_TEXTBOX, JobParameterRenderers::createTextField);
        converter.put(PT_HIDDEN, (jobParameter, name) -> new JobParameterComponent<>(jobParameter, new JLabel(), false));
    }

    @NotNull
    @Override
    public JobParameterComponent<String> render(@NotNull JobParameter jobParameter, @Nullable ProjectJob projectJob) {
        return converter.getOrDefault(jobParameter.getJobParameterType(), JobParameterRenderers::createErrorLabel)
                .apply(jobParameter, jobParameter.getDefaultValue());
    }

    @Override
    public boolean isForJobParameter(@NotNull JobParameter jobParameter) {
        return converter.containsKey(jobParameter.getJobParameterType());
    }
}
