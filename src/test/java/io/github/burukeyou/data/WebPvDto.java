package io.github.burukeyou.data;

import lombok.Data;

@Data
public class WebPvDto {

    private String type;

    private Integer score;

    private Integer pvCount;

//    private Integer rank;

    public  Object value;


/*
    private Integer rowNumber;

    private Integer densRank;

    private BigDecimal percentRank;

    private BigDecimal sum;

    private Integer max;

    private BigDecimal avg;
*/


    public WebPvDto(String type, Integer score, Integer pvCount) {
        this.type = type;
        this.score = score;
        this.pvCount = pvCount;
    }
}
