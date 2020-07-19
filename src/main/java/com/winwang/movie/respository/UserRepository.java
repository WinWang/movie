package com.winwang.movie.respository;

import com.winwang.movie.pojo.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


public interface UserRepository extends JpaRepository<Test, Integer> {




}
