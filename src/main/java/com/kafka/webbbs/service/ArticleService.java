package com.kafka.webbbs.service;

import com.kafka.webbbs.model.dto.article.ArticleVo;
import com.kafka.webbbs.model.entity.Article;
import com.kafka.webbbs.model.entity.User;

import java.util.ArrayList;
import java.util.logging.Logger;

public interface ArticleService {
    /**
     * 获取主页数据
     * @return
     */
    ArrayList<ArticleVo> getAllArticleHome();
    /**
     * 根据id获取文章内容
     */
    ArticleVo getArticleById(String articleId);

    /**
     * 主页数据根据按最新时间发布排序
     * @return
     */
    ArrayList<ArticleVo> getAllArticleHomeToTime();

    /**
     * 获取前端信息 根据热度排序desc
     * @return
     */
    ArrayList<ArticleVo> getAllArticleFront();

    /**
     * 获取前端信息 根据发布时间排序desc
     * @return
     */
    ArrayList<ArticleVo> getAllArticleFrontTime();

    /**
     * 获取后端文章信息 根据默认热度排序
     * @return
     */
    ArrayList<ArticleVo> getAllArticleBackend();

    /**
     * 获取后端文章信息 根据发布时间排序
     */
    ArrayList<ArticleVo> getAllArticleBackendTime();

    /**
     * 获取其他文章信息，根据热度排序
     * @return
     */
    ArrayList<ArticleVo> getAllArticleOther();

    /**
     * 获取其他文章的信息 按找发布时间排序
     * @return
     */
    ArrayList<ArticleVo> getAllArticleOtherTime();

    /**
     * 发布文章
     */
    Integer addAtricleByuid(Long uid,
                            String title,
                            Integer label,
                            String summary,
                            Integer editorType,
                            String content,
                            String markdownContent);

    /**
     * 添加阅读量
     */
    Integer updateReadCount(String articleId);

    /**
     * 获取文章作者的图片
     */
    User getInfo(Integer uid);
}
