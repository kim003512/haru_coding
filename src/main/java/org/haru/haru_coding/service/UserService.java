package org.haru.haru_coding.service;

import lombok.extern.slf4j.Slf4j;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.dto.UserNotProfile;
import org.haru.haru_coding.mapper.UserMapper;
import org.haru.haru_coding.model.DefaultRes;
import org.haru.haru_coding.model.SignUpReq;
import org.haru.haru_coding.utils.ResponseMessage;
import org.haru.haru_coding.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public DefaultRes user_update(final int userIdx, final User user, final MultipartFile profile) {
        if (userMapper.findByUidx(userIdx) != null) {
            try {
                User myUser = userMapper.findByUidx(userIdx);

                if(myUser == null)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);



                if(user.getName() != null) myUser.setName(user.getName());
                if(user.getEmail() != null) myUser.setEmail(user.getEmail());
                if(user.getProfileUrl() != null){
                    myUser.setProfileUrl(s3FileUploadService.upload(user.getProfile()));

                    userMapper.updateUser(userIdx, myUser);

                    return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
                }

            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
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
    public DefaultRes<List<User>> RankingOfAllUsers(){
        List<User> userList = userMapper.listOfAllRanking();

        if(userList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
    }

    @Transactional
    public DefaultRes allUsersNum(){
        List<User> users = userMapper.allUserNum();

        if(users.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, users.size());
    }
}