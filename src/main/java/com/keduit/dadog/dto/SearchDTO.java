package com.keduit.dadog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDTO {
    private String searchBy;

    private String searchQuery = "";
}
