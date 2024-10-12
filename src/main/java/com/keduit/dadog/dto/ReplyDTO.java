package com.keduit.dadog.dto;

import groovy.transform.Sealed;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Sealed
@ToString
@Builder
public class ReplyDTO {

    private Long ReplyNo;

    private Long boardNo;

    private String ReplyWriter;

    private String ReplyTitle;

    private String ReplyContent;

    private Date updateReply;

    private Date createReply;

}
