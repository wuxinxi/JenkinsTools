package cn.xxstudy.jenkins.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import cn.xxstudy.jenkins.tools.model.Build;

public class JobTracker {
    private static JobTracker ourInstance = new JobTracker();

    public static JobTracker getInstance() {
        return ourInstance;
    }

    private ArrayList<TraceableBuildJob> buildJobs = new ArrayList<>();

    private JobTracker() {
    }

    public void registerJob(TraceableBuildJob buildJob) {
        if (!buildJobs.contains(buildJob)) {
            buildJobs.add(buildJob);
        } else { //remove existing job if a new one was submitted from GUI
            buildJobs.removeIf(buildJob::equals);
            buildJobs.add(buildJob);
        }
    }

    public void onNewFinishedBuilds(Map<String, Build> finishedBuilds) {
        notifyJobsAboutNewFinishedBuilds(finishedBuilds.values());
        removeDoneJobs();
    }

    private void removeDoneJobs() {
        buildJobs.removeIf(TraceableBuildJob::isDone);
    }

    private void notifyJobsAboutNewFinishedBuilds(Collection<Build> finishedBuilds) {
        buildJobs.forEach(traceableBuildJob -> finishedBuilds.forEach(traceableBuildJob::someBuildFinished));
    }
}


