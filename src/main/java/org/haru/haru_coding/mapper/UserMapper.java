package org.haru.haru_coding.mapper;

import org.apache.ibatis.annotations.*;
import org.haru.haru_coding.dto.User;
import org.haru.haru_coding.dto.UserNotProfile;
import org.haru.haru_coding.model.LoginReq;
import org.haru.haru_coding.model.RankingRes_all;
import org.haru.haru_coding.model.SignUpReq;
import org.haru.haru_coding.model.UserChangeReq;

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
     *
     * @param userIdx
     * @return
     */
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

    @Select("SELECT * FROM user WHERE name = #{name}")
    String findByName_same(@Param("name") final String name);

    /**
     * 회원정보수정
     * @param userIdx
     * @param userChangeReq
     */
    @Update("UPDATE user SET name = #{userChangeReq.name}, pw=#{userChangeReq.pw}, email = #{userChangeReq.email}, star = #{userChangeReq.star} WHERE userIdx = #{userIdx}")
    void updateUser(@Param("userIdx") final int userIdx, @Param("userChangeReq") final UserChangeReq userChangeReq);

    /**
     * 회원 프로필 수정
     * @param profileUrl
     */
    @Update("UPDATE user SET profileUrl = #{profileUrl}")
    void updateUserProfile(@Param("profileUrl") final String profileUrl);

    /**
     * 전체 사용자 star순으로 조회
     * @return
     */
    @Select("SELECT * FROM user ORDER BY star DESC")
    List<RankingRes_all> listOfAllRanking();

    @Select("SELECT * FROM user ORDER BY javastar DESC")
    List<RankingRes_all> listOfAllRanking_java();

    @Select("SELECT * FROM user ORDER BY pythonstar DESC")
    List<RankingRes_all> listOfAllRanking_python();

    @Select("SELECT * FROM user ORDER BY cplusstar DESC")
    List<RankingRes_all> listOfAllRanking_cplus();

    /**
     * 모든 프로그래머 수 조회
     * @return
     */
    @Select("SELECT * FROM user")
    List<User> allUserNum();

    /**
     * 사용자 star 얻어오기
     * @param userIdx
     * @return
     */
    @Select("SELECT star FROM user WHERE userIdx = #{userIdx}")
    int getStar(@Param("userIdx") final int userIdx);

    @Select("SELECT javastar FROM user WHERE userIdx = #{userIdx}")
    int getJavaStar(@Param("userIdx") final int userIdx);

    @Select("SELECT pythonstar FROM user WHERE userIdx = #{userIdx}")
    int getPythonStar(@Param("userIdx") final int userIdx);

    @Select("SELECT cplusstar FROM user WHERE userIdx = #{userIdx}")
    int getCplusStar(@Param("userIdx") final int userIdx);

    /**
     * star수 변경
     * @param star
     * @param userIdx
     * @return
     */
    @Update("Update user SET star=#{star} WHERE userIdx = #{userIdx}")
    int save_star(@Param("star") final int star, @Param("userIdx") final int userIdx);

    @Update("Update user SET javastar=#{javastar} WHERE userIdx=#{userIdx}")
    int save_javastar(@Param("javastar") final int javastar, @Param("userIdx") final int userIdx);

    @Update("Update user SET pythonstar=#{pythonstar} WHERE userIdx=#{userIdx}")
    int save_pythonstar(@Param("pythonstar") final int pythonstar, @Param("userIdx") final int userIdx);

    @Update("Update user SET cplusstar=#{cplusstar} WHERE userIdx=#{userIdx}")
    int save_cplusstar(@Param("cplusstar") final int cplusstar, @Param("userIdx") final int userIdx);
}
