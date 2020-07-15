package com.winwang.movie.pojo;

import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.conf.XxlCrawlerConf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BannerBean {

    @PageFieldSelect(cssQuery = ".img-responsive", selectType = XxlCrawlerConf.SelectType.HTML)
    private String coverUrl;

    private String movieName;

    private String linkUrl;

}
