package com.winwang.movie.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieBean {
    private Integer itemType;//Recycleview类型type
    private List<BannerBean> bannerList;
    private String headTitle; //头布局的title,
    private String coverUrl;
    private String movieName;
    private String linkUrl;
    private String playUrl; //播放Url

}
