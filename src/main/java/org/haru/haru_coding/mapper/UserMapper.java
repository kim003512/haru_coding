package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.dto.UserNotProfile;
import org.haru.haru_coding.model.LoginReq;
import org.haru.haru_coding.model.RankingRes;
import org.haru.haru_coding.model.SignUpReq;
import org.haru.haru_coding.model.UserChangeReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 회원가입(프로필 x)
     * @param user
     * @return
     */
    @Insert("INSERT INTO user(name, pw, email) VALUES(#{user.name}, #{user.pw}, #{user.email})")
    @Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    int save(@Param("user") final User user);

    @Insert("INSERT INTO user(name, pw, email, profileUrl) VALUES(#{signUpReq.name}, #{signUpReq.pw}, #{signUpReq.email}, #{signUpReq.profileUrl})")
    @Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    int save_user(@Param("signUpReq") final SignUpReq signUpReq);

    /**
     * Useridx로 조회
     * @param userIdx
     * @return
     */
    @Select("SELECT * FROM user WHERE userIdx = #{userIdx}")
    User findByUidx(@Param("userIdx") final int userIdx);

    @Select("SELECT * FROM user WHERE userIdx = #{userIdx}")
    UserNotProfile findByUidx_profile(@Param("userIdx") final int userIdx);

    @Select("SELECT * FROM user WHERE userIdx = #{userIdx}")
    UserChangeReq findByUidx_userchange(@Param("userIdx") final int userIdx);

    /**
     * 아이디와 비밀번호로 조회
     * @param loginReq
     * @return
     */
    @Select("SELECT * FROM user WHERE name = #{loginReq.name} AND pw = #{loginReq.pw}")
    User findByIdAndPassword(@Param("loginReq") final LoginReq loginReq);

    /**
     * ID로 조회
     * @param name
     * @return
     */
    @Select("SELECT * FROM user WHERE name = #{name}")
    User findByName(@Param("name") final String name);

    /**
     * 회원정보수정
     * @param userIdx
     * @param userChangeReq
     */
    @Update("UPDATE user SET name = #{userChangeReq.name}, pw=#{userChangeReq.pw}, email = #{userChangeReq.email}, star = #{userChangeReq.star}")
    void updateUser(@Param("userIdx") final int userIdx, @Param("userChangeReq") final UserChangeReq userChangeReq);

    @Update("UPDATE user SET profileUrl = #{profileUrl}")
    void updateUserProfile(@Param("profileUrl") final String profileUrl);

    /**
     * 전체 사용자 star순으로 조회
     * @return
     */
    @Select("SELECT * FROM user ORDER BY star DESC")
    List<RankingRes> listOfAllRanking();

    /**
     * 모든 프로그래머 수 조회
     * @return
     */
    @Select("SELECT * FROM user")
    List<User> allUserNum();

    @Select("SELECT star FROM user WHERE userIdx = #{userIdx}")
    int getStar(@Param("userIdx") final int userIdx);

    @Update("Update user SET star=#{star} WHERE userIdx = #{userIdx}")
    int save_star(@Param("star") final int star, @Param("userIdx") final int userIdx);
}
