package cn.xxstudy.jenkins.tools.view.action.results;

import com.google.common.base.MoreObjects;
import com.intellij.execution.PsiLocation;
import com.intellij.execution.testframework.sm.runner.GeneralTestEventsProcessor;
import com.intellij.execution.testframework.sm.runner.events.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.offbytwo.jenkins.model.TestCase;
import com.offbytwo.jenkins.model.TestResult;
import com.offbytwo.jenkins.model.TestSuites;
import jetbrains.buildServer.messages.serviceMessages.TestFailed;
import cn.xxstudy.jenkins.tools.exception.JenkinsPluginRuntimeException;
import cn.xxstudy.jenkins.tools.logic.RequestManager;
import cn.xxstudy.jenkins.tools.model.Job;
import cn.xxstudy.jenkins.tools.view.BrowserPanel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class JobTestResultsHandler {
    private static final String CLASS_METHOD_SEPARATOR = ":::";
    private Job job;
    private final Project project;
    private final GeneralTestEventsProcessor testEventsProcessor;

    JobTestResultsHandler(Job job, Project project, GeneralTestEventsProcessor testEventsProcessor) {
        this.job = job;
        this.project = project;
        this.testEventsProcessor = testEventsProcessor;
        testEventsProcessor.setLocator((protocol, path, project1, scope) -> {
            String[] parts = path.split(CLASS_METHOD_SEPARATOR);
            String className = parts.length != 0 ? parts[0] : null;
            String method = parts.length > 1 ? parts[1] : null;

            PsiClass clazz = find(className, project1);
            PsiElement element = clazz == null || method == null
                    ? clazz
                    : Arrays.stream(clazz.getMethods())
                    .filter(m -> method.equals(m.getName()))
                    .findFirst()
                    .map(PsiElement.class::cast)
                    .orElse(clazz);

            return clazz == null ? Collections.emptyList() : Collections.singletonList(new PsiLocation<>(element));
        });

    }

    void handle() {
        final BrowserPanel browserPanel = BrowserPanel.getInstance(project);
        try {
            List<TestResult> testResults = RequestManager.getInstance(project).loadTestResultsFor(job);
            testResults.forEach(this::handleTestResult);
        } catch (JenkinsPluginRuntimeException e) {
            browserPanel.notifyErrorJenkinsToolWindow(e.getMessage());
        }
        testEventsProcessor.onFinishTesting();
    }

    private void handleTestResult(TestResult testResult) {
        if (testResult.getSuites() != null) {
            testResult.getSuites().forEach(this::handleTestSuites);
        }
    }

    private void handleTestSuites(TestSuites testSuites) {
        testEventsProcessor.onSuiteStarted(new TestSuiteStartedEvent(testSuites.getName(), "file://" + testSuites.getName()));
        testSuites.getCases().forEach(this::handleTestCase);
        testEventsProcessor.onSuiteFinished(new TestSuiteFinishedEvent(testSuites.getName()));
    }

    private void handleTestCase(TestCase testCase) {

        testEventsProcessor.onTestStarted(new TestStartedEvent(testCase.getName(), "file://" + testCase.getClassName() + CLASS_METHOD_SEPARATOR + testCase.getName()));

        if (testCase.isSkipped()) {
            testEventsProcessor.onTestIgnored(new TestIgnoredEvent(testCase.getName(), MoreObjects.firstNonNull(testCase.getErrorDetails(), ""), testCase.getErrorStackTrace()));
        } else if (testCase.getErrorDetails() != null) {
            testEventsProcessor.onTestFailure(new TestFailedEvent(new MyTestFailed(testCase), true));
        }
        testEventsProcessor.onTestFinished(new TestFinishedEvent(testCase.getName(), (long) testCase.getDuration()));
    }

    private static PsiClass find(String fqClassname, Project project) {
        return JavaPsiFacade.getInstance(project).findClass(fqClassname, GlobalSearchScope.allScope(project));
    }

    private static class MyTestFailed extends TestFailed {
        private String stacktrace;

        MyTestFailed(TestCase c) {
            super(c.getName(), c.getErrorDetails());
            this.stacktrace = c.getErrorStackTrace();
        }

        @Override
        public String getStacktrace() {
            return stacktrace;
        }
    }
}
