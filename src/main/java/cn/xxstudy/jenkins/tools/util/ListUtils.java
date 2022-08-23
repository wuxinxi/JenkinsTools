package cn.xxstudy.jenkins.tools.util;

import cn.xxstudy.jenkins.tools.JenkinsAppSettings;
import cn.xxstudy.jenkins.tools.model.Job;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @date: 2022/8/23 11:07
 * @author: Sensi
 * @remark:
 */
public class ListUtils {

    public static List<Job> filter(List<Job> jobList, JenkinsAppSettings appSettings) {
        final List<Job> filterJobList = new ArrayList<>();
        String filterProjects = appSettings.getFilterProjects();
        if (filterProjects != null && !filterProjects.isEmpty()) {
            String[] filters = filterProjects.split(",");
            Arrays.stream(filters).forEach(filter -> jobList.stream().filter(job -> filter.equalsIgnoreCase(job.getName())).forEach(filterJobList::add));
        }
        if (filterJobList.isEmpty()) {
            filterJobList.addAll(jobList);
        }
        return filterJobList;
    }
}
