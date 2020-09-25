package com.winwang.movie.controller;


import com.winwang.movie.pojo.movie.MovieBean;
import com.winwang.movie.pojo.movie.PlayBean;
import com.winwang.movie.pojo.base.ResObject;
import com.winwang.movie.pojo.Test;
import com.winwang.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Autowired
    MovieService movieService;

    /**
     * 获取首页数据接口（首页电影）
     *
     * @return
     */
    @GetMapping("")
    public ResObject getMovieHome() {
        List<MovieBean> homeDataByDB = movieService.getHomeDataByDB();
        if (homeDataByDB != null && homeDataByDB.size() > 0) {
            ResObject<Object> res = ResObject.successResult();
            res.setResult(homeDataByDB);
            return res;
        } else {
            return movieService.getHomeData();
        }
    }

    /**
     * 获取单个视频详情
     *
     * @param path
     * @return
     */
    @PostMapping("/detail")
    public ResObject detailMovie(@RequestParam String path) {
        PlayBean videoDetailByDB = movieService.getVideoDetailByDB(path);
        if (videoDetailByDB != null) {
            ResObject<Object> resObject = ResObject.successResult();
            resObject.setResult(videoDetailByDB);
            return resObject;
        } else {
            return movieService.getVideoDetail(path);
        }
    }


    @GetMapping("/test")
    public List<Test> tests() {
        return movieService.test();
    }


}
