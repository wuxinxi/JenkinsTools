package cn.xxstudy.jenkins.tools.view.parameter;

import cn.xxstudy.jenkins.tools.model.JobParameter;
import cn.xxstudy.jenkins.tools.model.JobParameterType;
import cn.xxstudy.jenkins.tools.model.ProjectJob;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderer;
import cn.xxstudy.jenkins.tools.view.extension.JobParameterRenderers;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static cn.xxstudy.jenkins.tools.model.JobParameterType.createTypeForClassPrefix;

public class ActiveChoicesParameterRenderer implements JobParameterRenderer {

    @NonNls
    private static final String TYPE_CLASS_PREFIX = "org.biouno.unochoice.";

    static final JobParameterType CHOICE_PARAMETER = createTypeForClassPrefix("ChoiceParameter", TYPE_CLASS_PREFIX);

    static final JobParameterType CASCADE_CHOICE_PARAMETER = createTypeForClassPrefix("CascadeChoiceParameter", TYPE_CLASS_PREFIX);

    static final JobParameterType DYNAMIC_REFERENCE_PARAMETER = createTypeForClassPrefix("DynamicReferenceParameter", TYPE_CLASS_PREFIX);

    private final Map<JobParameterType, BiFunction<JobParameter, String, JobParameterComponent<String>>> converter =
            new HashMap<>();

    public ActiveChoicesParameterRenderer() {
        converter.put(CHOICE_PARAMETER, JobParameterRenderers::createComboBoxIfChoicesExists);
        converter.put(CASCADE_CHOICE_PARAMETER, JobParameterRenderers::createComboBoxIfChoicesExists);
        converter.put(DYNAMIC_REFERENCE_PARAMETER, JobParameterRenderers::createLabel);
    }

    @Override
    public @NotNull JobParameterComponent render(@NotNull JobParameter jobParameter, @Nullable ProjectJob projectJob) {
        return converter.getOrDefault(jobParameter.getJobParameterType(), JobParameterRenderers::createErrorLabel)
                .apply(jobParameter, jobParameter.getDefaultValue());
    }

    @Override
    public boolean isForJobParameter(@NotNull JobParameter jobParameter) {
        return converter.containsKey(jobParameter.getJobParameterType());
    }
}
