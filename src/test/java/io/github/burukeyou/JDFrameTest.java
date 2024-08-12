package io.github.burukeyou;

import io.github.burukeyou.data.Student;
import io.github.burukeyou.data.UserInfo;
import io.github.burukeyou.dataframe.iframe.JDFrame;
import io.github.burukeyou.dataframe.iframe.SDFrame;
import io.github.burukeyou.dataframe.iframe.function.CompareTwo;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.JoinOn;
import io.github.burukeyou.dataframe.iframe.support.MaxMin;
import io.github.burukeyou.dataframe.iframe.window.Sorter;
import io.github.burukeyou.dataframe.iframe.window.Window;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class JDFrameTest {

    static List<Student> studentList = new ArrayList<>();

    static {
        studentList.add(new Student(1,"a","一中","\"生活\",日子",11, new BigDecimal(1)));
        studentList.add(new Student(2,"a","一中","一年级",13, new BigDecimal(1)));
        studentList.add(new Student(3,"d","二中","一年级",14, new BigDecimal(4)));
        studentList.add(new Student(4,"b","一中","三年级",12, new BigDecimal(2)));
        studentList.add(new Student(5,"c","二中","一年级",13, new BigDecimal(3)));
        studentList.add(new Student(6,"e","三中","[\"a\",\"b\"]",14, new BigDecimal(5)));
        studentList.add(new Student(7,"e","三中","二年级",14, new BigDecimal(5)));
        studentList.add(new Student(8,"e","三中","[{\"a\":1},{\"b\":2}]",14, new BigDecimal(5)));
        studentList.add(new Student(10,"e","一中","[爱好,奇怪，懂得]",15, new BigDecimal(5)));
        studentList.add(new Student(11,"e","三中","二年级",15, new BigDecimal(5)));
        studentList.add(new Student(12,"e","三中","二年级",16, new BigDecimal(5)));
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

        SDFrame.read(studentList).distinct(Student::getName,(list) -> {
            // 重复元素里选择年龄最大那个
            return SDFrame.read(list).max(Student::getAge);
        }).show();


        System.out.println();
    }



    @Test
    public void testWhere(){
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

        System.out.println("======== 矩阵1 =======");

        SDFrame<Student> sdf = SDFrame.read(studentList);

        sdf.show(2);

        // 获取学生年龄在9到16岁的学学校合计分数最高的前10名
        SDFrame<FI2<String, BigDecimal>> sdf2 = SDFrame.read(studentList)
                .whereNotNull(Student::getAge)
                .whereBetween(Student::getAge,9,16)
                .groupBySum(Student::getSchool, Student::getScore)
                .sortDesc(FI2::getC2)
                .cutFirst(10);

        System.out.println("======== 矩阵2 =======");
        sdf2.show();

        List<Student> studentList2 = studentList;
        List<FI2<String, BigDecimal>> fi2s = sdf2.toLists();



        Map<String, FI2<String, BigDecimal>> collect = fi2s.stream().collect(Collectors.toMap(FI2::getC1, e -> e));
        for (Student student : studentList2) {
            FI2<String, BigDecimal> key = collect.get(student.getSchool());
            if (key != null){

            }
        }


        SDFrame<UserInfo> frame = sdf.join(sdf2, (a, b) -> a.getSchool().equals(b.getC1()), (a, b) -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setKey1(a.getSchool());
            userInfo.setKey2(b.getC2().intValue());
            userInfo.setKey3(String.valueOf(a.getId()));
            return userInfo;
        });

        System.out.println("======== 连接后结果 =======");
        frame.show(5);
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

    @Test
    public void testGroup(){
        JDFrame<Student> frame = JDFrame.read(studentList);
        // 等价于 select school,sum(age) ... group by school
        List<FI2<String, BigDecimal>> a = frame.groupBySum(Student::getSchool, Student::getAge).toLists();
        // 等价于 select school,max(age) ... group by school
        List<FI2<String, Integer>> a2 = frame.groupByMaxValue(Student::getSchool, Student::getAge).toLists();
        //  与 groupByMaxValue 含义一致，只是返回的是最大的对象非最大的值
        List<FI2<String, Student>> a3 = frame.groupByMax(Student::getSchool, Student::getAge).toLists();
        // 等价于 select school,min(age) ... group by school
        List<FI2<String, Integer>> a4 = frame.groupByMinValue(Student::getSchool, Student::getAge).toLists();
        // 等价于 select school,count(*) ... group by school
        List<FI2<String, Long>> a5 = frame.groupByCount(Student::getSchool).toLists();
        // 等价于 select school,avg(age) ... group by school
        List<FI2<String, BigDecimal>> a6 = frame.groupByAvg(Student::getSchool, Student::getAge).toLists();

        // 等价于 select school,sum(age),count(age) group by school
        List<FI3<String, BigDecimal, Long>> a7 = frame.groupBySumCount(Student::getSchool, Student::getAge).toLists();

        // (二级分组)等价于 select school,level,sum(age),count(age) group by school,level
        List<FI3<String, String, BigDecimal>> a8 = frame.groupBySum(Student::getSchool, Student::getLevel, Student::getAge).toLists();

        // （三级分组）等价于 select school,level,name,sum(age),count(age) group by school,level,name
        List<FI4<String, String, String, BigDecimal>> a9 = frame.groupBySum(Student::getSchool, Student::getLevel, Student::getName, Student::getAge).toLists();


        //
        SDFrame.read(studentList).groupByCustom(Student::getName,(list) -> SDFrame.read(list).maxValue(Student::getAge)).show();

        SDFrame.read(studentList).groupByCustom(Student::getName,Student::getSchool,(list) -> SDFrame.read(list).joining(Student::getId,",")).show();

        System.out.println();
    }

    @Test
    public void testSort(){
        // 等价于 order by age desc
        SDFrame.read(studentList).sortDesc(Student::getAge);
        //  等价于 order by age desc, level asc
        SDFrame.read(studentList).sortDesc(Student::getAge).sortAsc(Student::getLevel);
        // 等价于 order by age asc
        SDFrame.read(studentList).sortAsc(Student::getAge);
        // 使用Comparator 排序
        SDFrame.read(studentList).sortAsc(java.util.Comparator.comparing(e -> e.getLevel() + e.getId()));

        // 等价于 select round(score*100,2) from student
        SDFrame<Student> map2 = SDFrame.read(studentList).mapPercent(Student::getScore, Student::setScore,2);
    }

    @Test
    public void testReplenish(){
        List<String> allDim = Arrays.asList("一中","二中","三中","四中");
        //SDFrame.read(studentList).replenish(Student::getSchool,allDim,(school) -> new Student(school)).show(30);

        SDFrame.read(studentList).replenish(Student::getSchool,Student::getLevel,(school,level) -> new Student(school,level)).show(30);
    }


    @Test
    public void testOver(){

        List<Student> students1 = studentList.subList(3, 5);
        System.out.println();

        SDFrame<Student> sdFrame = SDFrame.read(studentList);
        //Over.OverBuilder<Student, Object, R> collect = Over.sortBy(Comparator.comparing(Student::getAge)).collect(OverEnum.ROW_NUMBER);
        //SDFrame<FI2<Student, Integer>> fi2s = sdFrame.overRowNumber(overBuilder);

        //Window<Student> window = Window.sortAscBy(Student::getId).sortDesc(Student::getAge);

        //Window<Student> window = Window.sortBy(Comparator.comparing(Student::getId).thenComparing(Comparator.comparing(Student::getAge).reversed()));

        //studentList.sort(Comparator.comparing(Student::getLevel).thenComparing(Student::getAge).reversed());
        //studentList.sort(Comparator.comparing(Student::getId).thenComparing(Comparator.comparing(Student::getAge).reversed()));

        //studentList.sort(Sorter.sortAscBy(Student::getId).sortDesc(Student::getAge));
        //Window<Student> window = Window.groupBy(Student::getSchool, Student::getLevel).sortDesc(Student::getAge);

        //Window<Student> window = Window.sortDescBy(Student::getAge);
        //SDFrame<FI2<Student, Integer>> map = sdFrame.overRowNumber(window);


        //sdFrame.window(Window.sortDescBy(Student::getAge)).overRowNumber().show(30);

        //WindowSDFrame<Student> windowSDFrame = SDFrame.read(studentList).window(Window.sortDescBy(Student::getAge));

/*
        Window<Student> window = Window.sortDescBy(Student::getAge);
        sdFrame = sdFrame.overRowNumber(Student::setRank,window)
                         .overRank(Student::setId,window);
*/


        //WindowSDFrame<FI2<Student, Integer>> frame = sdFrame.window(Window.sortDescBy(Student::getAge)).overRowNumber();

        SDFrame<Student> students = sdFrame
                .whereNotNull(Student::getScore)
                .window(Window.sortDescBy(Student::getAge))
                .overRowNumberS(Student::setRank)
                .overRankS(Student::setId);

        //sdFrame.window().overMaxValue(Student::getAge).show(30);

        //sdFrame.overMaxValue(Student::getAge).show(30);

        sdFrame.overMaxValueS(Student::setId,Student::getAge).show(30);

        //rank.forEachDo(e -> System.out.println(e));


        sdFrame.window().overRowNumber();

        //SDFrame<FI2<Integer, Integer>> map = sdFrame.overRank(overParam).map(e -> new FI2<>(e.getC1().getAge(), e.getC2()));
        //SDFrame<FI2<Integer, Integer>> map = sdFrame.overDenseRank(overParam).map(e -> new FI2<>(e.getC1().getAge(), e.getC2()));

        //SDFrame<FI2<Integer, BigDecimal>> map = sdFrame.overPercentRank(overParam).map(e -> new FI2<>(e.getC1().getAge(), e.getC2()));
        //SDFrame<FI2<Integer, BigDecimal>> map = sdFrame.overCumeDist(overParam).map(e -> new FI2<>(e.getC1().getAge(), e.getC2()));

        //SDFrame<FI2<Integer, Integer>> map = sdFrame.overLag(overParam, Student::getId, 1).map(e -> new FI2<>(e.getC1().getAge(), e.getC2()));
        //SDFrame<FI2<Integer, Integer>> map = sdFrame.overLead(overParam, Student::getId, 1).map(e -> new FI2<>(e.getC1().getAge(), e.getC2()));
        //map.show(30);
    }

    @Test
    public void testAddCol2(){
        JDFrame.read(studentList)
                //.addRowNumberCol()
                .addRowNumberCol(Sorter.sortDescBy(Student::getAge),Student::setRank)
                //.addRowNumberCol(Student::setRank)
                //.addRowNumberCol(Sorter.sortAscBy(Student::getAge),Student::setRank)
                //.addRowNumberCol(Sorter.sortAscBy(Student::getAge))
                //.addRankCol(Sorter.sortAscBy(Student::getAge),Student::setRank)
                //.addRankCol(Sorter.sortAscBy(Student::getAge))
                .show(30);
    }

    @Test
    public void testToMap(){
        // 原生stream toMap value 不能为null， key可以为null. Frame的toMap 不会
        Map<String, Integer> stringIntegerMap = SDFrame.read(studentList).toMap(Student::getName, Student::getId);
        Map<Integer, String> toMap = SDFrame.read(studentList).toMap(Student::getId, Student::getName);

        //
        Map<String, Map<String, Integer>> multiMap = SDFrame.read(studentList).toMap(Student::getSchool, Student::getName, Student::getId);
        System.out.println();
    }

    @Test
    public void testForEachDo(){
        SDFrame.read(studentList).forEachDo((index,student) -> {
            student.setRank(index);
            System.out.println(index + "------>" + student.getName());
        }).show();

        SDFrame.read(studentList).forEachDo((student) -> {
            System.out.println("------>" + student.getId());
        });

        SDFrame.read(studentList).forEachParallel((student) -> {
            System.out.println("------>>>>" + student.getId());
        });
    }

    @Test
    public void testSortNullValue(){
        // 原生sort， 如果字段值为null会空指针异常
        //studentList.sort(Comparator.comparing(Student::getCreateTime));

        studentList.get(1).setCreateTime(LocalDateTime.now());
        studentList.get(3).setCreateTime(LocalDateTime.now().plusDays(3));

        // 兼容 null值 情况， 并将null排序到最后
        SDFrame.read(studentList).sortAsc(Student::getCreateTime).show(30);

        SDFrame.read(studentList).sortDesc(Student::getCreateTime).show(30);

        System.out.println();
    }

    @Test
    public void eqContain(){
        SDFrame.read(studentList).whereEq(Student::getName,"e").show(30);

        boolean e = SDFrame.read(studentList).containsValue(Student::getName, "e");
        System.out.println(e);
    }

    @Test
    public void testJoining(){
        String joining = SDFrame.read(studentList).joining(Student::getName, ",");
        String joining2 = SDFrame.read(studentList).joining(Student::getName, ",","[","]");
        System.out.println();
    }

    @Test
    public void testExplodeString(){
        //SDFrame.read(studentList).explodeString(Student::getLevel,"[,，]").show(30);

        //SDFrame.read(studentList).explodeString(Student::getLevel, Student::setName, "[,，]").show(30);
        List<Student> ax = SDFrame.read(studentList).explodeString(Student::getLevel, Student::setName, "[,，]").toLists();

        //SDFrame.read(studentList).explodeString(Student::getLevel,(t,v) -> t.setRank(Integer.parseInt(v)),"[,，]").show(30);

        System.out.println();
    }


    @Test
    public void testExplodeJsonArray(){
        SDFrame.read(studentList).explodeJsonArray(Student::getLevel, Student::setName).show(30);

        System.out.println();
    }

    @Test
    public void testExplodeCollection(){
        // 1
        studentList.get(1).setStringList(Arrays.asList("哈哈1","哈哈2"));
        studentList.get(5).setStringList(Arrays.asList("牛逼1","牛逼2"));
        List<FI2<Student, String>> fi2s = SDFrame.read(studentList).explodeCollection(Student::getStringList).toLists();


        SDFrame.read(studentList).explodeCollection(Student::getStringList,Student::setName).show(30);

        // 2
        studentList.get(3).setUserInfoList(Arrays.asList(new UserInfo("a1",1),new UserInfo("a1",2)));
        studentList.get(7).setUserInfoList(Arrays.asList(new UserInfo("b1",1),new UserInfo("b1",2)));
        List<FI2<Student, UserInfo>> fi2s1 = SDFrame.read(studentList).explodeCollection(Student::getUserInfoList).toLists();

        System.out.println();
    }

    @Test
    public void testExplodeArray(){
        // 1、
        studentList.get(1).setStringArray(new String[]{"哈哈1","哈哈2"});
        studentList.get(5).setStringArray(new String[]{"牛逼1","牛逼2"});
        List<FI2<Student, String>> a1 = SDFrame.read(studentList).explodeCollectionArray(Student::getStringArray, String.class).toLists();
        System.out.println();

        // 2、
        studentList.get(3).setUserInfoArray(new UserInfo[]{new UserInfo("哈哈",1),new UserInfo("哈哈",2)});
        studentList.get(7).setUserInfoArray(new UserInfo[]{new UserInfo("牛逼",1),new UserInfo("牛逼",2)});
        List<FI2<Student, UserInfo>> a2 = SDFrame.read(studentList).explodeCollectionArray(Student::getUserInfoArray, UserInfo.class).toLists();

        System.out.println();
    }

    @Test
    public void testforEachParallel(){
        SDFrame.read(studentList).mapParallel(e -> {
            System.out.println("---->" + e.getId());
            if (e.getId() > 8){
                return 0;
            }
            return e.getId();
        }).whereNotEq(Function.identity(), 0).show();
    }


    @Test
    public void testVoidJoin(){
        SDFrame<Student> frame1 = SDFrame.read(studentList);

        List<UserInfo> userInfos = Arrays.asList(new UserInfo("a", 99), new UserInfo("a", 4), new UserInfo("b", 4));
        SDFrame<UserInfo> frame2 = SDFrame.read(userInfos);

        frame1.leftJoinVoid(frame2,(a, b) -> a.getName().equals(b.getKey1()),(a, b) -> {
            if (b == null){
                return;
            }
            log.info("id【{}】name【{}】对应的key【{}】",a.getId(),a.getName(),b.getKey2());
        });

        System.out.println("===========");

        JoinOn<Student, UserInfo> joinOn = JoinOn.on(Student::getName, UserInfo::getKey1).thenOn(Student::getId, UserInfo::getKey2);
        frame1.leftJoinVoid(frame2,joinOn,(stu, user) -> {
            if (user == null){
                // 未关联上
                return;
            }
            // 关联上了
            log.info("name【{}】对应的key【{}】",stu.getName(),user.getKey2());
        });
    }


    /**
     *  Once：
     *      在SQL语言里默认的join语义是： 主表一条记录可能会关联副表多条记录。
     *      而Once就是只会关联其中一个。
     *
     *  Void：
     *      只是执行连结操作的回调。不会改变Frame的数据和行数
     */
    @Test
    public void testJoinOnce(){
        SDFrame<Student> frame1 = SDFrame.read(studentList);
        List<UserInfo> userInfos = Arrays.asList(new UserInfo("a", 99), new UserInfo("a", 4), new UserInfo("b", 4));
        SDFrame<UserInfo> frame2 = SDFrame.read(userInfos);
        frame1.leftJoinOnceVoid(frame2,(a, b) -> a.getName().equals(b.getKey1()),(a, b) -> {
            if (b == null){
                return;
            }
            log.info("id【{}】name【{}】对应的key【{}】",a.getId(),a.getName(),b.getKey2());
        }).show();

    }




    @Test
    public void testOper(){

        List<UserInfo> us1 = Arrays.asList(new UserInfo("a", 99), new UserInfo("a", 99),new UserInfo("a", 4), new UserInfo("b", 4));
        List<UserInfo> us2 = Arrays.asList(new UserInfo("a", 99), new UserInfo("b", 4), new UserInfo("c", 4));

        SDFrame<UserInfo> frame2 = SDFrame.read(us2);

        System.out.println("----- 并集(不去重)------");
        SDFrame.read(us1).unionAll(frame2).show();

        System.out.println("----- 并集(去重)------");
        SDFrame.read(us1).union(frame2).show();

        System.out.println("----- 交集------");
        SDFrame.read(us1).intersection(frame2).show();

        System.out.println("----- retainAll------");
        SDFrame.read(us1).retainAll(frame2).show();

        System.out.println("----- 差集------");
        SDFrame.read(us1).different(frame2).show();

        System.out.println("------- 减去 -----");
        SDFrame.read(us1).subtract(frame2).show();

        SDFrame.read(us1).show();

    }

    @Test
    public void testOper2(){
        List<UserInfo> us1 = Arrays.asList(new UserInfo("a", 1,"99"), new UserInfo("a", 2,"99"),new UserInfo("c",3,"88"), new UserInfo("b", 4,"77"));
        List<UserInfo> us2 = Arrays.asList(new UserInfo("a", 5,"99"), new UserInfo("b",6,"77"), new UserInfo("c", 7,"4"));

        SDFrame<UserInfo> frame2 = SDFrame.read(us2);

        System.out.println("------- 并集 -----");
        SDFrame.read(us1).union(frame2, Comparator.comparing(UserInfo::getKey1).thenComparing(UserInfo::getKey3)).show();

        System.out.println("------- 交集 -----");
        SDFrame.read(us1).intersection(frame2, Comparator.comparing(UserInfo::getKey1).thenComparing(UserInfo::getKey3)).show();

        System.out.println("------- retainAll -----");
        SDFrame.read(us1).retainAll(frame2, Comparator.comparing(UserInfo::getKey1).thenComparing(UserInfo::getKey3)).show();

        System.out.println("------- 差集 -----");
        SDFrame.read(us1).different(frame2, Comparator.comparing(UserInfo::getKey1).thenComparing(UserInfo::getKey3)).show();

        System.out.println("------- 减去 -----");
        SDFrame.read(us1).subtract(frame2, Comparator.comparing(UserInfo::getKey1).thenComparing(UserInfo::getKey3)).show();

        System.out.println();
    }

    @Test
    public void testOper3(){
        List<UserInfo> us1 = Arrays.asList(new UserInfo("a", 1,"99"), new UserInfo("a",11,"99"),new UserInfo("c",3,"88"), new UserInfo("b", 4,"77"));

        System.out.println("---- retainAllOther-1 -----");
        SDFrame.read(studentList)
                .retainAllOther(us1, CompareTwo.on(Student::getName,UserInfo::getKey1))
                .show();

        System.out.println("---- retainAllOther-2 -----");
        SDFrame.read(studentList)
               .retainAllOther(us1, CompareTwo.on(Student::getName,UserInfo::getKey1).thenOn(Student::getAge,UserInfo::getKey2))
               .show();

        System.out.println("---- differentOther-1 -----");
        SDFrame.read(studentList)
                .differentOther(us1, CompareTwo.on(Student::getName,UserInfo::getKey1))
                .show();

        System.out.println("---- differentOther-2 -----");
        SDFrame.read(studentList)
                .differentOther(us1, CompareTwo.on(Student::getName,UserInfo::getKey1).thenOn(Student::getAge,UserInfo::getKey2))
                .show();
    }
}
