package org.haru.haru_coding.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class User {
    private int userIdx;
    private String name;
    private String pw; //(회원정보 요청시 비밀번호 반환 x)
    private String email;
    private int star;

    private MultipartFile profile;
    private String profileUrl;
}
