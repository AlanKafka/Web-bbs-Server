package com.kafka.webbbs.controller;

import cn.hutool.core.lang.UUID;
import com.kafka.webbbs.common.BaseResponse;
import com.kafka.webbbs.common.ErrorCode;
import com.kafka.webbbs.common.ResultUtils;
import com.kafka.webbbs.exception.BusinessException;
import com.kafka.webbbs.model.dto.user.UserLoginRequest;
import com.kafka.webbbs.model.dto.user.UserRegisterRequest;
import com.kafka.webbbs.model.dto.user.UserUpdateMyRequest;
import com.kafka.webbbs.model.entity.User;
import com.kafka.webbbs.model.untils.TokeConfig;
import com.kafka.webbbs.model.vo.LoginUserVO;
import com.kafka.webbbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private TokeConfig tokeConfig;

    
    @GetMapping ("/authentication")
    public BaseResponse<String> authenticationCheck(HttpServletRequest request){
        
        String jwtToken = request.getHeader("jwtToken");
        boolean authentication = userService.authentication(jwtToken);
        if(!authentication){
                return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"jwtToken失效");
        }
        User tokeMessage = tokeConfig.getTokeMessage(jwtToken);
        Long uid = tokeMessage.getUid();
        //获取用户图片
        User user = userService.UserByUid(uid);
        String userAvatar = user.getUserAvatar();
        return ResultUtils.success(userAvatar);
    }
    
    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            ResultUtils.error(ErrorCode.PARAMS_ERROR,"请输入信息");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            ResultUtils.error(ErrorCode.PARAMS_ERROR,"请输入信息");
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户上传图片
     */
    /* 时间格式化
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");
    
    @PostMapping("/UploadPicture")
    public BaseResponse<Integer> uploadPicture(MultipartFile file, HttpServletRequest request) {
        System.out.println(file);
        //从请求头获取jwt
        String jwt = request.getHeader("jwtToken");
        //鉴权
        boolean checkToke = tokeConfig.checkToke(jwt);
        if(!checkToke){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"token失效");
        }
        //解析jwt的信息
        User jwtToken = tokeConfig.getTokeMessage(jwt);
        
        //获取上传的文件的文件名
        String fileName = file.getOriginalFilename();
        //处理文件重名问题
        String hzName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + hzName;
        String photoPath = "C:\\Users\\hugua\\Pictures\\uploadfile";
        File file1 = new File(photoPath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        String finalPath = photoPath + File.separator + fileName;
        try {
            file.transferTo(new File(finalPath));
            int i = userService.UploadPicture(jwtToken.getUid(), finalPath);
            return  ResultUtils.success(i);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取用户资料
     */
    @GetMapping("/info")
    public BaseResponse<User> UserByUid(HttpServletRequest httpServletRequest){
        String jwtToken = httpServletRequest.getHeader("jwtToken");
        //鉴权
        boolean checkToke = tokeConfig.checkToke(jwtToken);
        if(!checkToke){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"token失效");
        }
        //通过jwt获取uid
        User tokeMessage = tokeConfig.getTokeMessage(jwtToken);
        User user = userService.UserByUid(tokeMessage.getUid());
        return ResultUtils.success(user);
    }

    /**
     * 修改用户资料
     */
    @PostMapping("/updateInfo")
    public BaseResponse<Integer> updateInfoByUid(@RequestBody User user, HttpServletRequest httpServletRequest){
        //获取token
        String jwtToken = httpServletRequest.getHeader("jwtToken");
        //鉴权
        boolean checkToke = tokeConfig.checkToke(jwtToken);
        //判断 toke 是否过期获取没有
        if(!checkToke){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"token失效");
        }
        //获取信息
        User tokeMessage = tokeConfig.getTokeMessage(jwtToken);
        Long uid = tokeMessage.getUid();
        int i = userService.updateInfoByUid(uid, user);
        return ResultUtils.success(i);
    }
}
