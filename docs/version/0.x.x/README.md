







# 简介

`JDFrame`是一个仿SQL数据处理工具， 一个流式处理工具， 一个JVM层级的仿DataFrame模型工具，提供了DataFrame模型的若干基本功能比如复杂数据筛选、分组聚合、窗口函数、连接矩阵。除此之外，还会语意化和简化一些Java8的stream流式处理功能，提供更加强大的流式处理能力。









<h1>特性</h1>

- **无侵入**：  非常轻量级无需任何依赖和配置环境， 使用它就像使用一个Util那么简单
- **DataFrame模型**:    JVM层级的仿DataFrame模型实现，涵盖了模型的大部分常用功能。同时，支持强类型，既不会导致数据类型的丢失，也无需进行硬编码操作。
- **类SQL的语义**： 如果您会 SQL 语言，基本上不需要花费什么学习精力。多数 API 基本都是依据 SQL 的语义来进行 Java 的实现，具有极高的可读性
- **强大的流式处理能力:**     在原有 Java8 的 stream 流的基础之上，提供了更为强大且更为简洁的流式处理能力。就理念而言，它并非是对 stream 的替换，而是对 stream 的继承
- **极简式API:**     把业务功能或者冗长繁杂的数据处理功能凝聚成一个极简且语意明晰的 API，以供开发者使用





---------





# 快速开始



##  引入依赖



建议使用最新版本



```xml
<dependency>
    <groupId>io.github.burukeyou</groupId>
    <artifactId>jdframe</artifactId>
    <version>0.1.6</version>
</dependency>
```





## 使用案例

接下来让我们使用SDFrame去统计每个学校的学生年龄在9到16岁间的学生的合计分数，然后去掉合计分数小于1000的学校， 最后统计合计分数最高的前10名的学校吧，代码如下

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





---------





# 必须知道

## SDFrame



SDFrame就是我们的DataFrame模型了， 可通过read方法将集合、Map等数据转换为该模型进行复杂的数据处理. 具体有哪些可使用的API见下文.





## FI是什么

FI类就是用于描述动态表格的列头类， 其实在各种API的结果列表我们可以经常看到FI2、FI3、FI4等对象.    这些对象就是我们的FI类。 区别就是如果矩阵有两列就会用FI2存储, 有三列就用FI3存储，FI4类比同理。  FI类里面有c1、c2、c3等字段分别表示第几列的结果, c1就表示第一列的结果, c2就表示第2列的结果, c3，c4同理







<br>





# API文档

## read

> read(List<R> list)      读取列表
>
> read(Map<K,V> map)   读取Map
>
> readMap(Map<K,Map<J,V>> map)    读取多级Map



读取其他数据成Frame模型进行数据处理.   如果是对象类型， 则Frame模型的表头就是该对象的所有字段



```java
// 读取列表
List<Student> studentList;
SDFrame<Student> frame =  SDFrame.read(studentList);

// 读取Map。  自动转成FI2进行处理, 
Map<String,Integer> map;
SDFrame<FI2<String, Integer>> frame = SDFrame.read(map);

// 读取多级Map.      自动转成FI3进行处理, 
Map<String,Map<Integer,Long>> map;
SDFrame<FI3<String, Integer,Long>> frame = SDFrame.read(map);
```



## show

> show()
>
> show(int n)



打印前N行数据成表格到控制台



```java
 SDFrame.read(studentList).show(30);
```







## toLists

> toLists()



将Frame模型转换成列表



```jade
SDFrame<Student> frame = SDFrame.read(studentList)
List<Student> data = frame.toLists();
```





## toArray

> toArray()



将Frame模型转换成数组





```jade
SDFrame<Student> frame = SDFrame.read(studentList)
Student[] arr = frame.toArray();
```





## toMap

> toMap(Function<T, K> keyMapper, Function<T,V> valueMapper)



将Frame模型转换成Map.  key就是keyMapper， value就是valueMapper



```java
Map<Integer,String> map = SDFrame.read(studentList).toMap(Student::getId, Student::getName);
```





## toMulti2Map

> toMulti2Map(Function<T, K> keyMapper, Function<T, K2> key2Mapper, Function<T, V> valueMapper)



将Frame模型转换成二层级Map.  

- 第一层Map的key就是keyMapper，第二层的Map的key就是key2Mapper，  value就是valueMapper



```java
Map<String,Map<Integer,Long>> map = SDFrame
    .read(studentList)
    .toMulti2Map(Student::getSchool, Student::getLevel,Student::getId);
```



## toMulti3Map

> toMulti3Map(Function<T, K> keyMapper, Function<T, K2> key2Mapper, Function<T, K3> key3Mapper,Function<T, V> valueMapper)



将Frame模型转换成三层级Map.  

- 第一层Map的key就是keyMapper，第二层的Map的key就是key2Mapper，第三层的Map的key就是key3Mapper， value就是valueMapper



```java
Map<String,Map<Integer,Map<String,Long>>> map = SDFrame
    .read(studentList)
    .toMulti3Map(Student::getSchool, Student::getLevel,Student::getName,Student::getId);
```







## stream

> stream()



获取Frame模型的stream流



```jade
SDFrame<Student> frame = SDFrame.read(studentList)
Stream<Student> = frame.stream();
```



## isEmpty

> isEmpty()



判断模型列表数据是否为空



```java
boolean flag = SDFrame.read(studentList).isEmpty();
```





## isNotEmpty

> isNotEmpty()



判断模型列表数据是否不为空



```java
boolean flag = SDFrame.read(studentList).isNotEmpty();
```



## contains

> contains(T other)



是否存在指定元素



```java
boolean flag = SDFrame.read(studentList).contains(new Student("a","b"));
```



## containsValue

>  containsValue(Function<T,U> valueFunction, U value)



某列是否存在指定值



```java
// 判断学生里是否存在名字叫张三的同学
boolean flag = SDFrame.read(studentList).containsValue(Student::getName,"张三");
```







## head

> head()		获取第一个元素
>
> head(int n)	获取前N个元素



获取前N个元素返回



```java
// 获取第一个元素
Student firstStudent = SDFrame.read(studentList).head();

// 获取前10个元素
List<Student> topNList = SDFrame.read(studentList).head(10);
```



## tail

> tail()		获取最后一个元素
>
> tail(int n)	获取后N个元素



获取最后N个元素返回



