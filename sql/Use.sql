-- 创建库
create database my_db default charset utf8mb4;
use my_db;
-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
    ) comment '用户' collate = utf8mb4_unicode_ci;

### 文章内容
DROP TABLE IF EXISTS `article`;
CREATE TABLE article
(
    `article_id`       varchar(15)   NOT NULL COMMENT '文章id',
    `uid`              bigint       NOT NULL COMMENT '用户id',
    `userName`         varchar(256) NOT NULL COMMENT '用户名称',
    `title`            varchar(255) NOT NULL COMMENT '标题',
    `label`             int NOT NULL COMMENT '标签 -1为其他，0为前端，1为后端',
    `cover`            varchar(255)      DEFAULT NULL COMMENT '封面',
    `content`          text COMMENT '内容',
    `markdown_content` text COMMENT 'markdown内容',
    `editor_type`      tinyint(4)   NOT NULL COMMENT '0 富文本编辑器 1:markdown编辑器',
    `summary`          varchar(200)      DEFAULT NULL COMMENT '摘要',
    `post_time`        datetime     NOT NULL COMMENT '发布时间',
    `update_time`      timestamp    NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `read_count`       int(50)           DEFAULT '0' COMMENT '阅读数量',
    `good_count`       int(50)           DEFAULT '0' COMMENT '点赞数量',
    `comment_count`    int(50)           DEFAULT '0' COMMENT '评论数',
    `status`           tinyint(4)        DEFAULT NULL COMMENT '-1已删除,0待审核，1-已审核',
    primary key (article_id),
    FOREIGN KEY (uid) REFERENCES user (uid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章信息';