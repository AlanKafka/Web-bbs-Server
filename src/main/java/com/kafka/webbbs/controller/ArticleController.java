package com.kafka.webbbs.controller;


import com.kafka.webbbs.common.BaseResponse;
import com.kafka.webbbs.common.ErrorCode;
import com.kafka.webbbs.common.ResultUtils;
import com.kafka.webbbs.model.dto.article.ArticleVo;
import com.kafka.webbbs.model.entity.Article;
import com.kafka.webbbs.model.entity.User;
import com.kafka.webbbs.model.untils.TokeConfig;
import com.kafka.webbbs.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@Slf4j
public class ArticleController {
    @Resource
    ArticleService articleService;
    @Resource
    private TokeConfig tokeConfig;
    @GetMapping("/home")
    public BaseResponse<ArrayList<ArticleVo>> getAllArticleHome() {
        ArrayList<ArticleVo> allArticleHome = articleService.getAllArticleHome();
        return ResultUtils.success(allArticleHome);
    }

    @GetMapping("/article")
    public BaseResponse<ArticleVo> getArticleById(@RequestParam(value = "articleId") String articleId) {
        ArticleVo article = articleService.getArticleById(articleId);
        return ResultUtils.success(article);
    }

    @GetMapping("/home/time")
    public BaseResponse<ArrayList<ArticleVo>> getAllArticleHomeToTime() {
        ArrayList<ArticleVo> allArticleHomeToTime = articleService.getAllArticleHomeToTime();
        return ResultUtils.success(allArticleHomeToTime);
    }
    @GetMapping("/front/hot")
    public BaseResponse<ArrayList<ArticleVo>> getAllArticleFrontHot() {
        ArrayList<ArticleVo> allArticleFront = articleService.getAllArticleFront();
        return ResultUtils.success(allArticleFront);
    }

    @GetMapping("/front/time")
    public BaseResponse<ArrayList<ArticleVo>> doArticleFrontHot() {
        ArrayList<ArticleVo> allArticleFrontTime = articleService.getAllArticleFrontTime();
        return ResultUtils.success(allArticleFrontTime);
    }

    @GetMapping("/backend/hot")
    public BaseResponse<ArrayList<ArticleVo>> doArticleBackendtHot() {
        ArrayList<ArticleVo> allArticleBackend = articleService.getAllArticleBackend();
        return ResultUtils.success(allArticleBackend);
    }
    @GetMapping("/backend/time")
    public BaseResponse<ArrayList<ArticleVo>> doArticleBackendtTime() {
        ArrayList<ArticleVo> allArticleBackendTime = articleService.getAllArticleBackendTime();
        return ResultUtils.success(allArticleBackendTime);
    }

    @GetMapping("/other/hot")
    public BaseResponse<ArrayList<ArticleVo>> doArticleOther() {
        ArrayList<ArticleVo> allArticleOther = articleService.getAllArticleOther();
        return ResultUtils.success(allArticleOther);
    }

    @GetMapping("/other/time")
    public BaseResponse<ArrayList<ArticleVo>> doArticleOtherTime() {
        ArrayList<ArticleVo> allArticleOtherTime = articleService.getAllArticleOtherTime();
        return ResultUtils.success(allArticleOtherTime);
    }

    /**
     * 发布新的文章
     */
    @PostMapping("/article/newPost")
    public BaseResponse<Integer> doNewPost( @RequestParam("title") String title,
                                            @RequestParam("label") Integer label,
                                            @RequestParam("summary") String summary,
                                            @RequestParam("editorType") Integer editorType,
                                            @RequestParam("content") String content,
                                            @RequestParam("markdownContent") String markdownContent,HttpServletRequest request){
        //获取jwt
        String jwtToken = request.getHeader("jwtToken");
        //鉴权
        boolean checkToke = tokeConfig.checkToke(jwtToken);
        if(!checkToke){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"token失效");
        }
        User tokeMessage = tokeConfig.getTokeMessage(jwtToken);
        Long uid = tokeMessage.getUid();
        Integer integer = articleService.addAtricleByuid(uid, title, label, summary, editorType, content, markdownContent);
        return ResultUtils.success(integer);
    }
    /**
     * 添加阅读量
     */
    @GetMapping("/article/readCount")
    public BaseResponse<Integer> addReadCount(@RequestParam("articleId") String articleId){
        System.out.println(articleId);
        Integer integer = articleService.updateReadCount(articleId);
        return ResultUtils.success(integer);
    }
    /**
     * 获取文章作者的图片
     */
    @GetMapping("/article/info")
    public BaseResponse<User> getInfo(Integer uid){
        User info = articleService.getInfo(uid);
        return ResultUtils.success(info);
    }
    
}
