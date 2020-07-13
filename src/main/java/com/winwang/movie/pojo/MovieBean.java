package com.winwang.movie.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieBean {
    private String coverUrl;
    private String movieName;
    private String linkUrl;
    private String headTitle; //头布局的title,
    private Integer type;//Recycleview类型type

}
