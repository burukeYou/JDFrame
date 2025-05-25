

## 1、 引入依赖

建议使用最新版本， [版本列表见](https://central.sonatype.com/artifact/io.github.burukeyou/jdframe/versions)

```xml
<dependency>
    <groupId>io.github.burukeyou</groupId>
    <artifactId>jdframe</artifactId>
    <version>LATEST</version>
</dependency>
```



## 2、使用案例

接下来让我们使用`JDFrame`的API去统计每个学校的学生年龄在9到16岁间的学生的合计分数，然后去掉合计分数小于1000的学校， 最后统计合计分数最高的前10名的学校吧，代码如下

```java
// 待处理数据
List<Student> studentList = new ArrayList<>(); 

// 数据处理
List<FI2<String, BigDecimal>> sdf2 = SDFrame.read(studentList) // 转换成DataFrame模型
                .whereNotNull(Student::getAge)    // 过滤年龄不为null的
                .whereBetween(Student::getAge,9,16)   // 获取年龄在9到16岁之间的
                .groupBySum(Student::getSchool, Student::getScore) // 按照学校分组求和计算合计分数
                .whereGe(FI2::getC2,new BigDecimal(1000)) // 过滤合计分数大于等于1000的数据
                .sortDesc(FI2::getC2) // 按照分组后的合计分数降序排序
                .cutFirst(10)    // 截取前10名
                .toLists();     // 转换成List拿到结果
```



可以看到假设存在一个Student对象的数据列表，我们先使用SDFrame的read方法转换成DataFrame模型去进行数据处理， 通过链式调用数据筛选where、分组求和、排序、截取等方法去实现我们的业务逻辑。 从SQL语意上，上面的代码实现的功能等价于下面的SQL:



```sql
select school, sum(score)
from student 
where age >= 9 and age <= 16 and age is not null
group by school
having sum(score) >= 1000
order by sum(score) desc
limit 10
```

