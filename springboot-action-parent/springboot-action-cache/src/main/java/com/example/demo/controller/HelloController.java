package com.example.demo.controller;

import com.example.demo.vo.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {
    @GetMapping("/hello")
    @Cacheable(cacheNames="A")
    public Result hello(){
        return Result.ok();
    }

    @GetMapping("/getArticleById/{articleId}")
    @Cacheable(value= "articleCache")
    public String getArticleById(@PathVariable("articleId") long articleId) {
        System.out.println("aaa");
        return "articleId";
    }

    @GetMapping("/getResult")
    @Cacheable(value= "articleCache", key= "#result")
    public Result getResult() {
        System.out.println("bbb");
        return Result.ok();
    }
}
