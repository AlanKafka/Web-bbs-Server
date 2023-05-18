package com.kafka.webbbs.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kafka.webbbs.model.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleVo {
    private String articleId;
    private int   label;
    private long uid;
    private String userName;
    private String title;
    private String cover;
    private String content;
    private String markdownContent;
    private Integer editorType;
    private String summary;
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date  postTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    private Integer readCount;
    private Integer goodCount;
    private Integer commentCount;
    private Integer topType;
    private Integer status;
    private User user;

}
