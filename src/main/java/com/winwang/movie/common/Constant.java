package com.winwang.movie.common;

public interface Constant {
    /**
     * 抓取电影的网站地址
     */
    String MOVIE_URL = "http://www.27k.cc";

    /**
     * 电视直播网站地址
     */
    String LIVE_TV_URL = "http://www.hao5.net/";

    /**
     * 请求成功结果码
     */
    int CODE_SUCCESS = 200;

    /**
     * 请求失败结果码
     */
    int CODE_FAILURE = -1;

    String MSG_SUCCESS = "请求成功";
    String MSG_FAILURE = "请求失败";

}