```java
// 获取最后一个元素
Student firstStudent = SDFrame.read(studentList).tail();

// 获取后10个元素
List<Student> topNList = SDFrame.read(studentList).tail(10);
```



## page

> page(int page,int pageSize)



获取第page页的元素返回



```java
// 每页大小是5， 以此获取第3页的数据返回
List<Student> pageList = SDFrame.read(studentList).page(3,5);
```









## col

> col(Function<T, R> function)



获取Frame表格的某一列数据



```java
// 获取所有学生姓名
List<String> nameList =  SDFrame.read(studentList).col(Student::getName);
```



## map

> map(Function<T,R> map)



映射准换成其他数据类型,   与stream().map 语意一致



```java
// SDFrame<Student>  转换成   SDFrame<UserInfo>
SDFrame<UserInfo> frame = SDFrame.read(studentList).map(e -> new UserInfo(e.getId));
```





## mapParallel

> mapParallel(Function<T,R> map)



并行映射准换成其他数据类型， 注意映射顺序可能与列表顺序不一致





```java
// SDFrame<Student>  转换成   SDFrame<UserInfo>
SDFrame<UserInfo> frame = SDFrame.read(studentList).mapParallel(e -> new UserInfo(e.getId));
```



## joining

> joining(Function<T,U> joinField,CharSequence delimiter)



将数值按照指定分隔符拼接起来返回



```java
// 将学生的id按照分隔符“，”拼接起来返回
String idStr = SDFrame.read(studentList).joining(Student::getId,",");
```







## partition

> partition(int n)



分区： 将表格切割成多个小表格，每个表格行数为n（最后一个表格行数可能小于n），用于将大任务拆成小任务



```java
// 将列表分区，每个区大小为5个
List<List<Student>> taskList = SDFrame.read(studentList).partition(5).toLists();
```





## addRowNumberCol

> addRowNumberCol()      生成行号到新的列
>
> addRowNumberCol(SetFunction<T,Integer> set)     生成行号到指定列
>
> addRowNumberCol(Sorter<T> sorter)       根据Sorter排序生成行号到新的列
>
> addRowNumberCol(Sorter<T> sorter,SetFunction<T,Integer> set)   根据Sorter排序生成行号到指定列



给每行生成行号，行号从1开始



```java
// 1、生成行号到新的c2列
List<FI2<Student,Integer>> data = SDFrame.read(studentList).addRowNumberCol().toLists();

// 2、生成行号到id列
List<Student> data = SDFrame.read(studentList).addRowNumberCol(Student::setId).toLists();

// 3、根据年龄降序排序生成行号新的c2列
List<FI2<Student,Integer>> data = SDFrame
  	.read(studentList)
  	.addRowNumberCol(Sorter.sortDscBy(Student::getAge))
  	.toLists();

// 4、根据年龄降序排序生成行号到id列
List<FI2<Student,Integer>> data = SDFrame
  	.read(studentList)
  	.addRowNumberCol(Sorter.sortDscBy(Student::getAge), Student::setId)
  	.toLists();
```



## addRankCol

> addRankCol(Sorter<T> sorter)      生成排名号到新的列
>
> addRankCol(Sorter<T> sorter, SetFunction<T,Integer> set)   生成排名号到指定列



给每行生成排名号，排名号从1开始。  相同值排名号一样，并且排名不连续. 比如 1,2,2,2,5,6,7



```java
// 根据年龄降序排序生成排名号新的c2列
List<FI2<Student,Integer>> data = SDFrame
  	.read(studentList)
  	.addRankCol(Sorter.sortDscBy(Student::getAge))
  	.toLists();

// 根据年龄降序排序生成行号到id列
List<FI2<Student,Integer>> data = SDFrame
  	.read(studentList)
  	.addRankCol(Sorter.sortDscBy(Student::getAge), Student::setId)
  	.toLists();
```



## distinct

> distinct()						根据对象去重
>
> distinct(Function<T, R> function)     根据字段值去重
>
> distinct(Comparator<T> comparator)   根据比较器去重
>
> distinct(Function<T, R> function, ListSelectOneFunction<T> listOneFunction)   根据字段值去重，并自定义重复元素的取舍



去除重复的元素

- 如果存在重复元素（大于1个）会将重复的元素回调给ListSelectOneFunction函数， 该函数去自定义判断选取哪个元素作为去重后的结果



```java
// 根据Student对象去重
List<Student> data = SDFrame.read(studentList).distinct().toLists();

// 根据学生姓名去重复
List<Student> data = SDFrame.read(studentList).distinct(Student::getName).toLists();

// 根据学生姓名和学生年龄去重
List<Student> data = SDFrame.read(studentList).distinct(Comparator.comparing(Student::getName).thenComparing(Student::getAge)).toLists();

// 根据学生姓名去重复， 如果学生姓名一样，则保留年龄最大的那个学生
List<Student> data = SDFrame
  .read(studentList)
  .distinct(Student::getName,(list) -> SDFrame.read(list).max(Student::getAge))
  .toLists();
```





## replenish

> replenishList()         返回缺失的条目
>
> replenish()               补充缺失的条目到当前frame
>
> replenishGroup()     分组补充缺失的条目到当前frame



`补充条目`：   根据某字段汇总所有条目， 然后与指定条目集对比， 对缺失的部分条目进行返回或者增加



```java

// 1、返回缺失的学校的学生
// 汇总frame所有学校， 然后与指定条目集allDim对比得出差集。 然后将差集的学校手动new补充
List<String> allDim = Arrays.asList("一中","二中","三中","四中");
List<Student> otherList = SDFrame
  .read(studentList)
  .replenishList(Student::getSchool,  // 汇总的学校
                 allDim,   // 指定条目集
                 (school) -> new Student(school,"补充的元素")); // 缺失的学校会回调该函数做补充


// 2、补充缺失的学校的学生。 
SDFrame<Student> frame = SDFrame
  .read(studentList)
  .replenish(Student::getSchool,  // 汇总的学校
                 allDim,   // 指定条目集
                 (school) -> new Student(school,"补充的元素")); // 缺失的学校会回调该函数做补充

// 3、分组补充缺失的学生,  先汇总所有学生姓名， 然后在分组里判断该分组是否缺少哪些学生姓名。 如果缺失将分组的学校和缺失的姓名进行回调到函数, 手动进行补充
SDFrame<Student> frame = SDFrame
  .read(studentList)
  .replenishGroup(Student::getSchool,  //按照学校进行分组
                  Student::getName,  // 汇总所有学生姓名
                 (school, name) -> new Student(name,school,"补充的元素")); // 缺失的分组学校和学生姓名会回调该函数做补充

```











