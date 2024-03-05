package io.github.burukeyou;

import io.github.burukeyou.data.Student;
import io.github.burukeyou.dataframe2222.dataframe.SDFrame;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class JDFrameTest {

    static List<Student> studentList = new ArrayList<>();

    static {
        studentList.add(new Student("a","一中","一年级",11, new BigDecimal(1)));
        studentList.add(new Student("b","一中","一年级",12, new BigDecimal(2)));
        studentList.add(new Student("c","二中","一年级",13, new BigDecimal(3)));
        studentList.add(new Student("d","二中","一年级",null, new BigDecimal(4)));
        studentList.add(new Student("e","三中","一年级",null, new BigDecimal(5)));
    }

    @Test
    public void test0() {
        SDFrame<Student> sdf = SDFrame.read(studentList);

        SDFrame<Student> sdf2 = sdf.whereNotNull(Student::getAge);
        List<Student> students = sdf2.toLists();
        System.out.println();
    }


   /* @Test
    public void test1() {
        BigDecimal sum = SDFrame.read(studentList)
                .whereBetween(Student::getAge, 13, 16)
                .sum(Student::getScore);
        System.out.println(sum);

        List<FT2<String, BigDecimal>> d2 = SDFrame.read(studentList)
                .groupBySum(Student::getSchool, Student::getScore).toLists();

    }

    @Test
    public void test2() {
        SDFrame<Student> df = SDFrame.read(studentList);
        List<FT2<String, Long>> ft2s = df.whereGe(Student::getAge, 12).groupByCount(Student::getLevel).toLists();
        IFrame<FT2<String, Long>> iFrame = df.whereLe(Student::getAge, 13).groupByCount(Student::getLevel);

        System.out.println();
    }*/
}
