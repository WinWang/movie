package com.winwang.movie.respository;

import com.winwang.movie.pojo.movie.PlayBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDetailRepository extends JpaRepository<PlayBean, String> {
}
