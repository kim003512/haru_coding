package org.haru.haru_coding.api;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.LoginReq;
import org.haru.haru_coding.service.AuthService;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    private static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    private final AuthService authService;
    private final UserMapper userMapper;

    public LoginController(final AuthService authService, final UserMapper userMapper) {
        log.info("로그인 컨트롤러");
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("user/signin")
    public ResponseEntity login(@RequestBody final LoginReq loginReq){
        try{
            log.info(loginReq.toString());
            return new ResponseEntity<>(authService.login(loginReq), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
