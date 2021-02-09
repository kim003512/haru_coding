package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.Ranking;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.RankingRes_all;
import org.haru.haru_coding.model.SignUpReq;
import org.haru.haru_coding.model.UserChangeReq;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserMapper userMapper;
    private final S3FileUploadService s3FileUploadService;

    public UserService(final UserMapper userMapper, final S3FileUploadService s3FileUploadService) {
        log.info("유저서비스");
        this.userMapper = userMapper;
        this.s3FileUploadService= s3FileUploadService;
    }

    /**
     * 회원가입
     * @param user
     * @return
     */
    @Transactional
    public DefaultRes save(final User user){
        try {
            userMapper.save(user);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes save_user(SignUpReq signUpReq){
        try{
            if(signUpReq.getProfile() != null)
                signUpReq.setProfileUrl(s3FileUploadService.upload(signUpReq.getProfile()));

            userMapper.save_user(signUpReq);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 이름 중복 확인
     * @param name
     * @return
     */
    public DefaultRes findByName(final String name){
        final User user = userMapper.findByName(name);
        try{
            if(user==null){
                return DefaultRes.res(StatusCode.OK, ResponseMessage.POSSIBLE_NAME);
            } else {
                return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.ALREADY_USER);
            }
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes user_update_profile(final int userIdx, final MultipartFile profile) {
        if (userMapper.findByUidx(userIdx) != null) {
            try {
                String profileUrl = s3FileUploadService.upload(profile);

                userMapper.updateUserProfile(profileUrl);

                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_PROFILE);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
    }

    public DefaultRes update_user(final int userIdx, final UserChangeReq userChangeReq){
        UserChangeReq user = userMapper.findByUidx_userchange(userIdx);


        if(userMapper.findByUidx(userIdx) != null){
            if(userChangeReq.getName() != null){
                String name = userMapper.findByName_same(userChangeReq.getName());

                if(name != null) return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.ALREADY_USER);
            }
            if(userChangeReq.getName() == null) userChangeReq.setName(userMapper.findByUidx(userIdx).getName());
            if(userChangeReq.getPw() == null) userChangeReq.setPw(userMapper.findByUidx(userIdx).getPw());
            if(userChangeReq.getEmail() == null) userChangeReq.setEmail(userMapper.findByUidx(userIdx).getEmail());
            if(userChangeReq.getStar() == null) userChangeReq.setStar(String.valueOf(userMapper.findByUidx(userIdx).getStar()));

            userMapper.updateUser(userIdx, userChangeReq);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
        }
        return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
    }


    public DefaultRes findUser(final int userIdx){
        final User user = userMapper.findByUidx(userIdx);

        if(user != null){
            try{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user);
            }  catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 모든 유저 랭킹 조회
     * @return
     */
    @Transactional
    public DefaultRes<List<RankingRes_all>> RankingOfAllUsers() {
        List<RankingRes_all> rankingReAlls = userMapper.listOfAllRanking();

        if (rankingReAlls.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        else {
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, rankingReAlls);
        }
    }

    @Transactional
    public DefaultRes allUsersNum(){
        List<User> users = userMapper.allUserNum();

        if(users.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, users.size());
    }
}
