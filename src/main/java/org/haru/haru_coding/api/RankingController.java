package org.haru.haru_coding.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.haru.haru_coding.service.RankingService;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RankingController {
    private RankingService rankingService;

    public RankingController(RankingService rankingService){
        this.rankingService = rankingService;
    }
}
