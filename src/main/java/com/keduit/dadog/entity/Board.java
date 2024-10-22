package com.keduit.dadog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    private Long boardNo;

    @Column(name = "board_writer")
    private String boardWriter;

    @Column(name = "board_title")
    private String boardTitle;

    @Column(name = "board_content")
    private String boardContent;

    @Column(name = "board_views")
    private Long boardViews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    @JsonBackReference
    private User user;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    private List<Reply> replyList = new ArrayList<>();


}
