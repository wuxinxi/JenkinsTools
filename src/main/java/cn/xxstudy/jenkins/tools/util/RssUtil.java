/*
 * Copyright (c) 2013 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.xxstudy.jenkins.tools.util;

import com.intellij.openapi.diagnostic.Logger;
import lombok.experimental.UtilityClass;
import cn.xxstudy.jenkins.tools.logic.BuildStatusVisitor;
import cn.xxstudy.jenkins.tools.logic.RssBuildStatusVisitor;
import cn.xxstudy.jenkins.tools.model.BuildStatusEnum;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class RssUtil {

    private static final Pattern JOBNAME_FROM_TITLE = Pattern.compile("^(.*) (?>\\S+) (?>\\(.*\\))$");
    private static final int JOB_NAME_GROUP = 1;
    private static final Pattern BUILDNUMBER_PATTERN = Pattern.compile(".*/(\\d+)/?(?>\\??.*)");
    private static final int BUILDNUMBER_GROUP = 1;

    private static final Pattern SUCCESS_MATCHER = Pattern.compile("normal|stable");

    private static final Pattern FAILED_MATCHER = Pattern.compile("failing|broken");

    private static final Pattern UNSTABLE_MATCHER = Pattern.compile("unstable");

    private static final Pattern ABORTED_MATCHER = Pattern.compile("aborted");

    private static final Logger LOG = Logger.getInstance(DateUtil.class.getName());

    @NotNull
    public static BuildStatusEnum extractStatus(@NotNull String rssEntryTitle) {
        RssBuildStatusVisitor statusVisitor = new RssBuildStatusVisitor();
        visit(statusVisitor, rssEntryTitle);
        return statusVisitor.getStatus();
    }

    public static int extractBuildNumber(@NotNull String buildUrl) {
        try {
            final Matcher matcher = BUILDNUMBER_PATTERN.matcher(buildUrl);
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(BUILDNUMBER_GROUP));
            }
        } catch (NumberFormatException e) {
            LOG.debug(e);
        }
        return -1;
    }

    @NotNull
    public static String extractBuildJob(@NotNull String rssEntryTitle) {
        final Matcher matcher = JOBNAME_FROM_TITLE.matcher(rssEntryTitle);
        if (matcher.matches()) {
            return matcher.group(JOB_NAME_GROUP);
        }
        return rssEntryTitle;
    }


    private static void visit(BuildStatusVisitor statusVisitor, String rssEntryTitle) {
        if (matches(rssEntryTitle, SUCCESS_MATCHER)) {
            statusVisitor.visitSuccess();
            return;
        }
        if (matches(rssEntryTitle, FAILED_MATCHER)) {
            statusVisitor.visitFailed();
            return;
        }
        if (matches(rssEntryTitle, ABORTED_MATCHER)) {
            statusVisitor.visitAborted();
            return;
        }

        if (matches(rssEntryTitle, UNSTABLE_MATCHER)) {
            statusVisitor.visitUnstable();
            return;
        }

        statusVisitor.visitUnknown();
    }

    private static boolean matches(String rssEntryTitle, Pattern pattern) {
        Matcher matcher = pattern.matcher(rssEntryTitle);
        return matcher.find();
    }

}
