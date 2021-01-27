package org.haru.haru_coding.api;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Problem;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.mapper.QuestionMapper;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.AnsChangeReq;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.service.JwtService;
import org.haru.haru_coding.service.QuestionService;
import org.haru.haru_coding.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.haru.haru_coding.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class QuestionController {
    private UserService userService;
    private final JwtService jwtService;
    private QuestionService questionService;
    private UserMapper userMapper;
    private QuestionMapper questionMapper;

    public QuestionController(UserService userService, JwtService jwtService, QuestionService questionService, UserMapper userMapper, QuestionMapper questionMapper){
        this.userService = userService;
        this.jwtService = jwtService;
        this.questionService = questionService;
        this.userMapper = userMapper;
        this.questionMapper = questionMapper;
    }

    /**
     * 문제 등록
     * @param token
     * @param problem
     * @return
     */
    @PostMapping("question")
    public ResponseEntity register_quest(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody final Problem problem){
        int userIdx = jwtService.decode(token).getUser_idx();
        User user = userMapper.findByUidx(userIdx); //존재하는 유저인지 확인

        if(user != null){
            try{
                problem.setUserIdx(userIdx);
                return new ResponseEntity<>(questionService.register_quest(userIdx, problem), HttpStatus.OK);
            } catch (Exception e){
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 정답 상태 변경
     * @param token
     * @param ansChangeReq
     * @return
     */
    @PostMapping("changeanswer")
    public ResponseEntity answer_change(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody final AnsChangeReq ansChangeReq){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                ansChangeReq.setUserIdx(userIdx);
                return new ResponseEntity<>(questionService.answer_change(ansChangeReq), HttpStatus.OK);
            } catch (Exception e){
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 오답노트(category 입력)
     * @param token
     * @return
     */
    @GetMapping("wrongassort")
    public ResponseEntity viewAllUserProblem_category(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody final String category){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                DefaultRes<List<Problem>> defaultRes = questionService.viewAllUserProblem_category(userIdx, category);
                return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            } catch (Exception e){
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 오답노트_전체
     * @param token
     * @return
     */
    @GetMapping("wrong")
    public ResponseEntity viewAllUserProblem(@RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                return new ResponseEntity<>(questionService.viewAllUserProblem(userIdx), HttpStatus.OK);
            } catch (Exception e){
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

}
