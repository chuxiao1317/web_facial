package com.chuxiao.web_facial.service;

import com.alibaba.fastjson.JSONObject;
import com.chuxiao.web_facial.util.BigCompare;
import com.chuxiao.web_facial.util.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class FacialService {

    private final Logger logger = LoggerFactory.getLogger(FacialService.class);

    //人脸入库-命令行
    private final static String CMD_PREFIX_ENROLL = "br -algorithm FaceRecognition -enrollAll -enroll ";

    //脸库追加-命令行
    private final static String CMD_PREFIX_CAT = "br -algorithm FaceRecognition -cat ";

    //人脸比对-命令行
    private final static String CMD_PREFIX_COMPARE = "br -algorithm FaceRecognition -compare ";

    //比对结果的记录文件
    private final static String COMPARE_RESULT_FILE_NAME = "match_scores.csv";

    //工作目录String
    private final static String WORK_DIR = "/usr/facial";

    //工作目录Path
    private final static Path WORK_PATH = Paths.get(WORK_DIR);

    //gal文件保存目录
    private final static String GAL_SAVE_LOCATION = "/usr/facial/gals";

    //总人脸库保存目录
    private final static String CAT_SAVE_LOCATION = "/usr/facial/cat";

    //总人脸库绝对路径
    private final static String ALL_FACES_FULL_PATH = "/usr/facial/cat/gals.gal";

    // 阈值
    private final static double THRESHOLD_VALUE = 2.50;

//    public FacialService() {
//        directoryInit(WORK_PATH);
//        directoryInit(Paths.get(GAL_SAVE_LOCATION));
//        directoryInit(Paths.get(CAT_SAVE_LOCATION));
//    }

    /**
     * 文件上传
     */
    private String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "文件为空，上传失败！";
        }
        try {
            String originFileName = file.getOriginalFilename().trim();
            Path fileFullPath = WORK_PATH.resolve(originFileName);
            if (Files.exists(fileFullPath)) {
                deleteFile(fileFullPath.toString());
            }
            Files.copy(file.getInputStream(), fileFullPath);
            logger.info("待比较文件名：" + originFileName);
            return originFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "上传出现异常";
        }
    }

    /**
     * 文件-验证-入库
     */
    public boolean uploadAndEnroll(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("待入库文件为空！");
            return false;
        }
        try {
            String originFileName = file.getOriginalFilename().trim();
            Path fileFullPath = WORK_PATH.resolve(originFileName);
            if (Files.exists(fileFullPath)) {
                deleteFile(fileFullPath.toString());
            }
            Files.copy(file.getInputStream(), fileFullPath);
            //返回入库结果
            boolean result = enrollAndStorage(originFileName);
            deleteFile(fileFullPath.toString());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 入库保存gal文件并将结果返回
     */
    private boolean enrollAndStorage(String fileName) {
        // 获得判断能否入库的命令
        String cmdline = getEnrollCommand(fileName);
        ArrayList<String> output = Shell.command(cmdline, GAL_SAVE_LOCATION);
        if (output == null || output.isEmpty()) {
            logger.error("命令行执行异常");
            return false;
        }
        String lastLine = output.get(output.size() - 1);
        logger.info("最后一行内容为：" + lastLine);
        String enrollResult = lastLine.substring(lastLine.lastIndexOf("=") + 1);
        logger.info("入库结果为：" + enrollResult);
        return "1".equals(enrollResult);
    }

    /**
     * 追加入库
     */
    public String cat() {
        String cmdline = CMD_PREFIX_CAT + GAL_SAVE_LOCATION + "/* " + ALL_FACES_FULL_PATH;
        ArrayList<String> strings = new Shell().command(cmdline, CAT_SAVE_LOCATION);
        if (strings == null || strings.isEmpty()) {
            logger.error("命令行执行异常");
            return "追加失败";
        }
        String count = strings.get(strings.size() - 1).split(" ")[1];
        if (Integer.parseInt(count) >= 1) {
            return count + "个人脸库追加成功";
        } else {
            return "追加失败";
        }
    }

    /**
     * 人脸识别
     */
    public String recognition(MultipartFile file) {
        String imgName = upload(file);
        Set<JSONObject> jsons = new HashSet<>();
        String imgAbsolutePath = getAbsolutePath(WORK_PATH, imgName);
        String cmdline = getCompareCommand(imgAbsolutePath, ALL_FACES_FULL_PATH);
        ArrayList<String> listStrings = Shell.command2(cmdline, WORK_DIR);
        if (listStrings == null || listStrings.isEmpty()) {
            return "您上传的文件名中带有非法符号，请重新处理！";
        }
        //logger.info("共比较了" + Integer.toString(listStrings.size() - 1) + "张人脸");
        for (int i = 0; i < listStrings.size(); i++) {
            //logger.info("当i=" + Integer.toString(i) + "时");
            String read = listStrings.get(i);
            //logger.info("此行为：" + read);
            String[] splited = read.split(",");
            String name = splited[0].substring(splited[0].lastIndexOf("/") + 1);
            String score = splited[1];
            if (isNumber(score)) {
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("score", score);
                jsons.add(json);
            }
        }

        double img_score = 0.00;
        String img_name = null;
        for (JSONObject json : jsons) {
            double tempScore = Double.valueOf(json.getString("score"));
            //compare()方法返回-1，将最大的值赋给左数
            if (new BigCompare<Double>().compare(img_score, tempScore) < 0) {
                img_score = tempScore;
                img_name = json.getString("name");
            }
        }
        // 将测试结果转换为百分数
//        String img_score_String;
//        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
//        if (img_score >= THRESHOLD_VALUE) {
//            img_score_String = "100.00%";
//        } else if (img_score <= 0) {
//            return "识别失败！";
//        } else {
//            img_score_String = decimalFormat.format(img_score / (THRESHOLD_VALUE + 0.5));
//        }

        // 比较结束后删除图片
        if (Files.exists(Paths.get(imgAbsolutePath))) {
            logger.info("比较完毕，删除文件" + imgAbsolutePath);
            deleteFile(imgAbsolutePath);
        } else {
            logger.error("文件" + imgAbsolutePath + "不存在");
        }

//        return "最高匹配：" + img_name + ":" + img_score_String;
        if (img_score >= THRESHOLD_VALUE) {
            return img_name;
        } else {
            return null;
        }
    }

    /**
     * 判断单张图片能否入库
     */
    public boolean canEnroll(String fileName) {
        // 获得判断能否入库的命令
        String cmdline = CMD_PREFIX_ENROLL + getAbsolutePath(WORK_PATH, fileName);
        ArrayList<String> output = Shell.command(cmdline, GAL_SAVE_LOCATION);
        if (output == null || output.isEmpty()) {
            logger.error("命令行执行异常");
            return false;
        }
        String lastLine = output.get(output.size() - 1);
        logger.info("最后一行内容为：" + lastLine);
        String enrollResult = lastLine.substring(lastLine.lastIndexOf("=") + 1);
        logger.info("入库结果为：" + enrollResult);
        return "1".equals(enrollResult);
    }

    /**
     * 获取入库命令
     */
    private String getEnrollCommand(String fileName) {
        String fileAbsolutePath = getAbsolutePath(WORK_PATH, fileName);
        logger.info("待入库文件名：" + fileName);
        String strFileName = fileName.substring(0, fileName.indexOf("."));
        return CMD_PREFIX_ENROLL + fileAbsolutePath + " " + strFileName + ".gal";
    }

    /**
     * 获取对比命令
     */
    private String getCompareCommand(String imgAbsolutePath, String galPath) {
        return CMD_PREFIX_COMPARE + imgAbsolutePath + " " + galPath + " " + COMPARE_RESULT_FILE_NAME;
    }

    /**
     * 根据文件名和指定路径获取绝对路径名
     */
    private String getAbsolutePath(Path path, final String filename) {
        return path.resolve(filename).toAbsolutePath().toString();
    }

    // 删除文件
    private void deleteFile(String fileAbsolutePath) {
        boolean result = new File(fileAbsolutePath).delete();
        if (result) {
            logger.info("成功删除文件：" + fileAbsolutePath);
        } else {
            logger.error("无法删除文件：" + fileAbsolutePath);
        }
    }

//    /**
//     * 文件夹初始化
//     */
//    private Path directoryInit(Path path) {
//        try {
//            if (!Files.exists(path)) {
//                Files.createDirectory(path);
//            }
//            return path;
//        } catch (IOException e) {
//            throw new StorageException("Could not initialize storage", e);
//        }
//    }

    /**
     * 判断字符串是否为数字
     **/
    private boolean isNumber(final String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
