package com.winwang.movie.service;


import com.winwang.movie.common.Constant;
import com.winwang.movie.crawler.MyHtmlUnitLoader;
import com.winwang.movie.pojo.base.ResObject;
import com.winwang.movie.pojo.live.LiveBean;
import com.winwang.movie.pojo.live.LiveTypeBean;
import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.parser.PageParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LiveTVService implements BaseService {


    public ResObject getLiveTvList() {
        ResObject temp = new ResObject();
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(Constant.LIVE_TV_URL)
                .setAllowSpread(false)
                .setPageLoader(new MyHtmlUnitLoader())         // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<String>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, String pageVo) {
                        List<LiveTypeBean> liveList = new ArrayList<>();
                        handleLiveCrawler(liveList, html, temp);
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return temp;
    }


    public ResObject getLiveTVDetail(String tvPath) {
        ResObject temp = new ResObject();
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(Constant.LIVE_TV_URL + tvPath)
                .setAllowSpread(false)
                .setPageLoader(new MyHtmlUnitLoader())         // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<String>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, String pageVo) {
                        handleLiveDetailCrawler(html, temp);
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return temp;
    }

    private void handleLiveDetailCrawler(Document document, ResObject temp) {
        Elements embed = document.getElementsByTag("embed");
        if (embed != null && embed.size() > 0) {
            String src = embed.get(0).attr("src");
            temp.setResult(src);
        } else {
            Elements iframe = document.getElementsByTag("iframe");
            if (iframe != null && iframe.size() > 0) {
                String src = iframe.get(0).attr("src");
                temp = crawlerTwice(src, temp);
            }
        }
        ResObject.setSucecss(temp);
    }

    private ResObject crawlerTwice(String src, ResObject temp) {
        src = src.startsWith("http") ? src : "http:" + src;
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(src)
                .setAllowSpread(false)
                .setPageLoader(new MyHtmlUnitLoader())         // HtmlUnit 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<String>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, String pageVo) {
                        String s = handleTwiceCrawler(html);
                        temp.setResult(s);
                    }
                })
                .build();
        // 启动
        crawler.start(true);
        return temp;
    }

    private String handleTwiceCrawler(Document document) {
        Elements iframe = document.getElementsByTag("iframe");
        if (iframe != null && iframe.size() > 0) {
            for (Element element : iframe) {
                String src = element.attr("src");
                if (src.contains(".m3u8")) {
                    String[] split = src.split("&url=");
                    if (split != null && split.length >= 2) {
                        return split[1];
                    }
                }
            }
        }
        return "";
    }


    /**
     * 抓取电视直播页面数据
     *
     * @param liveList
     * @param document
     * @param temp
     */
    private void handleLiveCrawler(List<LiveTypeBean> liveList, Document document, ResObject temp) {
        Elements pp_b = document.getElementsByClass("pp b");
        Elements xyou = pp_b.get(0).getElementsByClass("xyou");
        for (Element element : xyou) {
            String title = element.getElementsByClass("cy").text();
            Elements a = element.getElementsByTag("a");
            ArrayList<LiveBean> tempList = new ArrayList<>();
            for (Element element1 : a) {
                Elements href = element1.getElementsByAttribute("href");
                String itemText = href.text();
                String linkUrl = href.attr("href");
                tempList.add(new LiveBean(itemText, linkUrl));
            }
            liveList.add(new LiveTypeBean(title, tempList));
        }
        temp.setResult(liveList);
        ResObject.setSucecss(temp);
    }


}
