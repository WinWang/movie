package com.winwang.movie.pojo.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movie")
public class MovieBean {
    @Id
    @GeneratedValue()
    private Integer id; //自增主键
    @Column(nullable = false,length = 20)
    private Integer itemType;//Recycleview类型type
    @Embedded
    private List<BannerBean> bannerList;
    @Column(nullable = true,length = 50)
    private String headTitle; //头布局的title,
    @Column(nullable = true,length = 200)
    private String coverUrl;
    @Column(nullable = true,length = 20)
    private String movieName;
    @Column(nullable = true,length = 200)
    private String linkUrl;
    @Column(nullable = true,length = 200)
    private String playUrl; //播放Url

}
