package io.github.burukeyou.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private int id;
    private String name;
    private String school;
    private String level;
    private Integer age;
    private BigDecimal score;


    public Student(String level, BigDecimal score) {
        this.level = level;
        this.score = score;
    }
}
