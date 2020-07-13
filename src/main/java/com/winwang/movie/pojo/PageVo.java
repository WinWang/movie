package com.winwang.movie.pojo;

import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;

import java.util.List;

@PageSelect(cssQuery = "#hot_1 .section-list")
public class PageVo {
    @PageFieldSelect(cssQuery = ".list_mov_home")
    private String title;

    @PageFieldSelect(cssQuery = ".list_mov_home")
    private List<String> list;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "title='" + title + '\'' +
                ", list=" + list +
                '}';
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
