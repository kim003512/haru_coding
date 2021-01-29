package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.Ranking;
import org.haru.haru_coding.model.RankingReq_individual;
import java.sql.Date;
import java.util.List;

@Mapper
public interface RankingMapper {
    @Insert("INSERT INTO ranking(userIdx, rankingnum, rankingdate, category) VALUES(#{rankingReqIndividual.userIdx}, #{rankingReqIndividual.rankingnum}, #{rankingReqIndividual.rankingnum}, #{rankingReqIndividual.category})")
    int save_ranking(@Param("rankingReqIndividual") final RankingReq_individual rankingReqIndividual);

    @Select("SELECT * FROM ranking WHERE userIdx = #{userIdx} AND category = #{category} ORDER BY rankingdate")
    List<Ranking> ranking_category(@Param("userIdx") final int userIdx, @Param("category") final String category);

    @Delete("DELETE FROM ranking WHERE userIdx = #{userIdx} AND rankingIdx = #{rankingIdx}")
    void deleteRanking(@Param("userIdx") final int userIdx, @Param("rankingIdx") final int rankingIdx);
}
