package com.chuxiao.web_facial.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * Author: Created by qinyl.
 * Date:   2016/12/19.
 * Comments:
 */
public class Shell {
    private final static Logger logger = LoggerFactory.getLogger(Shell.class);

    /**
     * 返回输出流
     */
    public static ArrayList<String> command(final String cmdline, final String directory) {
        try {
            Process process =
                    new ProcessBuilder(new String[]{"bash", "-c", cmdline})
                            .redirectErrorStream(true)
                            .directory(new File(directory))
                            .start();

            ArrayList<String> output = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            // 当输入流当中的每一行不为空时，将该行命令行输出内容保存进output集合中
            while ((line = br.readLine()) != null) {
                logger.info("cmd result: " + line);
                output.add(line);
            }

            //There should really be a timeout here.
            if (0 != process.waitFor()) {
                logger.error("cmd timeout");
                return null;
            }

            return output;
        } catch (Exception e) {
            logger.error("cmd execute exception");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回结果文件中的输入流
     */
    public static ArrayList<String> command2(final String cmdline, final String directory) {
        try {
            Process process =
                    new ProcessBuilder(new String[]{"bash", "-c", cmdline})
                            .redirectErrorStream(true)
                            .directory(new File(directory))
                            .start();

            ArrayList<String> output = new ArrayList<>();

            if (0 == process.waitFor()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(new File("/usr/facial/match_scores.csv"))));
                String line;
                while ((line = br.readLine()) != null) {
                    logger.info("cmd result: " + line);
                    output.add(line);
                }
            } else {
                logger.error("cmd timeout");
                return null;
            }

            return output;
        } catch (Exception e) {
            logger.error("cmd execute exception");
            e.printStackTrace();
            return null;
        }
    }
}
