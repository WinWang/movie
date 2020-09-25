package com.winwang.movie.respository;

import com.winwang.movie.pojo.live.LiveTypeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveRepository extends JpaRepository<LiveTypeBean, String> {


}
