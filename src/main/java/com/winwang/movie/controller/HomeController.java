package com.winwang.movie.controller;


import com.winwang.movie.common.Constant;
import com.winwang.movie.common.MovieTypeEnum;
import com.winwang.movie.pojo.*;
import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.loader.strategy.HtmlUnitPageLoader;
import com.xuxueli.crawler.parser.PageParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

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
                        List<MovieBean> movieList = new ArrayList<>();
                        handleDocument(movieList, html, temp);
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return temp;
    }

    @PostMapping("/detail")
    public ResObject detailMovie(@RequestParam String path) {
        ResObject detailRes = new ResObject();
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(Constant.MOVIE_URL + path)
                .setAllowSpread(false)
                .setPageLoader(new HtmlUnitPageLoader())        // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<String>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, String pageVo) {
                        handleCommonDetail(html, detailRes);
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return detailRes;

    }

    /**
     * 获取播放页详情ID
     *
     * @param html

     */
    private void handleCommonDetail(Document html, ResObject detailRes) {
        PlayBean playBean = new PlayBean();
        Element playList = html.getElementById("stab81");
        Element vlink_1 = playList.getElementById("vlink_1");
        Elements li = vlink_1.getElementsByTag("li");
        if (li != null && li.size() > 0) {
            List<PlayListBean> listBeans = new ArrayList<>();
            for (Element element : li) {
                Elements a = element.getElementsByTag("a");
                String text = a.get(0).text();
                String src = a.get(0).attr("href");
                PlayListBean playListBean = PlayListBean.builder().playListUrl(src).playName(text).build();
                listBeans.add(playListBean);
            }
            playBean.setPlayList(listBeans);
        }

        Elements playUrlList = html.getElementsByClass("panel panel-quality-1");
        if (playUrlList != null && playUrlList.size() > 0) {
            for (Element element : playUrlList) {
                Elements playElement = element.getElementsByClass("col-lg-7 col-md-6 col-sm-6 col-xs-12 td-dl-links");
                if (playElement != null && playElement.size() > 0) {
                    for (Element ele : playElement) {
                        Elements a = ele.getElementsByTag("a");
                        String href_original = a.attr("href_original");
                        playBean.setPlayUrl(href_original);
                    }
                }
            }
        }
        detailRes.setResult(playBean);
        ResObject.setSucecss(detailRes);
    }


    private void handleDocument(List<MovieBean> movieList, Document document, ResObject resObject) {
        try {
            //设置Banner
            Element hotBanner = document.getElementById("hot_1");
            Elements hotBanners = hotBanner.getElementsByClass("list_mov");
            List<BannerBean> bannerList = new ArrayList<>();
            for (Element banner : hotBanners) {
                Elements coverElement = banner.getElementsByClass("list_mov_poster");
                Elements imgElement = coverElement.get(0).getElementsByTag("img");
                String imgCover = imgElement.get(0).attr("src");
                Elements bannerTitle = banner.getElementsByTag("a");
                String linkUrl = bannerTitle.attr("href");
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
            setHeadMovieTitle("最新电影",movieList);
            Elements hotMovieElements = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl2");
            Elements childrenElement = hotMovieElements.get(0).children();
            if (childrenElement != null) {
                formatMovieData(childrenElement,movieList);
            }
            //最新电视剧
            setHeadMovieTitle("最新电视剧",movieList);
            Elements hotTVElements = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl4");
            Elements childrenTVElement = hotTVElements.get(0).children();
            formatMovieData(childrenTVElement,movieList);
            //最新动漫
            setHeadMovieTitle("动漫综艺",movieList);
            Elements entertainmentAndCartoonElement = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl8");
            Elements entertainmentEle = entertainmentAndCartoonElement.get(0).children();
            formatMovieData(entertainmentEle,movieList);
            //最新综艺
            setHeadMovieTitle("娱乐综艺",movieList);
            Elements cartoonEle = entertainmentAndCartoonElement.get(1).children();
            formatMovieData(cartoonEle,movieList);
            resObject.setResult(movieList);
            ResObject.setSucecss(resObject);
        } catch (Exception e) {
            ResObject.setFail(resObject);
        }

    }

    private void formatMovieData(Elements childrenTVElement, List<MovieBean> movieList) {
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

    private void setHeadMovieTitle(String title, List<MovieBean> movieList) {
        MovieBean movieBean = new MovieBean();
        movieBean.setItemType(MovieTypeEnum.MOVIE_HEADER.getType());
        movieBean.setHeadTitle(title);
        movieList.add(movieBean);
    }


}
