package com.winwang.movie.common;

public enum MovieTypeEnum {
    MOVIE_CONTENT(0),
    MOVIE_HEADER(1),
    MOVIE_BANNER(2),
    MOVIE_TAB(3);
    private int type;

    private MovieTypeEnum(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }
}
