package com.kafka.webbbs.service;


import com.kafka.webbbs.model.entity.User;
import com.kafka.webbbs.model.vo.LoginUserVO;
import com.kafka.webbbs.model.vo.UserToken;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


public interface UserService {

    /**
     * 鉴权发送请求鉴权
     */
    boolean authentication(String jwtToken);

    /**
     * 用户注册
     */
    long userRegister(String userAccount,String userPassword,String checkPassword );

    /**
     * 根据 用户名查询信息
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword);
    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(UserToken userToken);

    /**
     * 用户上传图像
     */
    int UploadPicture(long uid,String filePath);
    /**
     * 根据用户uid 查询信息到资料
     */
    User UserByUid(long uid);

    /**
     * 根据用户uid，修改用户的信息
     */
    int updateInfoByUid(long uid,User user);
}
