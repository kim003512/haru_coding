package org.haru.haru_coding.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class Ranking {
    private int userIdx;
    private int rankingIdx;
    private int rankingnum;
    private String rankingdate;
    private String category;
}
