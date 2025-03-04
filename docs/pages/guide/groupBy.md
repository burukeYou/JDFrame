# group

> ```
> group(Function<T,K> key)
> ```



一级分组，根据key进行分组





```java
// 根据学校名进行分组
// Map<学校名， 学生列表>
Map<String, List<Student>> map = frame.group(Student::getSchool).toMap(FI2::C1, FI2::C2);
```





# group2

> group2(Function<T, K> key, Function<T,J> key2)



`二级分组:` 先根据key进行分组， 组内再根据key2进行分组。



```java
// 先根据学校名分组，再根据年级进行分组
// Map<学校,Map<年级,学生列表>>
Map<String, Map<String, List<Student>>> map = SDFrame
            .read(studentList)
            .group2(Student::getSchool, Student::getLevel)
            .toMulti2Map(FI3::C1, FI3::C2, FI3::C3);

```





# groupBySum

> groupBySum(Function<T, K> key, NumberFunction<T,R> value)



`一级分组求和`： 根据key进行分组，然后计算组内value的和



```java
// 计算每个学校的年龄和
// 等价于SQL语意:  select school,sum(age) from student group by school
List<FI2<String, BigDecimal>> a = SDFrame
      .read(studentList)
      .groupBySum(Student::getSchool, Student::getAge)
      .toLists();
```



# group2BySum

> group2BySum(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value)



`二级分组求和`： 根据key进行分组，再根据key2进行分组， 然后计算组内value的和



```java
// 计算每个学校的每个年级的年龄和
// 等价于SQL语意:  select school,level,sum(age)  group by school,level
// FI3<学校名，年级，年龄和>
List<FI3<String, String, BigDecimal>> a = SDFrame
      .read(studentList)
      .group2BySum(Student::getSchool, Student::getLevel, Student::getAge)
      .toLists();
```



# group3BySum

>  group3BySum(Function<T, K> key,  Function<T, J> key2,Function<T, H> key3,NumberFunction<T,R> value)



`三级分组求和`： 根据key进行分组，再根据key2进行分组， 再根据key3进行分组,然后计算组内value的和



```java

// 计算每个学校的每个年级的每个姓名的年龄和
// 等价于SQL语意:  select school,level,name,sum(age)  group by school,level,name
// FI4<学校名，年级，姓名,年龄和>
List<FI4<String, String, String, BigDecimal>> a = SDFrame
    .read(studentList)
    .group3BySum(Student::getSchool, Student::getLevel, Student::getName, Student::getAge)
    .toLists();
```





# groupByCount

> groupByCount(Function<T, K> key)



`一级分组求数量`： 根据key进行分组，然后计算组内数量



```java
// 计算每个学校的学生数量
// 等价于SQL语意: select school,count(*) ... group by school
// FI2<学校名,学生数量>
List<FI2<String, Long>> a =  SDFrame
      .read(studentList)
      .groupByCount(Student::getSchool)
      .toLists();
```



# group2ByCount

> group2ByCount(Function<T, K> key, Function<T, J> key2)



`二级分组求数量`： 根据key进行分组，再根据key2进行分组， 然后计算组内数量





```java
// 计算每个学校每个年级的学生数量
// 等价于SQL语意: select school,level, count(*) ... group by school，level
// FI3<学校名,年级,学生数量>
List<FI3<String,Integer, Long>> a =  SDFrame
      .read(studentList)
      .group2ByCount(Student::getSchool)
      .toLists();
```





# groupByAvg

> groupByAvg(Function<T, K> key, NumberFunction<T,R> value)



`一级分组求平均值`： 根据key进行分组，然后计算组内value的平均值





```java
// 计算每个学校的平均年龄
// 等价于SQL语意:  select school,avg(age) from student group by school
// FI2<学校，平均年龄>
 List<FI2<String, BigDecimal>> a = SDFrame
      .read(studentList)
      .groupByAvg(Student::getSchool, Student::getAge)
      .toLists();
```



# group2ByAvg

> group2ByAvg(Function<T, K> key, Function<T, J> key2, NumberFunction<T,R> value)



`二级分组求平均值`： 根据key进行分组，再根据key2进行分组， 然后计算组内平均值



```java
// 计算每个学校的每个年级的平均年龄
// 等价于SQL语意:  select school,level, avg(age) from student group by school, level
// FI3<学校，年级，平均年龄>
 List<FI3<String, Integer,BigDecimal>> a = SDFrame
      .read(studentList)
      .group2ByAvg(Student::getSchool, Student::getLevel,Student::getAge)
      .toLists();
```







# groupByMax

> groupByMax(Function<T, K> key, Function<T, V> value)



`一级分组求最大的对象`： 根据key进行分组，然后计算组内value的最大的对象





