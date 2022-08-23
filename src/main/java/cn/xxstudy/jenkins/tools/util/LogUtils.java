package cn.xxstudy.jenkins.tools.util;

import java.io.File;
import java.io.FileWriter;

/**
 * @date: 2022/8/23 09:21
 * @author: Sensi
 * @remark:
 */
public class LogUtils {
    public static void writeLog(String content) {
        try {
            String logPath = "/Users/sensiwu/Desktop";
            File file = new File(logPath + "/log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(content);
            fileWriter.write("\n");
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
