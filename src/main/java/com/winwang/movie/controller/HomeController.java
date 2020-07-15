package com.winwang.movie.controller;


import com.winwang.movie.common.Constant;
import com.winwang.movie.common.MovieTypeEnum;
import com.winwang.movie.pojo.BannerBean;
import com.winwang.movie.pojo.MovieBean;
import com.winwang.movie.pojo.PageVo;
import com.winwang.movie.pojo.ResObject;
import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.loader.strategy.HtmlUnitPageLoader;
import com.xuxueli.crawler.parser.PageParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    List<MovieBean> movieList = new ArrayList<>();

    @GetMapping("")
    public ResObject getMovieHome() {
        ResObject temp = new ResObject();
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(Constant.MOVIE_URL)
                .setAllowSpread(false)
                .setPageLoader(new HtmlUnitPageLoader())        // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageVo pageVo) {
                        handleDocument(html, temp);
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return temp;
    }


    private void handleDocument(Document document, ResObject resObject) {
        try {
            //设置Banner
            Element hotBanner = document.getElementById("hot_1");
            Elements hotBanners = hotBanner.getElementsByClass("list_mov");
            List<BannerBean> bannerList = new ArrayList<>();
            for (Element banner : hotBanners) {
                Elements coverElement = banner.getElementsByClass("list_mov_poster");
                Elements imgElement = coverElement.get(0).getElementsByTag("img");
                String imgCover = imgElement.get(0).attr("src");
                Elements bannerTitle = banner.getElementsByTag("h4");
                String linkUrl = bannerTitle.attr("href");
                System.out.println(">>>>>>>>>>>" + imgCover+">>>>>>"+bannerTitle.text());
                bannerList.add(new BannerBean(imgCover, bannerTitle.text(), linkUrl));
            }
            MovieBean movieBanner = new MovieBean();
            movieBanner.setItemType(MovieTypeEnum.MOVIE_BANNER.getType());
            movieBanner.setBannerList(bannerList);
            movieList.add(movieBanner);

            //设置TAB
            Elements elementsByClass = document.getElementsByClass("nav navbar-nav");
            Elements children = elementsByClass.get(0).children();
            for (Element child : children) {
                Elements tabTag = child.getElementsByTag("a");
                String tabTitle = tabTag.text();
                String href = tabTag.get(0).attr("href");//跳转连接
                MovieBean movieBean = new MovieBean();
                movieBean.setItemType(MovieTypeEnum.MOVIE_TAB.getType());
                movieBean.setMovieName(tabTitle);
                movieBean.setLinkUrl(href);
                movieList.add(movieBean);
            }
            //最新电影
            setHeadMovieTitle("最新电影");

            Elements hotMovieElements = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl2");
            Elements childrenElement = hotMovieElements.get(0).children();
            if (childrenElement != null) {
                formatMovieData(childrenElement);
            }

            //最新电视剧
            setHeadMovieTitle("最新电视剧");

            Elements hotTVElements = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl4");
            Elements childrenTVElement = hotTVElements.get(0).children();
            formatMovieData(childrenTVElement);

            setHeadMovieTitle("动漫综艺");
            Elements entertainmentAndCartoonElement = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl8");
            Elements entertainmentEle = entertainmentAndCartoonElement.get(0).children();
            formatMovieData(entertainmentEle);

            setHeadMovieTitle("娱乐综艺");
            Elements cartoonEle = entertainmentAndCartoonElement.get(1).children();
            formatMovieData(cartoonEle);

            resObject.setResult(movieList);
            ResObject.setSucecss(resObject);
        } catch (Exception e) {
            ResObject.setFail(resObject);
        }

    }

    private void formatMovieData(Elements childrenTVElement) {
        for (Element element : childrenTVElement) {
            Elements coverElement = element.getElementsByClass("img-responsive");
            Elements imgElement = coverElement.get(0).getElementsByTag("img");
            String imgCover = imgElement.get(0).attr("src");
            Elements titleH4 = element.getElementsByTag("h4");
            String linkUrl = titleH4.get(0).getElementsByTag("a").attr("href");
            MovieBean movieBean = new MovieBean();
            movieBean.setItemType(MovieTypeEnum.MOVIE_CONTENT.getType());
            movieBean.setMovieName(titleH4.text());
            movieBean.setLinkUrl(linkUrl);
            movieBean.setCoverUrl(imgCover);
            movieList.add(movieBean);
        }
    }

    private void setHeadMovieTitle(String title) {
        MovieBean movieBean = new MovieBean();
        movieBean.setItemType(MovieTypeEnum.MOVIE_HEADER.getType());
        movieBean.setHeadTitle(title);
        movieList.add(movieBean);
    }


}