## 排序



### sort

> sort(Sorter<T> comparator)



根据Sorter进行排序



```java
// 将学生先按照学校名升序排序，如果顺序一样再按照年龄降序排序
// 等价于SQL语意：   select * from student order by school asc, age desc;
SDFrame
  .read(studentList)
  .sort(Sorter.sortAscBy(Student::getSchool).sortDesc(Student::getAge));
```





### sortAsc

> sortAsc(Function<T, R> function)     根据字段值升序排序
>
> sortAsc(Comparator<T> comparator)     根据比较器升序排序



升序排序



```java
// 将学生按照年龄升序排序
// 等价于SQL语意：   select * from student order by age asc
SDFrame.read(studentList).sortAsc(Student::getAge);
SDFrame.read(studentList).sortAsc(Comparator.comparing(Student::getAge));
```





### sortDesc

> sortDesc(Function<T, R> function).   根据字段值降序排序
>
> sortDesc(Comparator<T> comparator)    根据比较器降序排序



降序排序



```java
// 将学生按照年龄降序排序
// 等价于SQL语意：   select * from student order by age desc
SDFrame.read(studentList).sortDesc(Student::getAge);
SDFrame.read(studentList).sortDesc(Comparator.comparing(Student::getAge));
```







## 截取

### cutFirst

> cutFirst(int n)



保留前N个元素



```java
SDFrame<Student> frame = SDFrame.read(studentList).cutFirst(10);
```



### cutLast

> cutLast(int n)



保留后N个元素



```java
SDFrame<Student> frame = SDFrame.read(studentList).cutLast(10);
```



### cut

