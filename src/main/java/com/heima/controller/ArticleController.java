package com.heima.controller;

import com.heima.pojo.Article;
import com.heima.pojo.PageBean;
import com.heima.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {


    @GetMapping("/list")
    public Result<PageBean<Article>> list() {
        return Result.success();
    }
}
