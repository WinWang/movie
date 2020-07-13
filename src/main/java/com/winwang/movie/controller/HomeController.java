package com.winwang.movie.controller;


import com.winwang.movie.common.Constant;
import com.winwang.movie.pojo.PageVo;
import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.loader.strategy.HtmlUnitPageLoader;
import com.xuxueli.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    String home = "";

    @GetMapping("")
    public String getMovieHome() {
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(Constant.MOVIE_URL)
                .setAllowSpread(false)
                .setPageLoader(new HtmlUnitPageLoader())        // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageVo pageVo) {
//                        home = html.text();
                        home = pageVo.toString();
                        List<String> list = pageVo.getList();
                        if(list!=null&&list.size()>0){
                            for (String s : list) {
                                System.out.println(s+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                            }
                        }
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return home;
    }


}
