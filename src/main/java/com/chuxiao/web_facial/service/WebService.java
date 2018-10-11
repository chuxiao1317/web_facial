package com.chuxiao.web_facial.service;

import com.chuxiao.web_facial.controller.FacialController;
import com.chuxiao.web_facial.dao.*;
import com.chuxiao.web_facial.entity.Clocking;
import com.chuxiao.web_facial.entity.Organization;
import com.chuxiao.web_facial.entity.User;
import com.chuxiao.web_facial.entity.UsersClockingsOrganizations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class WebService {

    private final Logger logger = LoggerFactory.getLogger(WebService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private ClockingDao clockingDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private UsersClockingsOrganizationsDao usersClockingsOrganizationsDao;

    @Autowired
    private FacialController facialController;

    /**
     * 载入enroll页面
     */
    public void enroll(Model model) {
        Set<String> organizations = organizationDao.getNames();
        model.addAttribute("organizations", organizations);
    }

    /**
     * 载入admin页面
     */
    public void admin(Model model) {
        List<Organization> organizations = organizationDao.findAll();
        model.addAttribute("organizations", organizations);
    }

    /**
     * 检查用户名的唯一性
     */
    public boolean checkUsernameUniqueness(String username) {
        if (username == null || username.length() <= 0) {
            return false;
        }
        List<String> usernames = userDao.getAllUsername();
        if (usernames == null || usernames.size() <= 0) {
            return true;
        }
        for (int i = 0; i < usernames.size(); i++) {
            String userName = usernames.get(i);
            if (userName.equals(username)) {
                return false;
            } else if (i == usernames.size() - 1) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * 入库
//     */
//    public List<String> enroll(List<MultipartFile> files) {
//        if (files.isEmpty()) {
//            List<String> list = new ArrayList<>();
//            list.add("文件上传失败！");
//            return list;
//        }
//        return facialController.uploadAndEnrollImgs(files);
//    }

    public boolean canEnroll(String username, String data) {
        if (username == null || username.length() <= 0) {
            logger.error("未获取到用户输入账号！");
            return false;
        }
        logger.info("获取用户输入账号：" + username);
        String imgFilePath = "/usr/facial/" + username + ".png";
        File file = generateImage(data, imgFilePath);
        MultipartFile multipartFile = generateMulitpartFile(file);
        if (multipartFile == null || multipartFile.isEmpty()) {
            return false;
        } else {
            logger.info("转换为multipartFile文件之后的文件名：" + multipartFile.getOriginalFilename());
            return facialController.canEnroll(multipartFile);
        }
    }

//    /**
//     * 入库
//     */
//    public boolean enroll(String username, String data) {
//        if (username == null || username.length() <= 0) {
//            logger.error("未获取到用户输入账号！");
//            return false;
//        }
//        logger.info("获取用户输入账号：" + username);
//        String imgFilePath = "/usr/facial/" + username + ".png";
//        File file = generateImage(data, imgFilePath);
//        MultipartFile multipartFile = generateMulitpartFile(file);
//        if (multipartFile != null) {
//            logger.info("转换为multipartFile文件之后的文件名：" + multipartFile.getOriginalFilename());
//            return facialController.enroll(multipartFile);
//        } else {
//            return false;
//        }
//    }

    /**
     * 用户注册
     */
    public boolean userRegister(HttpServletRequest request) {
        String username = request.getParameter("username");
        boolean usernameCheckResult = checkUsernameUniqueness(username);
        if (usernameCheckResult) {
            String password = request.getParameter("password");
            String nickname = request.getParameter("nickname");
            if (password == null || password.length() <= 0 || nickname == null || nickname.length() <= 0) {
                return false;
            }
            String sex = request.getParameter("sex");
            String organization = request.getParameter("organization");
            Organization tempOrganization = organizationDao.getOrganizationByName(organization);
            userDao.save(new User(username, password, nickname, sex, tempOrganization));
            String userName = userDao.getUsernameByUsername(username);
            if (userName != null && userName.equals(username)) {
                logger.info("注册信息成功保存，开始图片入库！");
                //注册成功，给过滤器传值
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
//                request.setAttribute("username", username);
                //开始入库
                String data = request.getParameter("data");
                String imgFilePath = "/usr/facial/" + username + ".png";
                File file = generateImage(data, imgFilePath);
                MultipartFile multipartFile = generateMulitpartFile(file);
                if (multipartFile == null || multipartFile.isEmpty()) {
                    return false;
                } else {
                    logger.info("转换为multipartFile文件之后的文件名：" + multipartFile.getOriginalFilename());
                    return facialController.enroll(multipartFile);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 用户登录
     */
    public boolean userLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sqlPassword = userDao.getPasswordByUsername(username);
        if (sqlPassword != null && sqlPassword.length() > 0 && password.equals(sqlPassword)) {
            logger.info("用户成功通过登录验证！");
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 管理员登录
     */
    public boolean adminLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sqlPassword = adminDao.getPasswordByUsername(username);
        if (sqlPassword != null && sqlPassword.length() > 0 && password.equals(sqlPassword)) {
            logger.info("管理员成功通过登录验证！");
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 退出管理员页面
     */
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        try {
            response.sendRedirect("/facial/loginPage");
            return true;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return true;
    }

    /**
     * 添加班级
     */
    public boolean addOrganization(String organizationName) {
        String isExistName = organizationDao.findNameByName(organizationName);
        if (isExistName != null && isExistName.length() > 0) {
            // 表中已有同名班级，不能添加
            return false;
        }
        organizationDao.save(new Organization(organizationName, new Date(), new Date()));
        String name = organizationDao.findNameByName(organizationName);
        return name != null && name.length() > 0;
    }

    /**
     * 修改班级名称
     */
    public boolean updateOrganization(String newName, String previousName) {
        organizationDao.updateOrganizationByName(newName, new Date(), previousName);
        String previous_name = organizationDao.findNameByName(previousName);
        String new_name = organizationDao.findNameByName(newName);
        return (previous_name == null || previous_name.length() <= 0) && new_name != null && new_name.equals(newName);
    }

    /**
     * 删除班级
     */
    public boolean deleteOrganization(String organizationName) {
        organizationDao.deleteOrganizationByName(organizationName);
        String name = organizationDao.findNameByName(organizationName);
        return name == null || name.length() <= 0;
    }

    /**
     * 分页查询Organizations
     */
    public Page<Organization> getOrganizationsNoCondition(Integer page, Integer size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "createDate", "updateDate", "id");
        return organizationDao.findAll(pageable);
    }

//    /**
//     * 分页查询users刷脸记录、用户名称、所属班级
//     */
//    public List<UsersClockingsOrganizations> getUserClockingsNoCondition(Integer page, Integer size) {
////        Pageable pageable = new PageRequest(page, size);
//        return usersClockingsOrganizationsDao.queryClockings();
//    }

    /**
     * 分页查询users刷脸记录、用户名称、所属班级
     */
    public List<UsersClockingsOrganizations> getUserClockingsNoCondition() {
//        Pageable pageable = new PageRequest(page, size);
        return usersClockingsOrganizationsDao.queryClockings();
    }

    /**
     * 分页查看“我”的刷脸记录
     */
    public Page<Date> queryMyClockings(Integer page, Integer size, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "createDate");
        return clockingDao.queryClockingsByUsername(username, pageable);
    }

    /**
     * 人脸识别
     */
    public String recognition(String data) {
        String imgFilePath = "/usr/facial/temp.png";
        File file = generateImage(data, imgFilePath);
        MultipartFile multipartFile = generateMulitpartFile(file);
        if (multipartFile == null || multipartFile.isEmpty()) {
            logger.error("识别时MultipartFile文件为空");
            return null;
        } else {
            logger.info("转换为multipartFile文件之后的文件名：" + multipartFile.getOriginalFilename());
            facialController.cat();
            String fileName = facialController.recognition(multipartFile);
            if (fileName != null && fileName.length() > 0) {
                String trimFileName = fileName.substring(0, fileName.lastIndexOf("."));
                String nickName = userDao.getNicknameByUsername(trimFileName);
//                User userInfo = userDao.getUserByUsername()
                User user = userDao.findByUsername(trimFileName);
                logger.info("根据用户名‘" + trimFileName + "’查到昵称：" + nickName);
                if (nickName != null && nickName.length() > 0) {
                    clockingDao.save(new Clocking(new Date(), user));
                    return nickName;
                } else {
                    logger.error("找不到关于trimFileName的User信息");
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 生成multipartFile
     */
    private MultipartFile generateMulitpartFile(File file) {
        if (file == null) {
            logger.error("base64人脸文件生成为null");
            return null;
        } else {
            logger.info("base64生成文件:" + file.getAbsolutePath());
        }
        try {
            return new MockMultipartFile(file.getName(), file.getName(), "", new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成File
     */
    private File generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null || imgStr.isEmpty()) {
            logger.error("Base64码为null");
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
//            String imgFilePath = "/usr/facial/temp.png";
            if (Files.exists(Paths.get(imgFilePath))) {
                boolean result = new File(imgFilePath).delete();
                if (result) {
                    logger.info("同名文件已存在，现已删除");
                } else {
                    logger.error("无法删除已存在的同名文件:" + imgFilePath);
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return new File(imgFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
