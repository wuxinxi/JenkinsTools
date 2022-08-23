package cn.xxstudy.jenkins.tools.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import cn.xxstudy.jenkins.tools.JenkinsSettings;
import cn.xxstudy.jenkins.tools.model.Job;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class JobUtil {

    @NotNull
    public JenkinsSettings.FavoriteJob createFavoriteJob(@NotNull Job job) {
        return new JenkinsSettings.FavoriteJob(createNameForFavorite(job), job.getUrl());
    }

    public boolean isFavoriteJob(@NotNull Job job, @NotNull JenkinsSettings.FavoriteJob favoriteJob) {
        return isFavoriteJobName(createNameForFavorite(job), favoriteJob) || isFavoriteJobUrl(job.getUrl(), favoriteJob);
    }

    @NotNull
    String createNameForFavorite(@NotNull Job job) {
        return job.getFullName();
    }

    private boolean isFavoriteJobName(@NotNull String jobName, @NotNull JenkinsSettings.FavoriteJob favoriteJob) {
        return StringUtils.equals(jobName, favoriteJob.getName());
    }

    private boolean isFavoriteJobUrl(@NotNull String url, @NotNull JenkinsSettings.FavoriteJob favoriteJob) {
        return StringUtils.equals(url, favoriteJob.getUrl());
    }
}