```java
// 计算每个学校年龄最大的学生
// 等价于SQL语意:  select school,max(age) from student group by school
// FI2<学校，年龄最大的学生>
 List<FI2<String, Student>> a = SDFrame
      .read(studentList)
      .groupByMax(Student::getSchool, Student::getAge)
      .toLists();
```







# groupByMaxValue

> groupByMaxValue(Function<T, K> key, Function<T, V> value)



`一级分组求最大的值`： 根据key进行分组，然后计算组内value的最大值





```java
// 计算每个学校的最大年龄
// 等价于SQL语意:  select school,max(age) from student group by school
// FI2<学校，最大年龄>
 List<FI2<String, Integer>> a = SDFrame
      .read(studentList)
      .groupByMax(Student::getSchool, Student::getAge)
      .toLists();
```





# groupByMin

> groupByMin(Function<T, K> key, Function<T, V> value)



`一级分组求最小的对象`： 根据key进行分组，然后计算组内value的最小的对象





```java
// 计算每个学校年龄最小的学生
// 等价于SQL语意:  select school,min(age) from student group by school
// FI2<学校，年龄最小的学生>
 List<FI2<String, Student>> a = SDFrame
      .read(studentList)
      .groupByMin(Student::getSchool, Student::getAge)
      .toLists();
```





# groupByMinValue

> groupByMinValue(Function<T, K> key, Function<T, V> value)



`一级分组求最小的值`： 根据key进行分组，然后计算组内value的最小值





```java
// 计算每个学校的最小年龄
// 等价于SQL语意:  select school,min(age) from student group by school
// FI2<学校，最小年龄>
 List<FI2<String, Integer>> a = SDFrame
      .read(studentList)
      .groupByMinValue(Student::getSchool, Student::getAge)
      .toLists();
```





# groupByMaxMin

> groupByMaxMin(Function<T, K> key,Function<T, V> value)



`一级分组求值最大的和最小的对象`： 根据key进行分组，然后计算组内value值最大的对象和最小的对象



```java
// 计算每个学校的年龄最大的和年龄最小的学生
// 等价于SQL语意:  select school,min(age), max(age) from student group by school
// FI2<学校，年龄最大的和年龄最小的学生>
List<FI2<String, MaxMin<Student>>> a = SDFrame
      .read(studentList)
      .groupByMaxMin(Student::getSchool, Student::getAge)
      .toLists();
```







# groupByMaxMinValue

> groupByMaxMinValue(Function<T, K> key,Function<T, V> value)



`一级分组求最大值的和最小值`： 根据key进行分组，然后计算组内的value的最大和最小值



```java
// 计算每个学校的最大的年龄和最小的年龄
// 等价于SQL语意:  select school,min(age), max(age) from student group by school
// FI2<学校，最大的年龄和最小的年龄>
List<FI2<String, MaxMin<Integer>>> a = SDFrame
      .read(studentList)
      .groupByMaxMinValue(Student::getSchool, Student::getAge)
      .toLists();
```





# groupByConcat

> groupByConcat(Function<T, K> key, Function<T,V> concatField,CharSequence delimiter)



`一级分组求拼接`： 根据key进行分组，然后将组内的 concatFiel值按照分隔符进行拼接到一起



```java
// 计算每个学校的所有学生姓名的拼接字符串
// 等价于SQL语意:  select school, concat(name,";") from student group by school
// FI2<学校，拼接的姓名>
List<FI2<String, String>> a = SDFrame
      .read(studentList)
      .groupByConcat(Student::getSchool, Student::getName, ";")
      .toLists();
```









# groupByCustom

>  groupByCustom(Function<T, K> key, ListToOneValueFunction<T,V> function)



`一级自定义分组聚合：` 根据key进行分组， 然后将组内元素通过回调函数function聚合成一个值



```java
// 根据学校名进行分组， 然后计算组内的最大年龄
// FI2<学校名,最大年龄>
 List<FI2<String, Integer>> data = SDFrame
  .read(studentList)
  .groupByCustom(Student::getSchool,(list) -> SDFrame.read(list).maxValue(Student::getAge))
  .toLists();
```





# group2ByCustom

> group2ByCustom(Function<T, K> key, Function<T,J> key2, ListToOneValueFunction<T,V> function)



`二级自定义分组聚合：` 先根据key进行分组， 组内再根据key2进行分组，然后将组内元素通过回调函数function聚合成一个值





```java
// 计算每个学校名的每个年级的最大年龄
// FI2<学校名,年级,最大年龄>
 List<FI3<String, Integer, Integer>> data = SDFrame
    .read(studentList)
    .groupByCustom(Student::getSchool,Student::getLevel,(list) -> SDFrame.read(list).maxValue(Student::getAge))
    .toLists();
```