> cut(Integer startIndex,Integer endIndex



保留索引范围[startIndex,endIndex)内的元素.  



```java
SDFrame<Student> frame = SDFrame.read(studentList).cut(3,5);
```



### cutPage

> cutPage(int page,int pageSize)



保留第page页的元素



```java
// 每页大小是5， 以此保留第3页的元素
SDFrame<Student> frame = SDFrame.read(studentList).cutPage(3,5);
```









## 遍历



### forEachDo

> forEachDo(Consumer<? super T> action)



迭代遍历每个元素给到Consumer函数。  等价于List.forEach



```java
 SDFrame.read(studentList).forEachDo((student) -> {
     System.out.println("id: " + student.getId());
 });
```



### forEachParallel

> forEachParallel(Consumer<? super T> action)



并行的遍历每个元素给到Consumer函数， 注意此时遍历顺序可能与列表顺序不一致



```java
 SDFrame.read(studentList).forEachParallel((student) -> {
     System.out.println("id: " + student.getId());
 });
```







### forEachIndexDo

> forEachIndexDo(ConsumerIndex<? super T> action)



迭代遍历每个元素和当前遍历的下标索引(从0开始)给到ConsumerIndex函数



```java
SDFrame.read(studentList).forEachIndexDo((index, student) -> {
    System.out.println("name: " + student.getName() + ", index:" + index);
})
```



### forEachPreDo

> forEachPreDo(ConsumerPrevious<? super T> action)



迭代遍历每个元素和该元素的前一个元素给到ConsumerPrevious函数， 如果当前元素是第一个元素，则回调的前一个元素为null



```java
SDFrame.read(studentList).forEachPreDo((pre,cur) -> {
    // 如果前一个元素pre为null，说明当前元素cur是第一个元素
    if (pre != null){
        System.out.println("之前元素: " + pre.getId() + "   当前元素" + cur.getId());
    }
});
```





### forEachNextDo

> ```
> forEachNextDo(ConsumerNext<? super T> action)
> ```



迭代遍历每个元素和该元素的下一个元素给到ConsumerNext函数， 如果当前元素是最后一个元素，则回调的下一个元素为null



```java
SDFrame.read(studentList).forEachNextDo((cur,next) -> {
  // 如果下一个元素next为null，说明当前元素cur为最后一个元素
  if (next != null){
    	 System.out.println("当前元素: " + cur.getId() + "   下一个元素" + next.getId());
  }
});
```







## 筛选



### whereNull

>  whereNull(Function<T, R> function)      



过滤数据值为null的数据，兼容的字符串类型的判断，如果字符串为空也认为是null。 `等价于SQL语义:  where name is null or name = ''`



```java
// 过滤学校名字为null的数据。  等价于SQL语义:  where name is null or name = ''
SDFrame
  .read(studentList)
  .whereNull(Student::getName)
  .show()
```





### whereNotNull

>  whereNotNull(Function<T, R> function)      



过滤数据值不为null的数据，兼容的字符串类型的判断，字符串不为null也不为空才算非null. `等价于SQL语义:  where name is not null and name != ''`



```java
// 过滤学校名字不为null的数据               
SDFrame
  .read(studentList)
  .whereNotNull(Student::getName)
  .show()
```





### whereBetween

>  whereBetween()      -过滤范围在[start,end]之间数据，区间是前闭后闭
>
> whereBetweenN()     -过滤范围在(start,end)之间数据，区间是前开后开
>
> whereBetweenR()     -过滤范围在(start,end]之间数据，区间是前开后闭
>
> whereBetweenL()   -过滤范围在[start,end)之间数据，区间是前闭后开
>
> whereNotBetween()  -过滤范围在[start,end]之外的数据， 包含边界值
>
> whereNotBetweenN()    -过滤范围在(start,end)之外的数据， 不包含边界值





```java
SDFrame
  .read(studentList)
  .whereBetween(Student::getAge,3,6)  // 过滤年龄在[3，6]岁间的数据。 包含3岁、6岁
  .whereBetweenN(Student::getAge,3,6)  // 过滤年龄在(3，6)岁间的数据。 不包含3岁、6岁
  .whereBetweenR(Student::getAge,3,6)  // 过滤年龄在(3，6]岁间的数据。 包含6岁,不包含3岁
  .whereBetweenL(Student::getAge,3,6)  // 过滤年龄在[3，6)岁间的数据。 包含3岁,不包含6岁
  .whereNotBetween(Student::getAge,3,6)  // 过滤年龄在[3，6]之外的数据。 包含3岁、6岁
  .whereNotBetweenN(Student::getAge,3,6)  // 过滤年龄在[3，6]之外的数据。 不包含3岁、6岁
  .show()
```



等价于SQL语意:

```sql
where age between 3 and 6
```





### whereIn

> whereIn(Function<T, R> function, List<R> list)   



筛选数据值在指定列表内的数据. 等价于SQL语意 `where age in (3,7,8) `



```java
// 过滤年龄为3岁 或者7岁 或者 8岁的数据
SDFrame
  .read(studentList)
  .whereIn(Student::getAge,  Arrays.asList(3,7,8)) 
  .show()
```





### whereNotIn

> whereNotIn(Function<T, R> function, List<R> list)   



筛选数据值不在指定列表内的数据. 等价于SQL语意 `where age not in (3,7,8) `



```java
// 过滤年龄不为3岁,7岁,8岁的数据
SDFrame
  .read(studentList)
  .whereNotIn(Student::getAge,  Arrays.asList(3,7,8)) 
  .show()
```







### whereTrue

>  whereTrue(Predicate<T> predicate). 



过滤断言条件为true的数据



```java
// 过滤学生年龄大于10岁或者年级等于3的数据
SDFrame
  .read(studentList)
  .whereTrue(student -> student.getAge() > 10 or student.getLevel() = 3)
  .show()
```





使用建议： 有复杂过滤条件或者or关系时使用



### whereNotTrue

>  whereNotTrue(Predicate<T> predicate). 



过滤断言条件不为true的数据， 相当于whereTrue取反



```java
// 过滤学生年龄不等于3的数据
SDFrame
  .read(studentList)
  .whereNotTrue(student -> student.getLevel() = 3)
  .show()
```





### whereEq

> whereEq(Function<T, R> function, R value)



过滤数据值为指定值的数据。  等价于SQL语意:  `where age = 3`



```java
// 过滤学生年龄等于3岁的数据
SDFrame
  .read(studentList)
  .whereEq(Student::getAge,3)
  .show()
```





### whereNotEq

> whereNotEq(Function<T, R> function, R value)



过滤数据值不为指定值的数据。  等价于SQL语意:  `where age != 3`



```java
// 过滤学生年龄不等于3岁的数据
SDFrame
  .read(studentList)
  .whereNotEq(Student::getAge,3)
  .show()
```





### whereGt

> whereGt(Function<T, R> function, R value)



过滤数据值大于指定值的数据。  等价于SQL语意:  `where age > 3`



```java
// 过滤学生年龄大于3岁的数据
SDFrame
  .read(studentList)
  .whereGt(Student::getAge,3)
  .show()
```



### whereGe

> whereGe(Function<T, R> function, R value)



过滤数据值大于等于指定值的数据。  等价于SQL语意:  `where age >= 3`



```java
// 过滤学生年龄大于等于3岁的数据
SDFrame
  .read(studentList)
  .whereGe(Student::getAge,3)
  .show()
```



### whereLt

> whereLt(Function<T, R> function, R value)



过滤数据值小于指定值的数据。  等价于SQL语意:  `where age < 3`



```java
// 过滤学生年龄小于于3岁的数据
SDFrame
  .read(studentList)
  .wherelt(Student::getAge,3)
  .show()
```





### whereLe

> whereLe(Function<T, R> function, R value)



过滤数据值小于等于指定值的数据。  等价于SQL语意:  `where age <= 3`



```java
// 过滤学生年龄小于等于3岁的数据
SDFrame
  .read(studentList)
  .whereLe(Student::getAge,3)
  .show()
```





### whereLike

> whereLike(Function<T, R> function, R value)



模糊查询数据值包含指定值的数据。  等价于SQL语意:  `where name like "%jay%"`



```java
// 模糊查询学生姓名包含jay的数据
SDFrame
  .read(studentList)
  .whereLike(Student::getName,"jay")
  .show()
```







### whereNotLike

> whereNotLike(Function<T, R> function, R value)



模糊查询数据值不包含指定值的数据。  等价于SQL语意:  `where name not like "%jay%"`



```java
// 模糊查询学生姓名不包含jay的数据
SDFrame
  .read(studentList)
  .whereNotLike(Student::getName,"jay")
  .show()
```



### whereLikeLeft

>  whereLikeLeft(Function<T, R> function, R value)
>
> 

模糊查询数据值包含指定前缀值的数据。  等价于SQL语意:  `where name  like "jay%"`





```java
// 模糊查询学生姓名前缀为jay的数据
SDFrame
  .read(studentList)
  .whereLikeLeft(Student::getName,"jay")
  .show()
```



### whereLikeRight

>  whereLikeRight(Function<T, R> function, R value)
>
> 

模糊查询数据值包含指定后缀值的数据。  等价于SQL语意:  `where name  like "%jay"`



```java
// 模糊查询学生姓名后缀为jay的数据
SDFrame
  .read(studentList)
  .whereLikeLeft(Student::getName,"jay")
  .show()
```



## 汇总



### sum

> sum(Function<T, R> function)



计算数据值的和

- 会自动过滤掉null值的计算， 防止空指针异常



```java
// 计算学生的年龄和
BigDecimal sum = SDFrame.read(studentList).sum(Student::getAge);
```





### Avg

> avg(Function<T, R> function)



计算数据值的平均值

- 会自动过滤掉null值， null值不会参与平均值的计算



```java
// 计算学生的年龄的平均值
BigDecimal sum = SDFrame.read(studentList).avg(Student::getAge);
```





### max

>  max(Function<T, R> function)



计算数值最大的对象



```java
// 获取年龄最大的学生
Student maxStudent = SDFrame.read(studentList).max(Student::getAge);
```





### maxValue

> maxValue(Function<T, R> function)



计算最大的数值



```java
// 获取最大的年龄
Integer maxAge = SDFrame.read(studentList).maxValue(Student::getAge);
```



### min

> min(Function<T, R> function)



计算数值最小的对象



```java
// 获取年龄最小的学生
Student maxStudent = SDFrame.read(studentList).min(Student::getAge);
```





### minValue

> minValue(Function<T, R> function)



计算最小的数值



```java
// 获取最小的年龄
Integer minAge = SDFrame.read(studentList).minValue(Student::getAge);
```







### maxMin

> maxMin(Function<T, R> function)



计算数值最大的对象和数值最小的对象



```java
// 获取年龄最大和年龄最小的学生
MaxMin<Student> mm = SDFrame.read(studentList).maxMin(Student::getAge);
```



### maxMinValue

> maxMinValue(Function<T, R> function)



计算最大和最小的数值



```java
// 获取最大的年龄和最小的年龄
MaxMin<Integer> mm = SDFrame.read(studentList).maxMinValue(Student::getAge);
```



### count

> count()



获取行数



```java
// 获取数量
long cunt = SDFrame.read(studentList).count();
```





### countDistinct

> countDistinct(Comparator<T> comparator)   -根据Comparator去重后获取行数
>
> countDistinct(Function<T, R> function)     -根据字段数值去重后获取行数





```java
// 获取不重复的学生名数量
long count = SDFrame.read(studentList).countDistinct(Student::Name);
```







## 分组聚合

### group

> ```
> group(Function<T,K> key)
> ```



一级分组，根据key进行分组





```java
// 根据学校名进行分组
// Map<学校名， 学生列表>
Map<String, List<Student>> map = frame.group(Student::getSchool).toMap(FI2::C1, FI2::C2);
```





### group2

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





### groupBySum

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



### group2BySum

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



### group3BySum

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





### groupByCount

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



### group2ByCount

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





### groupByAvg

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



### group2ByAvg

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







### groupByMax

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







### groupByMaxValue

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





### groupByMin

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





### groupByMinValue

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





### groupByMaxMin

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







### groupByMaxMinValue

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





### groupByConcat

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









### groupByCustom

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





### group2ByCustom

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





## 集合运算 

### unionAll

> unionAll(Collection<T> other)



求当前集合和其他集合的并集， 但是不会去除重复



```java

// 计算studentList和otherList的并集
// 等价于SQL语意：    select * from studentList union all select * from otherList
List<Student> studentList, otherList;
List<Student> data = SDFrame.read(studentList).unionAll(otherList).toList();
```



### union

> union(Collection<T> other, Comparator<T> comparator)



求当前集合和其他集合的并集， 会根据指定的comparator去除重复



```java
// 计算studentList和otherList的并集, 并根据学生年龄进行去重
// 等价于SQL语意：    select * from studentList union  select * from otherList
List<Student> studentList, otherList;
List<Student> data = SDFrame
      .read(studentList)
      .union(otherList,Comparator.comparing(Student::getAge))
      .toList();
```





### retainAll

> retainAll(Collection<T> other,Comparator<T> comparator)



保留当前集合中在其他集合内存在的元素， 会根据指定的comparator判断是否存在



```java
// 返回studentList内在集合otherList内存在的元素（根据姓名在otherList内判断是否存在）
List<Student> studentList, otherList;
List<Student> data = SDFrame
      .read(studentList)
      .union(otherList,Comparator.comparing(Student::getName))
      .toList();
```





### retainAllOther

> retainAllOther(Collection<K> other, CompareTwo<T,K> comparator)



保留当前集合中在其他集合(类型可以不一样)内存在的元素， 会根据指定的CompareTwo判断是否存在



```java
// 返回studentList内在集合otherList内存在的元素（根据Student姓名和UserInfo姓名在otherList内判断是否存在）
List<Student> studentList;
List<UserInfo> otherList;
List<Student> data = SDFrame
    .read(studentList)
    .retainAllOther(otherList, CompareTwo.on(Student::getName,UserInfo::getName))
    .show();
```



### intersection

> intersection(Collection<T> other,Comparator<T> comparator)



计算当前集合和其他集合的交集(共同元素).     根据指定的comparator判断是否相交



```java
// 返回studentList和集合otherList的交集（根据姓名判断是否相交）
List<Student> studentList, otherList;
List<Student> data = SDFrame
      .read(studentList)
      .intersection(otherList,Comparator.comparing(Student::getName))
      .toList();
```





### different

> different(Collection<T> other,Comparator<T> comparator)



计算当前集合与其他集合的差集.     即当前集合中不存在于其他集合内的元素.    根据指定的comparator判断是否不相交



```java
// 返回studentList与集合otherList的查集（根据姓名判断是否不相交）
List<Student> studentList, otherList;
List<Student> data = SDFrame
      .read(studentList)
      .different(otherList,Comparator.comparing(Student::getName))
      .toList();
```



### differentOther

> differentOther(Collection<K> other, CompareTwo<T,K> comparator)



计算当前集合与其他集合(类型可以不一样)的差集.     即当前集合中不存在于其他集合内的元素.    根据指定的CompareTwo判断是否不相交



```java
// 返回studentList内不存在于集合otherList内的元素（根据Student姓名和UserInfo姓名在otherList内判断是否不相交）
List<Student> studentList;
List<UserInfo> otherList;
List<Student> data = SDFrame
    .read(studentList)
    .differentOther(otherList, CompareTwo.on(Student::getName,UserInfo::getName))
    .show();
```



### subtract

> subtract(Collection<T> other,Comparator<T> comparator)



当前集合减去其他集合,  根据指定的comparator判断是否相同, 如果相同则相减. 



```java
// studentList 减去 otherList 
List<Student> studentList, otherList;
List<Student> data = SDFrame
      .read(studentList)
      .subtract(otherList,Comparator.comparing(Student::getName))
      .toList();
```



## 表连接 



### join

> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> joinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> joinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);
>
> joinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join)



