package io.github.burukeyou.data;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private LocalDateTime createTime;

    private Integer id;
    private String name;
    private String school;
    private String level;
    private Integer age;
    private BigDecimal score;

    private Integer rank;

    private List<String> stringList = new ArrayList<>();

    private List<UserInfo> userInfoList = new ArrayList<>();

    private String[] stringArray;

    private UserInfo[] userInfoArray;

    public Student(String school) {
        this.school = school;
    }

    public Student(String school, String level) {
        this.school = school;
        this.level = level;
    }

    public Student(String name, String school, String level) {
        this.name = name;
        this.school = school;
        this.level = level;
    }

    public Student(String level, BigDecimal score) {
        this.level = level;
        this.score = score;
    }

    public Student(Integer id, String name, String school, String level, Integer age, BigDecimal score) {
        this.id = id;
        this.name = name;
        this.school = school;
        this.level = level;
        this.age = age;
        this.score = score;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
