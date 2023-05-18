package com.kafka.webbbs.service.impl;

import cn.hutool.core.lang.UUID;
import com.kafka.webbbs.common.ErrorCode;
import com.kafka.webbbs.exception.BusinessException;
import com.kafka.webbbs.mapper.ArticleMapper;
import com.kafka.webbbs.mapper.UserMapper;
import com.kafka.webbbs.model.dto.article.ArticleVo;
import com.kafka.webbbs.model.entity.Article;
import com.kafka.webbbs.model.entity.User;
import com.kafka.webbbs.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    UserMapper userMapper;
    /**
     * 获取主页数据
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleHome() {
        ArrayList<ArticleVo> allArticleHome = articleMapper.getAllArticleHome();
        return allArticleHome;
    }

    /**
     * 根据id获取文章内容
     *
     * @param articleId
     */
    @Override
    public ArticleVo getArticleById(String articleId) {
        ArticleVo article = articleMapper.getArticleById(articleId);
        return article;
    }

    /**
     * 主页数据根据按最新时间发布排序
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleHomeToTime() {
        ArrayList<ArticleVo> articleArrayList = articleMapper.getAllArticleHomeToTime();
        return articleArrayList;
    }

    /**
     * 获取前端信息 根据热度排序desc
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleFront() {
        ArrayList<ArticleVo> allArticleFront = articleMapper.getAllArticleFront();
        return allArticleFront;
    }

    /**
     * 获取前端信息 根据发布时间排序desc
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleFrontTime() {
        ArrayList<ArticleVo> allArticleFrontTime = articleMapper.getAllArticleFrontTime();
        return allArticleFrontTime;
    }

    /**
     * 获取后端文章信息 根据默认热度排序
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleBackend() {
        ArrayList<ArticleVo> allArticleBackend = articleMapper.getAllArticleBackend();
        return allArticleBackend;
    }

    /**
     * 获取后端文章信息 根据发布时间排序
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleBackendTime() {
        ArrayList<ArticleVo> allArticleBackendTime = articleMapper.getAllArticleBackendTime();
        return allArticleBackendTime;
    }

    /**
     * 获取其他文章信息，根据热度排序
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleOther() {
        ArrayList<ArticleVo> allArticleOther = articleMapper.getAllArticleOther();
        return allArticleOther;
    }

    /**
     * 获取其他文章的信息 按找发布时间排序
     *
     * @return
     */
    @Override
    public ArrayList<ArticleVo> getAllArticleOtherTime() {
        ArrayList<ArticleVo> allArticleOtherTime = articleMapper.getAllArticleOtherTime();
        return allArticleOtherTime;
    }

    /**
     * 发布文章
     *
     * @param uid
     * @param title
     * @param label
     * @param summary
     * @param editorType
     * @param content
     * @param markdownContent
     */
    @Override
    public Integer addAtricleByuid(Long uid, 
                                   String title, 
                                   Integer label,
                                   String summary,
                                   Integer editorType,
                                   String content,
                                   String markdownContent) {
        User userByUid = userMapper.getUserByUid(uid);
        if(userByUid==null){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"用户不存在");
        }
        if(userByUid.getIsDelete() ==0 ){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"用户已注销");
        }
        Article article = new Article();
        article.setArticleId(UUID.randomUUID().toString());
        article.setUid(uid);
        article.setUserName(userByUid.getUserName());
        article.setTitle(title);
        article.setLabel(label);
        article.setContent(content);
        article.setMarkdownContent(markdownContent);
        article.setEditorType(editorType);
        article.setSummary(summary);
        article.setPostTime(new Date());
        article.setUpdateTime(new Date());
        article.setReadCount(0);
        article.setGoodCount(0);
        article.setCommentCount(0);
        article.setStatus(1);
        Integer integer = articleMapper.addAtricleByuid(article);
        return integer;
    }

    /**
     * 添加阅读量
     *
     * @param articleId
     */
    @Override
    public Integer updateReadCount(String articleId) {
        //先查询文章存不存在
        ArticleVo article = articleMapper.getArticleById(articleId);
        if(article == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"文章不存在");
        }
        if(article.getStatus() != 1){
            throw  new BusinessException(ErrorCode.FORBIDDEN_ERROR,"文章没被审核");
        }
        
        Integer readCount = article.getReadCount();
        Integer integer = articleMapper.updateReadCount(++readCount, articleId);
        return integer;
    }

    /**
     * 获取文章作者的图片
     *
     * @param uid
     */
    @Override
    public User getInfo(Integer uid) {
        User userByUid = userMapper.getUserByUid(uid);
        return userByUid;
    }
}