<br>



`内连接： ` 根据关联条件`JoinOn`关联其他表， 如果关联成功，将关联的两个对象回调给`Join`函数或者`VoidJoin`函数

- `join`:     内连接其他数据.  (与SQL `inner join`语义一致)
- `joinOnce`：    与join不同， 只会内连接一条数据
- `joinVoid`:     执行内连接操作， 与join区别是不会改变Frame的数据内容和行数，只做关联操作。 并把关联的两个对象回调给VoidJoin对象函数
- `joinOnceVoid`:   与joinVoid不同， 只会执行一次内连接操作



<br>



示例代码:

```java
// 学生表
SDFrame<Student> stuFrame = SDFrame.read(studentList);
// 用户表
SDFrame<Student> userFrame = SDFrame.read(userInfoList);

/* 等价于SQL语意:
		selet s.name,u.id,u.user_name
		from student s inner join user u  on  s.name = u.name
*/
SDFrame<StuDTO> data =  stuFrame.join(userFrame,
                 // 关联条件                 
                 JoinOn.on(Student::getName, UserInfo::getName),
                 // 关联操作                     
                 (stu, user) -> {
                        StuDTO dto = new StuDTO();
                        dto.setName(stu.getName());
                        dto.setUserId(user.getId());
                        dto.setUserName(user.getUserName());
                        return dto;
            	   });
```







