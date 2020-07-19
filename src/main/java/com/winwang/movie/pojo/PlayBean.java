package com.winwang.movie.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "video_detail")
public class PlayBean {
    @Id
    private String id;
    @Column(nullable = false, length = 100)
    private String playUrl;
//    @Embedded
//    private List<PlayListBean> playList; //用户可选播放-暂时弃用
    @Embedded
    private List<PlayListBean> urlList;  //多级播放-实际使用
}
