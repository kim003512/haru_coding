package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Problem;
import org.haru.haru_coding.mapper.QuestionMapper;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.AnsChangeReq;
import org.haru.haru_coding.model.DefaultRes;
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
//    @Transactional
//    public DefaultRes register_quest(final int userIdx, final Problem problem){
//        Problem exist_problem = questionMapper.findSameProblem(userIdx, problem.getQuest(), problem.getCategory());
//        int star = userMapper.getStar(userIdx);
//        int javaStar = userMapper.getJavaStar(userIdx);
//        int pythonStar = userMapper.getPythonStar(userIdx);
//        int cplusStar = userMapper.getCplusStar(userIdx);
//
//        try{
//            log.info("1");
//            if(exist_problem == null){
//                log.info("2");
//                if(problem.getCategory() == "java"){
//                    log.info("자바");
//                    if(problem.getAnswer() == 0) userMapper.save_javastar(javaStar+1, userIdx);
//                    else if(problem.getAnswer() == 1) userMapper.save_star(javaStar+3, userIdx);
//
//                    int javaStar_again = userMapper.getJavaStar(userIdx);
//
//                    userMapper.save_star(star+javaStar_again, userIdx);
//
//                    questionMapper.save_problem(problem);
//                    log.info("JAVA 문제를 추가하였습니다.");
//                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_QUESTION_JAVA);
//                } else if(problem.getCategory() == "python"){
//                    log.info("파이썬");
//                    if(problem.getAnswer() == 0) userMapper.save_pythonstar(pythonStar+1, userIdx);
//                    else if(problem.getAnswer() == 1) userMapper.save_pythonstar(pythonStar+3, userIdx);
//
//                    int pythonStar_again = userMapper.getPythonStar(userIdx);
//
//                    userMapper.save_star(star+pythonStar_again, userIdx);
//
//                    questionMapper.save_problem(problem);
//                    log.info("PYTHON 문제를 추가하였습니다.");
//                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_QUESTION_PYTHON);
//                } else if(problem.getCategory() == "cplus"){
//                    log.info("씨쁠");
//                    if(problem.getAnswer() == 0) userMapper.save_cplusstar(cplusStar+1, userIdx);
//                    else if(problem.getAnswer() == 1) userMapper.save_cplusstar(cplusStar+3, userIdx);
//
//                    int cplusStar_again = userMapper.getJavaStar(userIdx);
//
//                    userMapper.save_star(star+cplusStar_again, userIdx);
//
//                    questionMapper.save_problem(problem);
//                    log.info("CPLUS 문제를 추가하였습니다.");
//                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_QUESTION_CPLUS);
//                }
//                else{
//                    log.info("wrong 카테");
//                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.INSERT_WRONG_CATEGORY);
//                }
//            } else {
//                log.info("이미 등록된 문제");
//                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.ALREADY_QUESTION);
//            }
//        } catch (Exception e) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }

    @Transactional
    public DefaultRes save_problem(final int userIdx, final Problem problem){
        Problem sameProblem = questionMapper.findSameProblem(userIdx, problem.getQuest(), problem.getCategory());
        int getUserStar = userMapper.getStar(userIdx);
        int getJavaStar = userMapper.getJavaStar(userIdx);
        int getPythonStar = userMapper.getPythonStar(userIdx);
        int getCStar = userMapper.getCplusStar(userIdx);

        try{
            if(sameProblem == null){
                if(problem.getCategory().equals("java")){
                    log.info("자바 문제 등록");
                    if(problem.getAnswer() == 0) userMapper.save_javastar(getJavaStar+1, userIdx);
                    else if(problem.getAnswer() == 1) userMapper.save_javastar(getJavaStar+3, userIdx);

                    int getJavaStar_again = userMapper.getJavaStar(userIdx);

                    userMapper.save_star(getJavaStar_again+getPythonStar+getCStar, userIdx);
                    questionMapper.save_problem(problem);

                    return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_QUESTION_JAVA);
                } else if(problem.getCategory().equals("python")){
                    log.info("파이썬 문제 등록");
                    if(problem.getAnswer() == 0) userMapper.save_pythonstar(getPythonStar+1, userIdx);
                    else if(problem.getAnswer() == 1) userMapper.save_pythonstar(getPythonStar+3, userIdx);

                    int getPythonStar_again = userMapper.getPythonStar(userIdx);

                    userMapper.save_star(getJavaStar+getPythonStar_again+getCStar, userIdx);
                    questionMapper.save_problem(problem);

                    return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_QUESTION_PYTHON);
                } else if(problem.getCategory().equals("cplus")){
                    log.info("씨 문제 등록");
                    if(problem.getAnswer() == 0) userMapper.save_cplusstar(getCStar+1, userIdx);
                    else if(problem.getAnswer() == 1) userMapper.save_cplusstar(getCStar+3, userIdx);

                    int getCStar_again = userMapper.getCplusStar(userIdx);

                    userMapper.save_star(getJavaStar+getPythonStar+getCStar_again, userIdx);
                    questionMapper.save_problem(problem);

                    return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_QUESTION_CPLUS);
                } else{
                    log.info("올바르지 않은 카테고리");
                    return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.INSERT_WRONG_CATEGORY);
                }
            } else{
                log.info("문제 등록 실패_이미 존재하는 문제");
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_QUESTION);
            }
        } catch (Exception e){
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
