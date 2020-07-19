package com.winwang.movie.respository;

import com.winwang.movie.pojo.PlayListBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListRepository extends JpaRepository<PlayListBean, String> {

    @Query(value = "select * from playlist_table  where video_paht = :path", nativeQuery = true)
    public List<PlayListBean> findPlayListBeanByVideoPaht(@Param("path") String path);

}
