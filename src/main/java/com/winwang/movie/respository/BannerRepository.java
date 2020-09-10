package com.winwang.movie.respository;

import com.winwang.movie.pojo.BannerBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.Transient;

@Repository
public interface BannerRepository extends JpaRepository<BannerBean, Integer> {






}
