package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Ranking;
import org.haru.haru_coding.mapper.RankingMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.RankingReq_individual;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
@Slf4j
public class RankingService {
    private RankingMapper rankingMapper;

    public RankingService(RankingMapper rankingMapper){
        this.rankingMapper = rankingMapper;
    }

    @Transactional
    public DefaultRes register_ranking(final RankingReq_individual rankingReqIndividual){
        List<Ranking> rankingList = rankingMapper.ranking_category(rankingReqIndividual.getUserIdx(), rankingReqIndividual.getCategory());

        try{
            if(rankingList != null){
                if(rankingList.size() == 7){
                    rankingMapper.deleteRanking(rankingReqIndividual.getUserIdx(), rankingList.get(0).getRankingIdx());
                } else{
                    rankingMapper.save_ranking(rankingReqIndividual);
                }

                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_RANKING);
            } else{
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.NO_RANKING);
            }
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes get_ranking(final int userIdx, final String category){
        List<Ranking> rankingList = rankingMapper.ranking_category(userIdx, category);

        try{
            if(rankingList != null){
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_RANKING, rankingList);
            } else{
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.NO_RANKING);
            }
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
