package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Problem;
import org.haru.haru_coding.mapper.QuestionMapper;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.AnsChangeReq;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.WrongRes;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class QuestionService {
    private QuestionMapper questionMapper;
    private UserMapper userMapper;

    public QuestionService(QuestionMapper questionMapper, UserMapper userMapper){
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
    }

    /**
     * 문제 등록하기
     * @param problem
     * @return
     */
    @Transactional
    public DefaultRes register_quest(final int userIdx, final Problem problem){
        Problem exist_problem = questionMapper.findSameProblem(userIdx, problem.getQuest(), problem.getCategory());
        int star = userMapper.getStar(userIdx);

        try{
            if(exist_problem == null){
                if(problem.getAnswer() == 0) userMapper.save_star(star+1, userIdx);
                else if(problem.getAnswer() == 1) userMapper.save_star(star+3, userIdx);

                questionMapper.save_problem(problem);
                log.info("문제를 추가하였습니다.");
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_QUESTION);
            } else {
                log.info("이미 등록된 문제");
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.ALREADY_QUESTION);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes findProblem(final int quest){
        final Problem problem = questionMapper.findProblemByQuest(quest);

        if(problem != null){
            try{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.FIND_QUESTION);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NO_QUESTION);
    }

    /**
     * 정답 상태 변경
     * @param ansChangeReq
     * @return
     */
    @Transactional
    public DefaultRes answer_change(final AnsChangeReq ansChangeReq){
        Problem exist_problem = questionMapper.findSameProblem(ansChangeReq.getUserIdx(), ansChangeReq.getQuest(), ansChangeReq.getCategory());

        try{
            if(exist_problem != null){
                log.info("정답 상태 변경");
                questionMapper.change_answer(ansChangeReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CHANGE_ANWSER);
            } else{
                log.info("정답 상태 변경 실패");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_QUESTION);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 오답 노트 조회_category별
     * @param userIdx
     * @param category
     * @return
     */
    @Transactional
    public DefaultRes viewAllUserProblem_category(final int userIdx, final String category){
        List<Problem> problemList = questionMapper.viewAllUserProblem_category(userIdx, category);

        if(problemList.isEmpty()){
            log.info("회원 오답 조회 실패_카테고리별");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_QUESTION);
        } else {
            log.info("회원 오답 조회 성공_카테고리별");
            return DefaultRes.res(StatusCode.OK, ResponseMessage.FIND_QUESTION, problemList);
        }
    }

    /**
     * 오답 노트 전회_전체
     * @param userIdx
     * @return
     */
    @Transactional
    public DefaultRes viewAllUserProblem(final int userIdx){
        List<Integer> problemList = questionMapper.viewAllUserProblem(userIdx);

        List<Integer> list = new ArrayList<Integer>();

        for(int i = 0; i < problemList.size(); i++){
            list.add(problemList.get(i));
        }

        if(problemList == null){
            log.info("회원 오답 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_QUESTION);
        } else{
            log.info("회원 오답 조회 성공");
            return DefaultRes.res(StatusCode.OK, ResponseMessage.FIND_QUESTION, list);
        }
    }
}
