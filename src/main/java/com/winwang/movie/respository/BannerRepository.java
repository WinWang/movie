package com.winwang.movie.respository;

import com.winwang.movie.pojo.movie.BannerBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<BannerBean, Integer> {






}
