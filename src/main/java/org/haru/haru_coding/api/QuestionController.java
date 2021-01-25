package org.haru.haru_coding.api;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Problem;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.service.JwtService;
import org.haru.haru_coding.service.QuestionService;
import org.haru.haru_coding.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.haru.haru_coding.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class QuestionController {
    private UserService userService;
    private final JwtService jwtService;
    private QuestionService questionService;
    private UserMapper userMapper;

    public QuestionController(UserService userService, JwtService jwtService, QuestionService questionService, UserMapper userMapper){
        this.userService = userService;
        this.jwtService = jwtService;
        this.questionService = questionService;
        this.userMapper = userMapper;
    }

//    @PostMapping("question/{question}")
//    public ResponseEntity question_enroll(
//            @RequestHeader(value = "Authorization") String token,
//            @PathVariable(value = "question") final int question){
//        int userIdx = jwtService.decode(token).getUser_idx();
//
//        User user = userMapper.findByUidx(userIdx);
//
//        if(user != null){
//            try{
//                return new ResponseEntity<>(questionService.enroll(userIdx, question, 0), HttpStatus.OK);
//            } catch (Exception e){
//                log.error(e.getMessage());
//                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//        }
//    }

    @PostMapping("question")
    public ResponseEntity register_quest(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody final Problem problem){
        int userIdx = jwtService.decode(token).getUser_idx();

        User user = userMapper.findByUidx(userIdx);


        if(user != null){
            try{
                problem.setAnswer(0);
                problem.setUserIdx(userIdx);
                return new ResponseEntity<>(questionService.register_quest(problem), HttpStatus.OK);
            } catch (Exception e){
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("question/{quest}/{answer}")
    public ResponseEntity answer_change(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "quest") final int quest,
            @PathVariable(value = "answer") final int answer){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);
        DefaultRes problem = questionService.findProblem(quest);

        if(user.getStatus() == 200 && problem.getStatus() == 200){
            try{
                log.info("정답 상태 변경");
                return new ResponseEntity<>(questionService.answer_change(userIdx, quest, answer), HttpStatus.OK);
            } catch (Exception e){
                log.info("정답 상태 변경 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("wrongnote")
    public ResponseEntity viewAllUserProblem(
            @RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("회원 오답 조회 성공");
                return new ResponseEntity<>(questionService.viewAllUserProblem(userIdx), HttpStatus.OK);
            } catch (Exception e){
                log.info("회원 오답 조회 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }
}
