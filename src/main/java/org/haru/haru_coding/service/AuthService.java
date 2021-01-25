package org.haru.haru_coding.service;


import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.LoginReq;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {


    private final UserMapper userMapper;

    private final JwtService jwtService;

    public AuthService(final UserMapper userMapper, JwtService jwtService) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 서비스
     *
     * @param loginReq 로그인 객체
     * @return DefaultRes
     */
    public DefaultRes<JwtService.TokenRes> login(final LoginReq loginReq) {
        final User user = userMapper.findByIdAndPassword(loginReq); //아이디 비번 체크 한후 유저 정보 통째로 가져오기
        final User user_id = userMapper.findByName(loginReq.getName()); //
        final User user_pw = userMapper.findByName(loginReq.getPw());
        log.info("2");
        if (user != null) {
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getUserIdx()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        } else if(user_id == null){
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        } else if(user_pw == null){
            return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.WRONG_PASSWORD);
        }
        log.info("들어옴");
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }
}

