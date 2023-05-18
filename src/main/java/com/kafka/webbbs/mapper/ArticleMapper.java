package com.kafka.webbbs.mapper;


import com.kafka.webbbs.model.dto.article.ArticleVo;
import com.kafka.webbbs.model.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
@Mapper
public interface ArticleMapper {
   /**
    * home 请求与数据
    * @return
    */
   ArrayList<ArticleVo> getAllArticleHome();

   /**
    * 根据文章id查询文章内容
    */
   ArticleVo getArticleById(String articleId);

   /**
    * 根据时间排序的首页文章
    * @return
    */
   ArrayList<ArticleVo> getAllArticleHomeToTime();

   /**
    * 前端的全部文章 按热度排序
    * @return
    */
   ArrayList<ArticleVo> getAllArticleFront();

   /**
    * 前端的全部文章 按发布时间排序
    * @return
    */
   ArrayList<ArticleVo> getAllArticleFrontTime();

   /**
    * 后端的全部文章 按热度排序
    */
   ArrayList<ArticleVo> getAllArticleBackend();

   /**
    * 后端的全部文章 按时间排序
    */
   ArrayList<ArticleVo> getAllArticleBackendTime();

   /**
    * 其他文章 按热度排序
    * @return
    */
   ArrayList<ArticleVo> getAllArticleOther();
   /**
    * 其他文章 按发布时间排序
    */
   ArrayList<ArticleVo> getAllArticleOtherTime();

   /**
    * 发布文章
    */
   Integer addAtricleByuid(Article article);

   /**
    * 增加阅读量
    */
   Integer updateReadCount(Integer readCount,String articleId);
}
