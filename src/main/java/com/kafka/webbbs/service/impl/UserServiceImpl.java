package com.kafka.webbbs.service.impl;

import cn.hutool.core.lang.UUID;
import com.kafka.webbbs.common.ErrorCode;
import com.kafka.webbbs.model.entity.User;
import com.kafka.webbbs.exception.BusinessException;
import com.kafka.webbbs.mapper.UserMapper;
import com.kafka.webbbs.model.untils.TokeConfig;
import com.kafka.webbbs.model.vo.LoginUserVO;
import com.kafka.webbbs.model.vo.UserToken;
import com.kafka.webbbs.service.UserService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokeConfig tokeConfig;
    /**
     * 盐值用于加密
     */
    private  static final String SALT = "kafka";


    /**
     * 鉴权发送请求鉴权
     *
     * @param jwtToken
     */
    @Override
    public boolean authentication(String jwtToken) {
        boolean checkToke = tokeConfig.checkToke(jwtToken);
        if(!checkToke){
            return false;
        }
        return true;
    }

    /**
     * 用户注册
     * @param userAccount 用户名
     * @param userPassword 密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword,String checkPassword) {
        //1、校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4 && userAccount.length() >16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //2、校验
        synchronized (userAccount.intern()){
            //（1)根据用户名查询数据库是否有重名
            long count = userMapper.getByuserAccount(userAccount);
            if(count>0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
            }
            //(2)加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //3、插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setUserRole("user");
            user.setIsDelete(1); //1、未注销 0、已注销
            user.setUserName(UUID.randomUUID().toString());
            long result = userMapper.userRegister(user);
            if(result<0){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据库异常");
            }
            long uid = userMapper.getUidByuserAccount(user.getUserAccount());
            return uid;
        }
    }

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword) {
        //1、校验参数
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4 && userAccount.length() >16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询
        User user = userMapper.getUserByusername(userAccount);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        if(user.getIsDelete()== 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已注销");
        }
        if(!encryptPassword.equals(user.getUserPassword())){
            throw new BusinessException(ErrorCode.WRONG_PASSWORD, "用户不存在或密码错误");
        }
        TokeConfig tokeConfig = new TokeConfig();
        String toke = tokeConfig.getTokes(user.getUid(), user.getUserAccount(), user.getUserPassword());
        UserToken userToken = new UserToken(user,toke);
        return this.getLoginUserVO(userToken);
    }

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(UserToken userToken) {
        if (userToken == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(userToken, loginUserVO);
        return loginUserVO;
    }

    /**
     * 用户上传图像
     *
     * @param uid
     * @param filePath
     */
    @Override
    public int UploadPicture(long uid, String filePath) {
        User userByUid = userMapper.getUserByUid(uid);
        if(userByUid == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        }
        if(userByUid.getIsDelete()==0){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"用户已注销");
        }
        int result = userMapper.UploadPicture(uid, filePath);
        return result;
    }

    /**
     * 根据用户uid 查询信息到资料
     *
     * @param uid
     */
    @Override
    public User UserByUid(long uid) {
        User userByUid = userMapper.getUserByUid(uid);
        if(userByUid == null){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        }
        if(userByUid.getIsDelete()==0){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"用户已注销");
        }
        return userByUid;
    }

    /**
     * 根据用户uid，修改用户的信息
     *
     * @param uid
     * @param user
     */
    @Override
    public int updateInfoByUid(long uid, User user) {
        User userByUid = userMapper.getUserByUid(uid);
        if(userByUid == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        }
        if(userByUid.getIsDelete() == 0){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"用户已注销");
        }
        User user1 = new User();
        user1.setUid(uid);
        user1.setUserName(user.getUserName());
        user1.setUserProfile(user.getUserProfile());
        int i = userMapper.updateUserInfo(user1);
        if(i !=1){
            throw  new BusinessException(ErrorCode.OPERATION_ERROR,"修改失败");
        }
        return i;
    }
}
