package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.Problem;

import java.util.List;

@Mapper
public interface QuestionMapper {
//    @Insert("INSERT INTO quest(userIdx, question, check) VALUES (#{userIdx}, #{question}, #{check})")
//    int save_question(@Param("userIdx") final int userIdx, @Param("question") final int question, @Param("check") final int check);

    @Insert("INSERT INTO problem(userIdx, quest, answer) VALUES (#{problem.userIdx}, #{problem.quest}, #{problem.answer})")
    int save_problem(@Param("problem") final Problem problem);

    @Select("SELECT * FROM problem WHERE quest = #{quest}")
    Problem findProblemByQuest(@Param("quest") final int quest);

    @Update("UPDATE register_like set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    @Update("UPDATE problem set answer = #{answer} WHERE userIdx=#{userIdx} AND quest=#{quest}")
    int change_answer(@Param("userIdx") final int userIdx, @Param("quest") final int quest, @Param("answer") final int answer);

    @Select("SELECT * FROM problem WHERE userIdx = #{userIdx}")
    List<Problem> viewAllUserProblem(@Param("userIdx") final int userIdx);
}