### leftJoin

> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> leftJoinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> leftJoinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);
>
> leftJoinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join)



<br>



`左连接:`  以当前表为主表，关联其他表， 如果根据关联条件`JoinOn`关联成功，将关联的两个对象回调给`Join`函数或者`VoidJoin`函数， 如果关联失败，只会将主表对象回调给`Join`函数， 而关联对象会回调为null

- `leftJoin`:     左连接其他数据。 (与SQL `left join`语义一致)
- `leftJoinOnce`：    与leftJoin不同， 只会左连接一条数据
- `leftJoinVoid`:     执行左连接操作， 与leftJoin区别是不会改变Frame的数据内容和行数，只做关联操作。 并把关联的两个对象回调给VoidJoin对象函数
- `leftJoinOnceVoid`:   与leftJoinVoid不同， 只会执行一次左连接操作



<br>



示例代码:

```java
// 学生表
SDFrame<Student> stuFrame = SDFrame.read(studentList);
// 用户表
SDFrame<Student> userFrame = SDFrame.read(userInfoList);

/* 等价于SQL语意:
		selet s.name,u.id,u.user_name
		from student s left join user u  on  s.name = u.name
*/
SDFrame<StuDTO> data =  stuFrame.leftJoin(userFrame,
                 // 关联条件                 
                 JoinOn.on(Student::getName, UserInfo::getName),
                 // 关联操作                     
                 (stu, user) -> {
                        if(user = null){
                          // 如果左连接失败，user会回调为null,需手动判断
                          user = new User();
                        }
                   
                        StuDTO dto = new StuDTO();
                        dto.setName(stu.getName());
                        dto.setUserId(user.getId());
                        dto.setUserName(user.getUserName());
                        return dto;
            	   });
```









### rightJoin

> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> rightJoinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> rightJoinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);
>
> rightJoinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join)



<br>



`右连接:`  以其他表为主表，关联当前表， 如果根据关联条件`JoinOn`关联成功，将关联的两个对象回调给`Join`函数或者`VoidJoin`函数， 如果关联失败，只会将主表对象回调给`Join`函数， 而关联对象会回调为null

- `rightJoin`:     左连接其他数据。 (与SQL `right join`语义一致)
- `rightJoinOnce`：    与rightJoin不同， 只会右边连接一条数据
- `rightJoinVoid`:     执行右连接操作， 与rightJoin区别是不会改变Frame的数据内容和行数，只做关联操作。 并把关联的两个对象回调给VoidJoin对象函数
- `rightJoinOnceVoid`:   与rightJoinVoid不同， 只会执行一次左连接操作



<br>



示例代码:

```java
// 学生表
SDFrame<Student> stuFrame = SDFrame.read(studentList);
// 用户表
SDFrame<Student> userFrame = SDFrame.read(userInfoList);

/* 等价于SQL语意:
		selet s.name,u.id,u.user_name
		from student s right join user u  on  s.name = u.name
*/
SDFrame<StuDTO> data =  stuFrame.rightJoin(userFrame,
                 // 关联条件                 
                 JoinOn.on(Student::getName, UserInfo::getName),
                 // 关联操作                     
                 (stu, user) -> {
                        if(stu = null){
                          // 如果右连接失败，stu会回调为null,需手动判断
                          stu = new Student();
                        }
                   
                        StuDTO dto = new StuDTO();
                        dto.setName(stu.getName());
                        dto.setUserId(user.getId());
                        dto.setUserName(user.getUserName());
                        return dto;
            	   });
```











## 爆炸函数

`explod爆炸函数：` 将集合、数组、结构体中的元素按照某种规则拆分成多行。 



### explodeString

> explodeString(Function<T,String> getFunction, String delimiter)   爆炸后的值放到FI2的c2列中
>
> explodeString(Function<T,String> getFunction, SetFunction<T,String> setFunction,String delimiter) 爆炸后的值放到SetFunction中



对`字符串`进行爆炸：  对某列字符串类型数值按照指定分隔符进行分隔， 将每个分隔后的值单独作为新的一行加入到Frame中.  分隔符支持正则表达式. 



```java
// 将学生标签字段tag的值按照分隔符";"进行爆炸拆分成多行.
// 比如tag字段值格式： 二次元;唱歌;舞蹈
SDFrame.read(studentList).explodeString(Student::getTag,";");
```





### explodeJsonArray

> explodeJsonArray(Function<T,String> getFunction)
>
> explodeJsonArray(Function<T,String> getFunction,SetFunction<T,String> setFunction)



对`json字符串数组`进行爆炸：  将json数组拆分，拆分后的值就是数组的每个元素， 这些元素单独作为新的一行加入到Frame中



```java

// 假设tag字段值格式为json字符串数组，比如["二次元","唱","舞蹈"]

//1、将tag字段炸开后放到新的c2列中
SDFrame<FI2<Student, String>> frame = SDFrame
  .read(studentList)
  .explodeJsonArray(Student::getTag)
  
// 2、将tag字段炸开后放到 type字段中
SDFrame<Student> frame = SDFrame
  .read(studentList)
  .explodeJsonArray(Student::getTag,Student::setType)
```





### explodeCollection

> explodeCollection(Function<T,? extends Collection<E>> getFunction)
>
> explodeCollection(Function<T,? extends Collection<E>> getFunction,SetFunction<T,E> setFunction)



对`集合Collection` 类型进行爆炸：  集合的每个元素单独作为新的一行加入到Frame中





```java
// 假设tag字段类型为Collection： List<String> tag = Arrays.asList("二次元","舞蹈")

//1、将tag字段炸开后放到新的c2列中
SDFrame<FI2<Student, String>> frame = SDFrame
  .read(studentList)
  .explodeCollection(Student::getTag)
  
// 2、将tag字段炸开后放到 type字段中
SDFrame<Student> frame = SDFrame
  .read(studentList)
  .explodeCollection(Student::getTag,Student::setType)
```



### explodeCollectionArray

> explodeCollectionArray(Function<T,?> getFunction,Class<E> elementClass)
>
> explodeCollectionArray(Function<T,?> getFunction,SetFunction<T,E> setFunction,Class<E> elementClass)



