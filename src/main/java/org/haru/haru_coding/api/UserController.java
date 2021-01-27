package org.haru.haru_coding.api;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.RankingRes;
import org.haru.haru_coding.model.SignUpReq;
import org.haru.haru_coding.model.UserChangeReq;
import org.haru.haru_coding.service.JwtService;
import org.haru.haru_coding.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.haru.haru_coding.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(final UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입(프로필없는 타입)
     */
//    @PostMapping("user/signup")
//    public ResponseEntity signup(@RequestBody final User user){
//        try{
//            return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
//        } catch (Exception e){
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * 회원가입
     * @param signUpReq
     * @param profile
     * @return
     */
    @PostMapping("user/signUp")
    public ResponseEntity signup_profile(
            SignUpReq signUpReq,
            @RequestPart(value = "profile", required = false) final MultipartFile profile){
        try{
            //파일을 signUpReq에 저장
            if(profile != null) signUpReq.setProfile(profile);
            return new ResponseEntity<>(userService.save_user(signUpReq), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 이름 중복 확인
     * @param name
     * @return
     */
    @PostMapping("user/check/{name}")
    public ResponseEntity nameCheck(@PathVariable(value = "name") final String name){
        DefaultRes defaultRes = userService.findByName(name);
        return new ResponseEntity<>(defaultRes, HttpStatus.OK);
    }

    /**
     * 프로필 수정
     * @param token
     * @param profile
     * @return
     */
    @PutMapping("userprofile")
    public ResponseEntity update_user_profile(
            @RequestHeader(value = "Authorization") String token,
            @RequestPart(value = "profile", required = false) final MultipartFile profile){
        try{
            int userIdx = jwtService.decode(token).getUser_idx();

            return new ResponseEntity<>(userService.user_update_profile(userIdx, profile), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 수정
     * @param token
     * @param userChangeReq
     * @return
     */
    @PutMapping("user")
    public ResponseEntity update_user(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody final UserChangeReq userChangeReq){
        try{
            int userIdx = jwtService.decode(token).getUser_idx();

            return new ResponseEntity<>(userService.update_user(userIdx, userChangeReq), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 모든 유저 랭킹 조회
     * @return
     */
    @GetMapping("user/allranking")
    public ResponseEntity getAllUserRaking(){
        try{
            log.info("모든 유저 랭킹 조회 성공");
            DefaultRes<List<RankingRes>> defaultRes = userService.RankingOfAllUsers();

            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("모든 유저 랭킹 조회 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 전체 프로그래머 수 조회
     * @return
     */
    @GetMapping("user/usernum")
    public ResponseEntity getAllUsersNum(){
        try{
            log.info("모든 유저 수 조회 성공");
            DefaultRes num = userService.allUsersNum();

            return new ResponseEntity<>(num, HttpStatus.OK);
        } catch (Exception e){
            log.info("모든 유저 수 조회 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
