package com.kafka.webbbs.mapper;

import com.kafka.webbbs.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询数据
     * @param userAccount
     * @return
     */
    long  getByuserAccount(String userAccount);
    /**
     * 注册用户
     */
    long userRegister(User user);
    /**
     * 根据用户名查询uid
     */
    long getUidByuserAccount(String userAccount);

    /**
     * 根据 用户名查询信息
     * @return
     */
    User getUserByusername(String userAccount);

    /**
     * 根据用户 uid查询信息
     */
    User getUserByUid(long uid);
    /**
     * 用户上传图像
     */
    int UploadPicture(long uid,String userAvatar);


    /**
     * 修改用户简介和用户名
     */
    int updateUserInfo(User user);

}
