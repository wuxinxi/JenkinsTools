package cn.xxstudy.jenkins.tools.logic;

import com.offbytwo.jenkins.helper.BuildConsoleStreamListener;
import com.offbytwo.jenkins.model.TestResult;
import cn.xxstudy.jenkins.tools.JenkinsAppSettings;
import cn.xxstudy.jenkins.tools.JenkinsSettings;
import cn.xxstudy.jenkins.tools.model.*;
import cn.xxstudy.jenkins.tools.security.JenkinsVersion;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface RequestManagerInterface {
    Jenkins loadJenkinsWorkspace(JenkinsAppSettings configuration);

    Map<String, Build> loadJenkinsRssLatestBuilds(JenkinsAppSettings configuration);

    void runBuild(Job job, JenkinsAppSettings configuration, Map<String, ?> parameters);

    void authenticate(JenkinsAppSettings jenkinsAppSettings, JenkinsSettings jenkinsSettings);

    String testAuthenticate(String serverUrl, String username, String password, String crumbData, JenkinsVersion version, int connectionTimoutInSeconds);

    List<Job> loadFavoriteJobs(List<JenkinsSettings.FavoriteJob> favoriteJobs);

    void stopBuild(Build build);

    @NotNull
    Job loadJob(Job job);

    @NotNull
    List<Job> loadJenkinsView(@NotNull View view);

    @NotNull
    Build loadBuild(Build build);

    List<Build> loadBuilds(Job job);

    String loadConsoleTextFor(Job job, BuildType buildType);

    void loadConsoleTextFor(Job job, BuildType buildType, BuildConsoleStreamListener buildConsoleStreamListener);

    List<TestResult> loadTestResultsFor(Job job);

    @NotNull
    List<Computer> loadComputer(JenkinsAppSettings settings);

    List<String> getGitParameterChoices(Job job, JobParameter jobParameter);
}
