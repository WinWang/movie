package com.winwang.movie.respository;

import com.winwang.movie.pojo.MovieBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<MovieBean, Integer> {

}
