package com.winwang.movie.pojo.live;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "live_type")
public class LiveTypeBean {

    @Id
    private String liveTypeName;

    @Embedded
    private List<LiveBean> liveList;


}
