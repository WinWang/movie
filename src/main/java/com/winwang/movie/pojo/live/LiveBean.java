package com.winwang.movie.pojo.live;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@Table(name = "live_table")
public class LiveBean {

    @Id
    private String liveName;

    @Column(nullable = false, length = 200)
    private String liveLink;

}
