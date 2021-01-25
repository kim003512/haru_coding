package org.haru.haru_coding.dto;

import lombok.Data;

@Data
public class UserNotProfile {
    private int userIdx;
    private String name;
    private String pw;
    private String email;
}
