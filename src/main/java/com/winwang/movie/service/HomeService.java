package com.winwang.movie.service;

import com.winwang.movie.common.Constant;
import com.winwang.movie.common.MovieTypeEnum;
import com.winwang.movie.pojo.*;
import com.winwang.movie.respository.*;
import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.loader.strategy.HtmlUnitPageLoader;
import com.xuxueli.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class HomeService implements BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private VideoDetailRepository videoDetailRepository;

    @Autowired
    private PlayListRepository playListRepository;


    public List<MovieBean> getHomeDataByDB() {
        List<BannerBean> bannerList = bannerRepository.findAll();
        List<MovieBean> homeList = homeRepository.findAll();
        MovieBean movieBanner = new MovieBean();
        movieBanner.setItemType(MovieTypeEnum.MOVIE_BANNER.getType());
        movieBanner.setBannerList(bannerList);
        if (homeList != null && homeList.size() > 0) {
            homeList.remove(0);
        }
        homeList.add(0, movieBanner);
        return homeList;
    }


    public PlayBean getVideoDetailByDB(String path) {
        Optional<PlayBean> videoDetailRepositoryById = videoDetailRepository.findById(path);
        if (videoDetailRepositoryById.isPresent()) {
            PlayBean playBean = videoDetailRepositoryById.get();
            List<PlayListBean> playListBeanByVideoPaht = playListRepository.findPlayListBeanByVideoPaht(path);
            playBean.setUrlList(playListBeanByVideoPaht);
            playBean.setPlayUrl(playListBeanByVideoPaht.get(0).getPlayListUrl());
            return playBean;
        } else {
            return null;
        }
    }


    /**
     * 抓取电影首页数据
     *
     * @return
     */
    public ResObject getHomeData() {
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

    /**
     * 获取单个电影详情
     *
     * @param path
     * @return
     */
    public ResObject getVideoDetail(String path) {
        ResObject detailRes = new ResObject();
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(Constant.MOVIE_URL + path)
                .setAllowSpread(false)
                .setPageLoader(new HtmlUnitPageLoader())        // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<String>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, String pageVo) {
                        handleCommonDetail(html, detailRes, path);
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
    private void handleCommonDetail(Document html, ResObject detailRes, String path) {
        PlayBean playBean = new PlayBean();
        playBean.setId(path);
        Elements tbody = html.getElementsByTag("tbody");
        Elements tr = tbody.get(0).getElementsByTag("tr");
        if (tr != null && tr.size() > 0) {
            List<PlayListBean> listBeans = new ArrayList<>();
            for (Element element : tr) {
                Elements td = element.getElementsByTag("td");
                Element aTag = td.get(0).getElementsByTag("a").get(0);
                String playUrl = aTag.attr("href");
                String playTitle = aTag.text();
                PlayListBean playListBean = PlayListBean.builder()
                        .playListUrl(playUrl)
                        .playName(playTitle)
                        .videoPaht(path)
                        .build();
                playBean.setPlayUrl(playUrl);
                playListRepository.save(playListBean);
                listBeans.add(playListBean);
            }
            videoDetailRepository.save(playBean);
            playBean.setUrlList(listBeans);
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
                BannerBean bannerBean = new BannerBean();
                bannerBean.setCoverUrl(imgCover);
                bannerBean.setLinkUrl(linkUrl);
                bannerBean.setMovieName(bannerTitle.text());
                bannerList.add(bannerBean);
            }
            bannerRepository.saveAll(bannerList);
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
            setHeadMovieTitle("最新电影", movieList);
            Elements hotMovieElements = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl2");
            Elements childrenElement = hotMovieElements.get(0).children();
            if (childrenElement != null) {
                formatMovieData(childrenElement, movieList);
            }
            //最新电视剧
            setHeadMovieTitle("最新电视剧", movieList);
            Elements hotTVElements = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl4");
            Elements childrenTVElement = hotTVElements.get(0).children();
            formatMovieData(childrenTVElement, movieList);
            //最新动漫
            setHeadMovieTitle("动漫综艺", movieList);
            Elements entertainmentAndCartoonElement = document.getElementsByClass("col-sm-12 col-md-12 col-lg-9 col-xlg-9 section-list sl8");
            Elements entertainmentEle = entertainmentAndCartoonElement.get(0).children();
            formatMovieData(entertainmentEle, movieList);
            //最新综艺
            setHeadMovieTitle("娱乐综艺", movieList);
            Elements cartoonEle = entertainmentAndCartoonElement.get(1).children();
            formatMovieData(cartoonEle, movieList);
            resObject.setResult(movieList);
            homeRepository.saveAll(movieList);
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


    public List<Test> test() {
        List<Test> all = userRepository.findAll();
        return all;
    }


}
