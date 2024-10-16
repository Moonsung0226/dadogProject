package com.keduit.dadog.entity;

//후원

import com.keduit.dadog.dto.SponDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sponNo;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private long price;

    public Sponsor createSponsor(SponDTO sponDTO) {
        this.name = sponDTO.getName();
        this.price = sponDTO.getPrice();
        return this;
    }

}
