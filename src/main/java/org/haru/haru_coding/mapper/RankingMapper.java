package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.Ranking;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.model.RankingReq_individual;
import org.haru.haru_coding.model.RankingRes_all;
import org.haru.haru_coding.model.RankingRes_individual;

import java.util.Date;
import java.util.List;

@Mapper
public interface RankingMapper {
//
//    @Select("SELECT userIdx FROM user WHERE category = #{category} ORDER BY star DESC")
//    List<Integer> listOfRanking(@Param("category") final String category);

    @Select("SELECT userIdx FROM user ORDER BY javastar DESC")
    List<Integer> RankingOfJava();

    @Select("SELECT userIdx FROM user ORDER BY pythonstar DESC")
    List<Integer> RankingOfPython();

    @Select("SELECT userIdx FROM user ORDER BY cplusstar DESC")
    List<Integer> RankingOfCplus();

    @Insert("INSERT INTO ranking(userIdx, rankingnum, rankingdate, category) VALUES(#{userIdx}, #{rankingnum}, #{rankingdate}, #{category})")
    int save_ranking(@Param("userIdx") final int userIdx, @Param("rankingnum") final int rankingnum, @Param("rankingdate") final String rankingdate, @Param("category") final String category);

    @Select("SELECT * FROM ranking WHERE userIdx = #{userIdx} AND category = #{category} ORDER BY rankingdate")
    List<Ranking> ranking_category(@Param("userIdx") final int userIdx, @Param("category") final String category);

    @Delete("DELETE FROM ranking WHERE userIdx = #{userIdx} AND rankingIdx = #{rankingIdx}")
    void deleteRanking(@Param("userIdx") final int userIdx, @Param("rankingIdx") final int rankingIdx);

    @Select("SELECT userIdx FROM user")
    List<Integer> allUserIdx();
}
