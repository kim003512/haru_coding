package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Problem;
import org.haru.haru_coding.mapper.QuestionMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
@Slf4j
public class QuestionService {
    private QuestionMapper questionMapper;

    public QuestionService(QuestionMapper questionMapper){
        this.questionMapper = questionMapper;
    }

//    @Transactional
//    public DefaultRes enroll(final int userIdx, final int question, final int check){
//        try{
//            questionMapper.save_question(userIdx, question, check);
//            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_QUESTION);
//        }  catch (Exception e) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }

    @Transactional
    public DefaultRes register_quest(final Problem problem){
        try{
            questionMapper.save_problem(problem);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_QUESTION);
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

    @Transactional
    public DefaultRes answer_change(final int userIdx, final int quest, final int answer){
        try{
            questionMapper.change_answer(userIdx, quest, answer);

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CHANGE_ANWSER);
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes viewAllUserProblem(final int userIdx){
        List<Problem> problemList = questionMapper.viewAllUserProblem(userIdx);

        if(problemList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_QUESTION);
        else
            return DefaultRes.res(StatusCode.OK, ResponseMessage.FIND_QUESTION, problemList);
    }
}
