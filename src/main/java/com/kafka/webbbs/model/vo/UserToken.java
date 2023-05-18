package com.kafka.webbbs.model.vo;

import com.kafka.webbbs.model.entity.User;
import lombok.Data;

import java.util.Date;
@Data
public class UserToken {
    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    private String token;

    public UserToken(User user, String token) {
        this.id = user.getUid();
        this.userName = user.getUserName();
        this.userAvatar = user.getUserAvatar();
        this.userProfile = user.getUserProfile();
        this.userRole = user.getUserRole();
        this.createTime = user.getCreateTime();
        this.updateTime = user.getUpdateTime();
        this.token = token;
    }
}
