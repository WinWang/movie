package com.winwang.movie.controller;


import com.winwang.movie.pojo.base.ResObject;
import com.winwang.movie.service.LiveTVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 电视直播处理Controller
 */

@Slf4j
@RequestMapping("/livetv")
@RestController
public class LiveTVController {

    @Autowired
    LiveTVService liveTVService;

    @GetMapping("/livelist")
    private ResObject getLiveListHome() {
        return liveTVService.getLiveTvList();
    }

    @PostMapping("/detail")
    private ResObject getTvDetail(@RequestParam String path) {
        return liveTVService.getLiveTVDetail(path);
    }


}
