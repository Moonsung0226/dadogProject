package com.keduit.dadog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LostSearchDTO {
    private String searchBy;

    private String searchQuery = "";
}
