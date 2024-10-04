package com.keduit.dadog;

import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SpringBootTest
public class Test001 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
