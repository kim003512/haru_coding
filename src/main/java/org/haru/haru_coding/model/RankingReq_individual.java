package org.haru.haru_coding.model;

import lombok.Data;
import java.sql.Date;

@Data
public class RankingReq_individual {
    private int userIdx;
    private int rankingnum;
    private String rankingdate;
    private String category;
}
