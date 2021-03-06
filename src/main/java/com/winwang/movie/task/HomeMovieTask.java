package com.winwang.movie.task;


import com.winwang.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling

public class HomeMovieTask {


    @Autowired
    MovieService movieService;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
//    @Scheduled(fixedDelay = 1000 * 60)
    private void fetchHomeMovieList() {

        movieService.getHomeData();
    }
}
