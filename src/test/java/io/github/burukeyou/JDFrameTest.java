package io.github.burukeyou;

import io.github.burukeyou.data.Student;
import io.github.burukeyou.data.UserInfo;
import io.github.burukeyou.dataframe.JDFrame;
import io.github.burukeyou.dataframe.SDFrame;
import io.github.burukeyou.dataframe.dataframe.MaxMin;
import io.github.burukeyou.dataframe.dataframe.item.FT2;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class JDFrameTest {

    static List<Student> studentList = new ArrayList<>();

    static {
        studentList.add(new Student(1,"a","一中","一年级",11, new BigDecimal(1)));
        studentList.add(new Student(2,"a","一中","一年级",11, new BigDecimal(1)));
        studentList.add(new Student(3,"b","一中","一年级",12, new BigDecimal(2)));
        studentList.add(new Student(4,"c","二中","一年级",13, new BigDecimal(3)));
        studentList.add(new Student(5,"d","二中","一年级",14, new BigDecimal(4)));
        studentList.add(new Student(6,"e","三中","二年级",15, new BigDecimal(5)));
    }

    /**
     *
     */
    @Test
    public void testDistinct(){
        List<Student> students = SDFrame.read(studentList).distinct().toLists();
        List<Student> students1 = SDFrame.read(studentList).distinct(Student::getSchool).toLists();
        List<Student> students15 = SDFrame.read(studentList).distinct(Student::getSchool).distinct(Student::getLevel).toLists();
        List<Student> students2 = SDFrame.read(studentList).distinct(e -> e.getSchool() + e.getLevel()).toLists();
        System.out.println();
    }

    @Test
    public void testRank(){
        SDFrame<Student> sdf = SDFrame.read(studentList).rankingAsc(Student::getAge, 3);
        JDFrame<Student> df = JDFrame.read(studentList).rankingAsc(Student::getAge, 3);
        SDFrame<Student> union = sdf.union(df);
        //
        List<Student> students = union.toLists();
        System.out.println();
    }

    @Test
    public void test1() {
        BigDecimal sum = SDFrame.read(studentList)
                .whereBetween(Student::getAge, 13, 16)
                .sum(Student::getAge);
        MaxMin<Student> studentMaxMin = SDFrame.read(studentList).maxMin(Student::getAge);
        MaxMin<Integer> integerMaxMin = SDFrame.read(studentList).maxMinValue(Student::getAge);
        System.out.println();

        System.out.println(sum);

        List<FT2<String, BigDecimal>> d2 = SDFrame.read(studentList)
                .groupBySum(Student::getSchool, Student::getScore).toLists();

        SDFrame<FT2<String, BigDecimal>> ft2SDFrame = SDFrame.read(studentList)
                .groupBySum(Student::getSchool, Student::getScore);

        SDFrame<Student> map = ft2SDFrame.map(e -> new Student(e.getC1(), e.getC2()));
        List<Student> students = map.toLists();

        System.out.println();
    }

    @Test
    public void test2() {
        JDFrame<Student> df = JDFrame.read(studentList);
        List<FT2<String, Long>> ft2s = df.whereGe(Student::getAge, 12).groupByCount(Student::getLevel).toLists();
        JDFrame<FT2<String, Long>> sdFrame = df.whereLe(Student::getAge, 13).groupByCount(Student::getLevel);

        System.out.println();
    }

    @Test
    public void testJoin(){
        SDFrame<Student> sdf = SDFrame.read(studentList);

        sdf.print();
        System.out.println("======== 1 =======");

        SDFrame<FT2<String, BigDecimal>> sdf2 = SDFrame.read(studentList).groupBySum(Student::getSchool, Student::getScore);

        sdf2.print();
        System.out.println("======== 2 =======");

        SDFrame<UserInfo> frame = sdf.join(sdf2, (a, b) -> a.getSchool().equals(b.getC1()), (a, b) -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setC1(a.getSchool());
            userInfo.setC2(b.getC2().intValue());
            userInfo.setC3(String.valueOf(a.getId()));
            return userInfo;
        });

        frame.print();
        System.out.println();
    }

    @Test
    public void testComun(){
        List<String> columns = SDFrame.read(studentList).columns();
        System.out.println();
    }
}
