package io.github.burukeyou.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String name;
    private String school;
    private String level;
    private Integer age;
    private BigDecimal score;



}
