package io.github.burukeyou;

import io.github.burukeyou.data.Student;
import io.github.burukeyou.data.UserInfo;
import io.github.burukeyou.dataframe.iframe.JDFrame;
import io.github.burukeyou.dataframe.iframe.SDFrame;
import io.github.burukeyou.dataframe.iframe.MaxMin;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JDFrameTest {

    static List<Student> studentList = new ArrayList<>();

    static {
        studentList.add(new Student(1,"a","一中","一年级",11, new BigDecimal(1)));
        studentList.add(new Student(2,"a","一中","一年级",11, new BigDecimal(1)));
        studentList.add(new Student(3,"b","一中","一年级",12, new BigDecimal(2)));
        studentList.add(new Student(4,"c","二中","一年级",13, new BigDecimal(3)));
        studentList.add(new Student(5,"d","二中","一年级",14, new BigDecimal(4)));
        studentList.add(new Student(6,"e","三中","二年级",14, new BigDecimal(5)));
        studentList.add(new Student(7,"e","三中","二年级",15, new BigDecimal(5)));
    }

    /**
     *
     */
    @Test
    public void testDistinct(){
        List<Student> students = SDFrame.read(studentList).distinct().toLists(); // 根据对象hashCode去重
        List<Student> students1 = SDFrame.read(studentList).distinct(Student::getSchool).toLists(); // 根据学校名去重
        List<Student> students2 = SDFrame.read(studentList).distinct(e -> e.getSchool() + e.getLevel()).toLists(); // 根据学校名拼接级别去重复
        List<Student> students3 = SDFrame.read(studentList).distinct(Student::getSchool).distinct(Student::getLevel).toLists(); // 先根据学校名去除重复再根据级别去除重复
        System.out.println();


        SDFrame.read(studentList)
                .whereBetween(Student::getAge,3,6) // 过滤年龄在[3，6]岁的
                .whereBetweenR(Student::getAge,3,6) // 过滤年龄在(3，6]岁的, 不含3岁
                .whereBetweenL(Student::getAge,3,6)      // 过滤年龄在[3，6)岁的, 不含6岁
                .whereNotNull(Student::getName) // 过滤名字不为空的数据， 兼容了空字符串''的判断
                .whereGt(Student::getAge,3)    // 过滤年龄大于3岁
                .whereGe(Student::getAge,3)   // 过滤年龄大于等于3岁
                .whereLt(Student::getAge,3)  // 过滤年龄小于3岁的
                .whereIn(Student::getAge, Arrays.asList(3,7,8)) // 过滤年龄为3岁 或者7岁 或者 8岁的数据
                .whereNotIn(Student::getAge, Arrays.asList(3,7,8)) // 过滤年龄不为为3岁 或者7岁 或者 8岁的数据
                .whereEq(Student::getAge,3) // 过滤年龄等于3岁的数据
                .whereNotEq(Student::getAge,3) // 过滤年龄不等于3岁的数据
                .whereLike(Student::getName,"jay") // 模糊查询，等价于 like "%jay%"
                .whereLikeLeft(Student::getName,"jay") // 模糊查询，等价于 like "jay%"
                .whereLikeRight(Student::getName,"jay"); // 模糊查询，等价于 like "%jay"


    }

    @Test
    public void testRank(){
        SDFrame<Student> sdf = SDFrame.read(studentList).cutRankingSameAsc(Student::getAge, 3);
        JDFrame<Student> df = JDFrame.read(studentList).cutRankingSameAsc(Student::getAge, 3);
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

        List<FI2<String, BigDecimal>> d2 = SDFrame.read(studentList)
                .groupBySum(Student::getSchool, Student::getScore).toLists();

        SDFrame<FI2<String, BigDecimal>> ft2SDFrame = SDFrame.read(studentList)
                .groupBySum(Student::getSchool, Student::getScore);

        SDFrame<Student> map = ft2SDFrame.map(e -> new Student(e.getC1(), e.getC2()));
        List<Student> students = map.toLists();

        System.out.println();
    }

    @Test
    public void test2() {
        JDFrame<Student> df = JDFrame.read(studentList);
        List<FI2<String, Long>> ft2s = df.whereGe(Student::getAge, 12).groupByCount(Student::getLevel).toLists();
        JDFrame<FI2<String, Long>> sdFrame = df.whereLe(Student::getAge, 13).groupByCount(Student::getLevel);

        System.out.println();
    }

    @Test
    public void testJoin(){
        SDFrame<Student> sdf = SDFrame.read(studentList);

        sdf.show(2);
        System.out.println("======== 1 =======");

        // 获取学生年龄在9到16岁的学学校合计分数最高的前10名
        SDFrame<FI2<String, BigDecimal>> sdf2 = SDFrame.read(studentList)
                .whereNotNull(Student::getAge)
                .whereBetween(Student::getAge,9,16)
                .groupBySum(Student::getSchool, Student::getScore)
                .sortDesc(FI2::getC2)
                .cutFirst(10);


        sdf2.show();
        System.out.println("======== 2 =======");

        SDFrame<UserInfo> frame = sdf.join(sdf2, (a, b) -> a.getSchool().equals(b.getC1()), (a, b) -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setC1(a.getSchool());
            userInfo.setC2(b.getC2().intValue());
            userInfo.setC3(String.valueOf(a.getId()));
            return userInfo;
        });

        frame.show(5);
        System.out.println();
    }

    @Test
    public void testComun(){
       // SDFrame<Student> students = SDFrame.read(studentList).mapPercent(Student::getScore,Student::setScore,2);

        SDFrame<Student> df = SDFrame.read(studentList);
        Student head = df.head();
        Student tail = df.tail();

        List<Student> head1 = df.head(3);
        List<Student> tail1 = df.tail(4);


        SDFrame<List<Student>> sdFrame = df.partition(5);
        List<List<Student>> lists = sdFrame.toLists();
        System.out.println();

        System.out.println();
    }

    @Test
    public void testAddCol(){
        SDFrame<Student> df = SDFrame.read(studentList);

        //List<FT2<Student, Integer>> ft2s = df.addRankingSameCol(Comparator.comparing(Student::getAge)).toLists();

        List<Student> students2 = df.addRankingSameCol(Student::getAge, Student::setRank).toLists();

        //SDFrame<Student> students = df.addSortNoCol(Student::setRank);
        //List<Student> students1 = students.toLists();

        List<Student> head = df.head(3);
        System.out.println();
    }

    @Test
    public void testRead() {
        // 枚举
        String  a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] chars = a.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars.length; j++) {
                String s = chars[i] + String.valueOf(chars[j]);
                System.out.println(  "SDFrame<"  + s + "<String,Integer>>");
            }
        }

    }

    @Test
    public void testSum() {
        JDFrame<Student> frame = JDFrame.read(studentList);
        Student s1 = frame.max(Student::getAge);// 获取年龄最大的学生
        Integer s2  = frame.maxValue(Student::getAge);      // 获取学生里最大的年龄
        Student s3 = frame.min(Student::getAge);// 获取年龄最小的学生
        Integer s4  = frame.minValue(Student::getAge);      // 获取学生里最小的年龄
        BigDecimal s5 = frame.avg(Student::getAge); // 获取所有学生的年龄的平均值
        BigDecimal s6 = frame.sum(Student::getAge); // 获取所有学生的年龄合计
        MaxMin<Student> s7 = frame.maxMin(Student::getAge); // 同时获取年龄最大和最小的学生
        MaxMin<Integer> s8 = frame.maxMinValue(Student::getAge); // 同时获取学生里最大和最小的年龄
        System.out.println();
    }
}
