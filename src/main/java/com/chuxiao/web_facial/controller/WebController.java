package com.chuxiao.web_facial.controller;

import com.chuxiao.web_facial.entity.Organization;
import com.chuxiao.web_facial.entity.UsersClockingsOrganizations;
import com.chuxiao.web_facial.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/facial")
public class WebController {

    @Autowired
    private WebService webService;

    /**
     * @return 载入首页
     */
    @RequestMapping("")
    public String index() {
        return "index";
    }

    /**
     * @return 跳转入库页面
     */
    @RequestMapping("/enrollPage")
    public String enroll(Model model) {
        webService.enroll(model);
        return "enroll";
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ResponseBody
    public boolean userRegister(HttpServletRequest request) {
        return webService.userRegister(request);
    }

    /**
     * @return 注册成功页面
     */
    @RequestMapping("/enrollSuccess")
    public String enrollSuccess() {
        return "enrollSuccess";
    }

    /**
     * 登录页面
     */
    @RequestMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    /**
     * 用户登录
     */
    @PostMapping("/userLogin")
    @ResponseBody
    public boolean userLogin(HttpServletRequest request) {
        return webService.userLogin(request);
    }

    /**
     * 管理员登录
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public boolean adminLogin(HttpServletRequest request) {
        return webService.adminLogin(request);
    }

    /**
     * 用户主页
     */
    @RequestMapping("/user")
    public String userPage() {
        return "user";
    }

    /**
     * 管理员主页
     */
    @RequestMapping("/admin")
    public String adminPage(Model model) {
        webService.admin(model);
        return "admin";
    }

    /**
     * 退出主页
     */
    @RequestMapping("/logout")
    @ResponseBody
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return webService.logout(request, response);
    }

    /**
     * 分页查看所有班级
     */
    @RequestMapping("/getOrganizationsNoCondition")
    public ModelAndView getOrganizationsNoCondition(Model model, @RequestParam(defaultValue = "0") Integer page) {
        Page<Organization> organizations = webService.getOrganizationsNoCondition(page, 5);
        model.addAttribute("organizations", organizations);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("organizationsTable");
        return modelAndView;
    }

    /**
     * 分页查看users刷脸记录
     */
    @RequestMapping("/getUserClockingsNoCondition")
    public ModelAndView getUserClockingsNoCondition(Model model, @RequestParam(defaultValue = "0") Integer page) {
        List<UsersClockingsOrganizations> userClockings = webService.getUserClockingsNoCondition();
        System.out.println(userClockings);
        model.addAttribute("userClockings", userClockings);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userClockingsTable");
        return modelAndView;
    }

    /**
     * 分页查看“我”的刷脸记录
     */
    @GetMapping("/queryMyClockings")
    @ResponseBody
    public ModelAndView queryMyClockings(Model model, @RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        Page<Date> createDates = webService.queryMyClockings(page, 5, request);
        model.addAttribute("createDates", createDates);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("clockingsTable");
        return modelAndView;
    }

    /**
     * 添加班级
     */
    @PostMapping("/addOrganization")
    @ResponseBody
    public boolean addOrganization(@RequestParam String organizationName) {
        return webService.addOrganization(organizationName);
    }

    /**
     * 修改班级名称
     */
    @PostMapping("/updateOrganization")
    @ResponseBody
    public boolean updateOrganization(@RequestParam String newName, @RequestParam String previousName) {
        return webService.updateOrganization(newName, previousName);
    }

    /**
     * 删除班级
     */
    @PostMapping("/deleteOrganization")
    @ResponseBody
    public boolean deleteOrganization(@RequestParam String organizationName) {
        return webService.deleteOrganization(organizationName);
    }

    /**
     * 验证账号唯一性
     */
    @RequestMapping("/checkUsernameUniqueness")
    @ResponseBody
    public boolean checkUsernameUniqueness(@RequestParam("username") String username) {
        return webService.checkUsernameUniqueness(username);
    }

    /**
     * 判断单张图片能否入库
     */
    @PostMapping("/canEnroll")
    @ResponseBody
    public boolean canEnroll(String username, String data) {
        return webService.canEnroll(username, data);
    }

//    /**
//     * 入库
//     *
//     * @param data 待比较人脸图片的base64码
//     * @return 入库结果
//     */
//    @PostMapping("/enroll")
//    @ResponseBody
//    public boolean enroll(String username, String data) {
//        return webService.enroll(username, data);
//    }

    /**
     * @return 人脸识别页面
     */
    @RequestMapping("/recognitionPage")
    public String recognition() {
        return "recognition";
    }

    /**
     * 识别
     *
     * @param data 待比较人脸图片的base64码
     * @return 识别结果
     */
    @PostMapping("/recognition")
    @ResponseBody
    public String recognition(String data) {
        return webService.recognition(data);
    }
}
