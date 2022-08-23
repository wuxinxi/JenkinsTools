package cn.xxstudy.jenkins.tools.logic;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExecutorService implements Disposable {

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public ExecutorService() {
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    public ScheduledThreadPoolExecutor getExecutor() {
        return scheduledThreadPoolExecutor;
    }

    public static ExecutorService getInstance(Project project) {
        return project.getService(ExecutorService.class);
    }

    public void safeTaskCancel(ScheduledFuture<?> futureTask) {
        if (futureTask == null) {
            return;
        }
        if (!futureTask.isDone() || !futureTask.isCancelled()) {
            futureTask.cancel(false);
        }
    }

    @Override
    public void dispose() {
        getExecutor().shutdown();
    }
}
