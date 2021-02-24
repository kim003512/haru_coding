package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Ranking;
import org.haru.haru_coding.mapper.RankingMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RankingService {
    private RankingMapper rankingMapper;

    public RankingService(RankingMapper rankingMapper){
        this.rankingMapper = rankingMapper;
    }

//    @Transactional
//    public DefaultRes register_ranking(final RankingReq_individual rankingReqIndividual){
//        List<Ranking> rankingList = rankingMapper.ranking_category(rankingReqIndividual.getUserIdx(), rankingReqIndividual.getCategory());
//
//        try{
//            if(rankingList != null){
//                if(rankingList.size() == 7){
//                    rankingMapper.deleteRanking(rankingReqIndividual.getUserIdx(), rankingList.get(0).getRankingIdx());
//                } else{
//                    rankingMapper.save_ranking(rankingReqIndividual);
//                }
//
//                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_RANKING);
//            } else{
//                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.NO_RANKING);
//            }
//        } catch (Exception e){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }
//
//    @Transactional
//    public DefaultRes get_ranking(final int userIdx, final String category) {
//        //List<Ranking> rankingList = rankingMapper.ranking_category(userIdx, category);
//        List<Integer> rankingList = rankingMapper.listOfRanking(category);
//
//
//        try {
//            if (rankingList != null) {
//                for (int i = 1; i <= rankingList.size(); i++) {
//                    List<Integer> ranking = new ArrayList<>(i);
//                    ranking.add(i);
//
//                    ArrayList<Integer>[][] myRanking = new ArrayList[rankingList.size()][ranking.size()];
//                    for(int j = 1; j <= ranking.size(); j++){
//                        myRanking[i][j].add(rankingList.get(i), ranking.get(j));
//
//                        log.info(String.valueOf(myRanking));
//                    }
//                }
//                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_RANKING, rankingList);
//            } else {
//                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.NO_RANKING);
//            }
//        } catch (Exception e) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public DefaultRes set_ranking(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String now_dt = format.format(now);

        List<Integer> userIdxList = rankingMapper.allUserIdx();

        try{
            for(int i=0; i < userIdxList.size(); i++){
                List<Ranking> rankingList_java = rankingMapper.ranking_category(userIdxList.get(i), "java");
                List<Ranking> rankingList_python = rankingMapper.ranking_category(userIdxList.get(i), "python");
                List<Ranking> rankingList_cplus = rankingMapper.ranking_category(userIdxList.get(i), "cplus");

                if(rankingList_java.size() == 7){
                    int rankingIdx = rankingList_java.get(0).getRankingIdx();
                    rankingMapper.deleteRanking(userIdxList.get(i), rankingIdx);
                } else{
                    rankingMapper.save_ranking(userIdxList.get(i), Integer.parseInt(get_ranking(userIdxList.get(i), "java").getData().toString()), now_dt, "java");
                }

                if(rankingList_python.size() == 7){
                    int rankingIdx = rankingList_python.get(0).getRankingIdx();
                    rankingMapper.deleteRanking(userIdxList.get(i), rankingIdx);
                } else{
                    rankingMapper.save_ranking(userIdxList.get(i), Integer.parseInt(get_ranking(userIdxList.get(i), "python").getData().toString()), now_dt, "python");
                }

                if(rankingList_cplus.size() == 7){
                    int rankingIdx = rankingList_cplus.get(0).getRankingIdx();
                    rankingMapper.deleteRanking(userIdxList.get(i), rankingIdx);
                } else{
                    rankingMapper.save_ranking(userIdxList.get(i), Integer.parseInt(get_ranking(userIdxList.get(i), "cplus").getData().toString()), now_dt, "cplus");
                }
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_RANKING);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes get_ranking(final int userIdx, final String category) {
//        List<Ranking> rankingList_java = rankingMapper.ranking_category(userIdx, category);
//        List<Ranking> rankingList_python = rankingMapper.ranking_category(userIdx, category);
//        List<Ranking> rankingList_cplus = rankingMapper.ranking_category(userIdx, category);
        List<Integer> rankingOfJava = rankingMapper.RankingOfJava();
        List<Integer> rankingOfPython = rankingMapper.RankingOfPython();
        List<Integer> rankingOfCplus = rankingMapper.RankingOfCplus();

        int myRankingJava = 0;
        int myRankingPython = 0;
        int myRankingCplus = 0;

        List<Integer> ranking_Java = new ArrayList<>();
        List<Integer> ranking_Python = new ArrayList<>();
        List<Integer> ranking_Cplus = new ArrayList<>();

        try{
            if(category == "java"){
                for (int i = 0; i < rankingOfJava.size(); i++) {
                    ranking_Java.add(rankingOfJava.get(i));
                    myRankingJava = ranking_Java.indexOf(userIdx);
                }
                return DefaultRes.res(StatusCode.OK, ResponseMessage.GET_RANKING_JAVA, myRankingJava+1);
            } else if(category == "python"){
                for (int i = 0; i < rankingOfPython.size(); i++) {
                    ranking_Python.add(rankingOfPython.get(i));
                    myRankingPython = ranking_Python.indexOf(userIdx);
                }
                return DefaultRes.res(StatusCode.OK, ResponseMessage.GET_RANKING_PYTHON, myRankingPython+1);
            } else if(category == "cplus"){
                for (int i = 0; i < rankingOfCplus.size(); i++) {
                    ranking_Cplus.add(rankingOfCplus.get(i));
                    myRankingCplus = ranking_Cplus.indexOf(userIdx);
                }
                return DefaultRes.res(StatusCode.OK, ResponseMessage.GET_RANKING_C, myRankingCplus+1);
            } else{
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.INSERT_WRONG_CATEGORY);
            }
        }  catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
