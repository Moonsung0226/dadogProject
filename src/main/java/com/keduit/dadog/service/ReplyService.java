package com.keduit.dadog.service;

import com.keduit.dadog.dto.ReplyDTO;
import com.keduit.dadog.entity.Reply;
import com.keduit.dadog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final List<ReplyDTO> replyDTOList = new ArrayList<>();

//    public Long addReply(ReplyDTO replyDTO) {
//        Reply reply = new Reply();
//        reply.set
//
//    }
}
