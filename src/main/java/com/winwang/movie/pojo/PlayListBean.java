package com.winwang.movie.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playlist_table")
public class PlayListBean {
    @Id
    private String playListUrl;
    @Column(nullable = false, length = 50)
    private String playName;
    @Column(nullable = false, length = 100)
    private String videoPaht; //为了表的关联关系-在详情页能查询到对应的详情的业务
}