对`数组` 类型进行爆炸：  数组的每个元素单独作为新的一行加入到Frame中



```java
// 假设tag字段类型为Collection： String[] tag = {"二次元","舞蹈"}

//1、将tag字段炸开后放到新的c2列中
SDFrame<FI2<Student, String>> frame = SDFrame
  .read(studentList)
  .explodeCollectionArray(Student::getTag,String.class) // 需要手动指定数组的元素类型
  
// 2、将tag字段炸开后放到 type字段中
SDFrame<Student> frame = SDFrame
  .read(studentList)
  .explodeCollectionArray(Student::getTag,Student::setType,String.class)  // 需要手动指定数组的元素类型
```







# 窗口函数

可以通过SDFrame.window()方法 或者 直接使用over方法去使用窗口函数。





## Window对象使用

Window对象在`JDFrame窗口函数`中主要用于构建窗口的分区、排序、以及窗口范围等逻辑，其构建的逻辑等价于下面的SQL的窗口函数语意: 

```sql
over(partition by xx order by xxx rows between xx and xx)
```



<br>





<h4>下面从几个例子来看Window对象如何构建窗口信息</h4>



1、按照学校进行分区

- 使用group 方法去指定分区的字段

```java
// 等价于SQL窗口语意:   over(partition by school)
Window<Student> window = Window.groupBy(Student::getSchool);
```



<br>



2、按照学校进行分区， 然后每个窗口再按照年龄降序排序

- 使用sort方法去指定窗口的排序信息

```java
// 等价于SQL窗口语意:   over(partition by school order by age desc)
Window<Student> window = Window.groupBy(Student::getSchool).sortDesc(Student::getAge);
```



<br>





3、窗口按照年龄降序排序， 如果年龄相同再按照年级升序排序

- 排序链式调用构建的是多级排序的逻辑

```java
// 等价于SQL窗口语意:   over(order by age desc, level asc)
Window<Student> window = Window.sortDescBy(Student::getAge).sortAsc(Student::getLevel);
```



<br>





4、按照学校进行分区， 然后计算窗口范围内第一行到当前行

- 使用roundBetween方法去指定窗口范围的开始边界和结束边界， 然后使用`Range`对象去指定窗口的范围边界

```java
// 等价于SQL窗口语意:  over(partition by school rows between UNBOUNDED PRECEDING and CURRENT ROW)
Window<Student> window = Window.groupBy(Student::getSchool).roundBetween(Range.START_ROW,Range.CURRENT_ROW);
```



<br>





## 窗口范围

具体可指定的窗口范围边界有哪些主要通过`Range` 枚举对象去指定使用, 具体如下: 

- `START_ROW`:  表示窗口的开始边界, 等价于SQL边界 `UNBOUNDED PRECEDING`

- `BEFORE_ROW`:   表示当前行的前N行.   等价于SQL边界 ` PRECEDING`

- `CURRENT_ROW`:   表示当前行.    等价于SQL边界 `CURRENT ROW`

- `AFTER_ROW`:   表示当前行的后N行.   等价于SQL边界 ` FOLLOWING`

- `END_ROW`:  表示窗口的结束边界。等价于SQL边界 `UNBOUNDED FOLLOWING`



<br>



除了可以通过`Window`的roundBetween方法去手动指定窗口范围的开始边界和结束边界。 Window对象也内置多种常用的窗口范围API， 简化手动指定，具体如下:

- `roundAllRow() `                                   窗口全部行(默认)

- `roundBefore2CurrentRow(int n)`     前N行到当前行
- `roundCurrentRow2After(int n)   `  当前行到后N行
- `roundCurrentRow2EndRow()`      当前行到结束边界
- `roundStartRow2CurrentRow()`       开始边界到当前行
- `roundBeforeAfter(int before, int after)`      前N行到后N行



<br>



<mark>注意: </mark>实现的窗口函数语意与SQL窗口函数意一致， 唯一区别就是在不指定窗口范围时, 默认的窗口范围不一样

具体区别如下: 

-  mysql中如果使用了order默认窗口范围就是 `rows between UNBOUNDED PRECEDING and CURRENT ROW`, 如果没有使用order也没指定`rows between`, 默认窗口范围才是全部。 
- 而`JDFrame`不管你有没指定order排序， 只要你没手动设置窗口范围，`默认的窗口范围就是窗口范围内的全部行`





## 窗口计算函数

窗口计算相关函数的API都以over开头， 凡是API名字后缀不带S都表示新生成一个窗口列到FI2.  如果后缀名带S表示自定义生成到某个列



### ROW_NUMBER

> overRowNumber()					生成行号列
>
> overRowNumberS(SetFunction<T,Integer> setFunction)  生成行号到指定字段



为窗口每行生成一个连续唯一的行号，从1开始



```java
// 1、生成行号列
// 等价于SQL： select ROW_NUMBER() over()
SDFrame<FI2<Student, Integer>> frame = SDFrame.read(studentList).overRowNumber();

// 2、生成行号到id列
SDFrame<Student> frame = SDFrame.read(studentList).overRowNumberS(Student::setId);

// 3、指定窗口,生成行号列
// 等价于SQL： select ROW_NUMBER() over(partition by school)
SDFrame<FI2<Student, Integer>> frame = SDFrame
    .read(studentList)
    .window(Window.groupBy(Student::getSchool))  // 指定窗口
    .overRowNumber();
```





### RANK 

> overRank()					生成排名号列
>
> overRankS(SetFunction<T,Integer> setFunction)     生成排名号到指定字段



为窗口每行生成一个排名号，如果是相同值则排名号一样，并且排名号可能不连续 



```java
// 按照学校分区窗口， 然后窗口内按照年龄进行降序排序，然后窗口内的为每行生成非连续排名号
// 等价于SQL： select rank() over(partition by school order age desc)
SDFrame<FI2<Student, Integer>> frame = SDFrame
    .read(studentList)
    .window(Window.groupBy(Student::getSchool).sortDesc(Student::getAge) // 指定窗口
    .overRank();
```



### DENSE_RANK

> overDenseRank()
>
> overDenseRankS(SetFunction<T,Integer> setFunction)



为窗口每行生成一个排名号，如果是相同值则排名号一样，但是排名号是连续的



