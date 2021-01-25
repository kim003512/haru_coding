package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.dto.UserNotProfile;
import org.haru.haru_coding.model.LoginReq;
import org.haru.haru_coding.model.SignUpReq;

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
     * @param user
     */
    @Update("UPDATE user SET name = #{user.name}, email = #{user.email}, profileUrl = #{user.profileUrl}")
    void updateUser(@Param("userIdx") final int userIdx, @Param("user") final User user);

    /**
     * 전체 사용자 star순으로 조회
     * @return
     */
    @Select("SELECT * FROM user ORDER BY star")
    List<User> listOfAllRanking();

    @Select("SELECT * FROM user")
    List<User> allUserNum();
}
