package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.haru.haru_coding.dto.Ranking;
import org.haru.haru_coding.model.RankingReq_individual;
import java.sql.Date;

@Mapper
public interface RankingMapper {
    @Insert("INSERT INTO ranking(userIdx, rankingnum, rankingdate, category) VALUES(#{rankingReqIndividual.userIdx}, #{rankingReqIndividual.rankingnum}, #{rankingReqIndividual.rankingnum}, #{rankingReqIndividual.category})")
    int save_ranking(@Param("rankingReqIndividual") final RankingReq_individual rankingReqIndividual);

    @Select("SELECT * FROM ranking WHERE userIdx = #{userIdx} AND category = #{category} ORDER BY rankingdate")
    Ranking ranking_category(@Param("userIdx") final int userIdx, @Param("category") final String category);
}