```java
// 按照年龄进行降序排序，为每行生成连续排名号
// 等价于SQL： select DENSE_RANK() over(order age desc)
SDFrame<FI2<Student, Integer>> frame = SDFrame
    .read(studentList)
    .window(Window.sortDescBy(Student::getAge) // 指定窗口
    .overRank();
```





### PERCENT_RANK

> overPercentRank()
>
> overPercentRankS(SetFunction<T,BigDecimal> setFunction)



为窗口每行生成一个相对排名号，相对排名号计算公式为: `(rank排名号-1) / (窗口行数-1)`



```java
// 按照年龄进行降序排序，为每行生成相对排名号
// 等价于SQL： select PERCENT_RANK() over(order age desc)
SDFrame<FI2<Student, BigDecimal>> frame = SDFrame
    .read(studentList)
    .defaultScale(6)   // 设置小数BigDecimal保留位数 , 默认四舍五入保留两位小数
    .window(Window.sortDescBy(Student::getAge) // 指定窗口
    .overPercentRank();
```





### Count

> overCount()
>
> overCountS(SetFunction<T,Integer> setFunction)



计算窗口内行数



```java
// 按照学校进行分窗， 然后计算从窗口开始边界到当前行到行数
// 等价于SQL： select count(*) over(partition by school rows between UNBOUNDED PRECEDING and CURRENT ROW)
SDFrame<FI2<Student, Integer>> frame = SDFrame
        .read(studentList)
        .window(Window.groupBy(Student::getSchool).roundStartRow2CurrentRow())
        .overCount();
```





### Sum

> overSum(Function<T,F> field)     		对窗口某字段求和
>
> overSumS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field)



计算窗口内的和



```java
// 按照学校分窗， 计算每个窗口内内年龄和
// 等价于SQL： select sum(age) over(partition by school)
SDFrame<FI2<Student,BigDecimal>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overSum(Student::getAge)

```



### Avg

> overAvg(Function<T,F> field)
>
> overAvgS(SetFunction<T,BigDecimal> setFunction, Function<T,F> field)





计算窗口内的平均值



```java
// 按照学校分窗， 计算当前行的前1行和后2行窗口范围内的年龄的平均值
// 等价于SQL： select avg(age) over(partition by school rows between 1 PRECEDING and 2 FOLLOWING)
SDFrame<FI2<Student,BigDecimal>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool).roundBetween(Range.BEFORE(1), Range.AFTER(2)))
        .overAvg(Student::getAge)

```





### Max

> overMaxValue(Function<T,F> field)
>
> overMaxValueS(SetFunction<T,F> setFunction, Function<T,F> field)



计算窗口内的最大值





```java
// 按照学校分窗， 计算每个窗口内的年龄最大值
// 等价于SQL： select max(age) over(partition by school)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overMaxValue(Student::getAge)

```



### Min

> overMinValue(Function<T,F> field)
>
> overMinValueS(SetFunction<T,F> setFunction, Function<T,F> field)



计算窗口内的最小值



```java
// 按照学校分窗， 计算每个窗口内的年龄最小值
// 等价于SQL： select min(age) over(partition by school)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overMinValue(Student::getAge)

```





### Lag

> overLag(Function<T,F> field, int n)
>
> overLagS(SetFunction<T,F> setFunction,Function<T,F> field,int n)



获取某列当前行的前N行的值





```java
// 按照学校分窗， 计算每个学生姓名的前面两位的学生姓名
// 等价于SQL： select lag(name,2) over(partition by school)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overLag(Student::getName)

```





### Lead

> overLead(Function<T,F> field, int n)
>
> overLeadS(SetFunction<T,F> setFunction,Function<T,F> field,int n)



获取某列当前行的后N行的值



```java
// 按照学校分窗， 计算每个学生姓名的后面两位的学生姓名
// 等价于SQL： select lead(name,2) over(partition by school)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overLead(Student::getName)

```





### NthValue 

> overNthValue(Function<T,F> field, int n)
>
> overNthValueS(SetFunction<T,F> setFunction,Function<T,F> field,int n)



获取窗口范围内某列的第N行的数值





```java
// 按照学校分窗， 计算窗口内第二行的姓名值
// 等价于SQL： select NTH_VALUE(name,2) over(partition by school)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overNthValue(Student::getName)

```



### FirstValue

> overFirstValue(Function<T,F> field)
>
> overFirstValueS(SetFunction<T,F> setFunction,Function<T,F> field)



获取窗口范围内某列的第1行的数值





```java
// 按照学校分窗， 将窗口按照年龄倒序排序，获取最大年龄的那个学生姓名
// 等价于SQL： select FIRST_VALUE(name) over(partition by school order by age desc)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool).sortDesc(Student::getAge))
        .overNthValue(Student::getName)

```





### LastValue

> overLastValue(Function<T,F> field)
>
> overLastValueS(SetFunction<T,F> setFunction,Function<T,F> field)



获取窗口范围内某列的最后一行的数值





```java
// 按照学校分窗， 将窗口按照年龄倒序排序，获取最小年龄的那个学生姓名
// 等价于SQL： select LAST_VALUE(name) over(partition by school order by age desc)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool).sortDesc(Student::getAge))
        .overLastValue(Student::getName)

```



### Ntile 

> overNtile(int n)
>
> overNtileS(SetFunction<T,Integer> setFunction, int n)



`分桶：` 将窗口范围内的数据尽量均匀的分成N个桶，每个桶分生成桶编号并且从1开始，如果分布不均匀，则优先分配给最小的桶，桶之间的大小差值最多不超过1





```java
// 按照学校分窗， 每个窗口分成4个桶
// 等价于SQL： select Ntile(name) over(partition by school)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.groupBy(Student::getSchool))
        .overNtile(4)

```





### CumeDist

> overCumeDist()
>
> overCumeDistS(SetFunction<T,BigDecimal> setFunction)



`累积分布值`：  用于计算一个值在一组值中的累积分布,  统计的是小于或等于当前行值的行数除以总行数, 结果的范围是 0 到 1之间

- 注意⚠️： 窗口必须指定排序，否则无法计算累积分布值



```java
// 窗口按照年龄进行倒序排序， 然后计算每行的累计分布值
// 等价于SQL： select cume_dist() over(order by age desc)
SDFrame<FI2<Student,Integer>> frame =   SDFrame
        .read(dataList)
        .window(Window.sortDescBy(Student::getAge))
        .overCumeDist()

```







