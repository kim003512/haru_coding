package org.haru.haru_coding.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.RankingReq_individual;
import org.haru.haru_coding.service.JwtService;
import org.haru.haru_coding.service.RankingService;
import org.haru.haru_coding.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.haru.haru_coding.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class RankingController {
    private RankingService rankingService;
    private UserService userService;
    private final JwtService jwtService;

    public RankingController(RankingService rankingService, UserService userService, JwtService jwtService){
        this.rankingService = rankingService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

//    @PostMapping("ranking")
//    public ResponseEntity ranking_registe(
//            @RequestHeader(value = "Authorization") String token,
//            @RequestBody final RankingReq_individual rankingReqIndividual){
//        int userIdx = jwtService.decode(token).getUser_idx();
//        DefaultRes user = userService.findUser(userIdx);
//
//        if(user != null){
//            try{
//                rankingReqIndividual.setUserIdx(userIdx);
//                return new ResponseEntity<>(rankingService.register_ranking(rankingReqIndividual), HttpStatus.OK);
//            } catch (Exception e){
//                log.error(e.getMessage());
//                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else{
//            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    @GetMapping("ranking")
//    public ResponseEntity get_ranking
//            (@RequestHeader(value = "Authorization") String token, @RequestBody final String category){
//        int userIdx = jwtService.decode(token).getUser_idx();
//        DefaultRes user = userService.findUser(userIdx);
//
//        if(user != null){
//            try{
//                return new ResponseEntity<>(rankingService.get_ranking(userIdx, category), HttpStatus.OK);
//            } catch (Exception e){
//                log.error(e.getMessage());
//                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else{
//            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//        }
//    }

    @PutMapping("ranking")
    public ResponseEntity set_ranking(){
        return new ResponseEntity<>(rankingService.set_ranking(), HttpStatus.OK);
    }

    @GetMapping("ranking")
    public ResponseEntity get_ranking(@RequestHeader(value = "Authorization") String token, @RequestBody final String category){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user != null){
            try{
                return new ResponseEntity<>(rankingService.get_ranking(userIdx, category), HttpStatus.OK);
            }catch (Exception e){
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }
}
