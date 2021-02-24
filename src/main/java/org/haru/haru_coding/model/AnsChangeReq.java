package org.haru.haru_coding.model;

import lombok.Data;

@Data
public class AnsChangeReq {
    private int userIdx;
    private String category;
    private int quest;
    private int answer;
    private int choiceAns;
}
