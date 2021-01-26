package org.haru.haru_coding.model;

import lombok.Data;

@Data
public class UserChangeReq {
    private String name;
    private String pw;
    private String email;
    private String star;
}
