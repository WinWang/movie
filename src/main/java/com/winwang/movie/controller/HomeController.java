package com.winwang.movie.controller;


import com.winwang.movie.pojo.MovieBean;
import com.winwang.movie.pojo.PlayBean;
import com.winwang.movie.pojo.ResObject;
import com.winwang.movie.pojo.Test;
import com.winwang.movie.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Autowired
    HomeService homeService;

    /**
     * 获取首页数据接口（首页电影）
     *
     * @return
     */
    @GetMapping("")
    public ResObject getMovieHome() {
        List<MovieBean> homeDataByDB = homeService.getHomeDataByDB();
        if (homeDataByDB != null && homeDataByDB.size() > 0) {
            ResObject<Object> res = ResObject.successResult();
            res.setResult(homeDataByDB);
            return res;
        } else {
            return homeService.getHomeData();
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
        PlayBean videoDetailByDB = homeService.getVideoDetailByDB(path);
        if (videoDetailByDB != null) {
            ResObject<Object> resObject = ResObject.successResult();
            resObject.setResult(videoDetailByDB);
            return resObject;
        } else {
            return homeService.getVideoDetail(path);
        }
    }


    @GetMapping("/test")
    public List<Test> tests() {
        return homeService.test();
    }


}
