package com.chuxiao.web_facial.controller;

import com.chuxiao.web_facial.service.FacialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FacialController {

    @Autowired
    private FacialService facialService;

    /**
     * 判断单张图片能否入库
     */
    @PostMapping(path = "/canEnroll")
    @ResponseBody
    public boolean canEnroll(@RequestParam("file") MultipartFile file) {
        return facialService.canEnroll(file.getOriginalFilename().trim());
    }

    /**
     * 单张入库
     */
    @PostMapping(path = "/sigleEnroll")
    @ResponseBody
    public boolean enroll(@RequestParam("file") MultipartFile file) {
        return facialService.uploadAndEnroll(file);
    }

    /**
     * 上传-验证-入库
     *
     * @param files 预留人脸文件
     * @return 入库结果反馈
     */
    @PostMapping(path = "/enroll")
    @ResponseBody
    public List<String> uploadAndEnrollImgs(@RequestParam("files") List<MultipartFile> files) {
        List<Boolean> enrollResults = new ArrayList<>();
        List<String> loseDescription = new ArrayList<>();
        if (files.isEmpty()) {
            loseDescription.add("文件上传失败！");
            return loseDescription;
        }
        for (MultipartFile file : files) {
//            if(file.getOriginalFilename().i)
            boolean result = facialService.uploadAndEnroll(file);
            enrollResults.add(result);
            if (!result) {
                loseDescription.add(file.getOriginalFilename() + "不能入库，请重新上传！");
            }
        }
        int filesNum = files.size();
        int resultNum = enrollResults.size();
        int loseNum = loseDescription.size();
        if (loseDescription.isEmpty() && filesNum == resultNum && filesNum > 0) {
            loseDescription.add(resultNum + "个文件已全部入库");
        } else {
            loseDescription.add("文件入库成功" + (filesNum - loseNum) + "个，失败" + loseNum + "个");
        }
        return loseDescription;
    }

    /**
     * 追加入库
     *
     * @return 反馈追加结果
     */
    @RequestMapping("/cat")
    @ResponseBody
    public String cat() {
        return facialService.cat();
    }

    /**
     * 识别
     * 其中count指的是规定时间内的访问次数，time指的就是规定时间，单位为毫秒。
     *
     * @param file 用来比较的图片
     * @return json描述识别结果
     */
    @PostMapping(path = "/recognition") // Map ONLY GET Requests
    @ResponseBody
    public String recognition(@RequestParam("file") MultipartFile file) {
        return facialService.recognition(file);
    }

}
