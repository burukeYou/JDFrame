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

    private BigDecimal percentRank;

    private BigDecimal sum;

    private Integer max;

    private BigDecimal avg;

    public Object value;

    public WebPvDto(String type, Integer score, Integer pvCount) {
        this.type = type;
        this.score = score;
        this.pvCount = pvCount;
    }


}
