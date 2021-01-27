package org.haru.haru_coding.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class Ranking {
    private int userIdx;
    private int rankingIdx;
    private int rankingnum;
    private Date rankingdate;
    private String category;
}
