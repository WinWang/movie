package com.winwang.movie.pojo.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "banner")
public class BannerBean {
    @Id
    @GeneratedValue()
    private Integer id;
    @Column(nullable = false, length = 200)
    private String coverUrl;
    @Column(nullable = false, length = 50)
    private String movieName;
    @Column(nullable = false, length = 200)
    private String linkUrl;

}
