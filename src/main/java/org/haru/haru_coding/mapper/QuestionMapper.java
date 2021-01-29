package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.Problem;
import org.haru.haru_coding.model.AnsChangeReq;
import org.haru.haru_coding.model.WrongRes;

import java.util.List;

@Mapper
public interface QuestionMapper {
    /**
     * 문제 등록
     * @param problem
     * @return
     */
    @Insert("INSERT INTO problem(userIdx, quest, answer, category) VALUES (#{problem.userIdx}, #{problem.quest}, #{problem.answer}, #{problem.category})")
    int save_problem(@Param("problem") final Problem problem);

    /**
     * 문제 번호로 문제 찾기
     * @param quest
     * @return
     */
    @Select("SELECT * FROM problem WHERE quest = #{quest}")
    Problem findProblemByQuest(@Param("quest") final int quest);

    /**
     * 정답 상태 변경
     * @param ansChangeReq
     * @return
     */
    @Update("UPDATE problem set answer = #{ansChangeReq.answer} WHERE userIdx=#{ansChangeReq.userIdx} AND quest=#{ansChangeReq.quest} AND category=#{ansChangeReq.category}")
    int change_answer(@Param("ansChangeReq") final AnsChangeReq ansChangeReq);


    /**
     * 사용자가 푼 오답노트 리스트
     * @param userIdx
     * @return
     */
    @Select("SELECT * FROM problem WHERE userIdx = #{userIdx} AND category = #{category}")
    List<Problem> viewAllUserProblem_category(@Param("userIdx") final int userIdx, @Param("category") final String category);

//    @Select("SELECT * FROM problem WHERE userIdx = #{userIdx}")
//    List<Problem> viewAllUserProblem(@Param("userIdx") final int userIdx);

    @Select("SELECT questIdx FROM problem WHERE userIdx = #{userIdx}")
    List<Integer> viewAllUserProblem(@Param("userIdx") final int userIdx);

//    @Select("SELECT * FROM problem WHERE userIdx = #{userIdx}")
//    WrongRes viewAllUserProblem(@Param("userIdx") final int userIdx);

    /**
     * 같은 문제가 등록되어 있는지 확인
     * @param userIdx
     * @param quest
     * @param category
     * @return
     */
    @Select("SELECT * FROM problem WHERE userIdx = #{userIdx} AND quest = #{quest} AND category = #{category}")
    Problem findSameProblem(@Param("userIdx") final int userIdx, @Param("quest") final int quest, @Param("category") final String category);
}
