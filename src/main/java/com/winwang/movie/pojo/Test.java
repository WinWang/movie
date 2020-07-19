package com.winwang.movie.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movie_record")
public class Test {

    @Id
    @GeneratedValue
    private int id; //自增主键
    @Column(nullable = false, unique = false)
    private Integer itemType;//Recycleview类型type

    @Column(nullable = false,length = 20)
    private String headTitle; //头布局的title,
    @Column(nullable = false,length = 20)
    private String coverUrl;
    @Column(nullable = false,length = 20)
    private String movieName;
    @Column(nullable = false,length = 20)
    private String linkUrl;
    @Column(nullable = false,length = 20)
    private String playUrl; //播放Url

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
