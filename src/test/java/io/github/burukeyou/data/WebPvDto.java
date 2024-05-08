package io.github.burukeyou.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WebPvDto {

    private String type;

    private Integer score;

    private Integer pvCount;

    private Integer rank;

    private Integer rowNumber;

    private Integer densRank;

    private BigDecimal sum;

    private Integer max;


    public WebPvDto(String type, Integer score, Integer pvCount) {
        this.type = type;
        this.score = score;
        this.pvCount = pvCount;
    }


}
