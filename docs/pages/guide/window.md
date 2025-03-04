# 1、简介

JDFrame实现了编程式的窗口函数的使用，可以通过SDFrame.window()方法 或者 直接使用over方法去使用窗口函数。


# 2、Window对象

Window对象在`JDFrame窗口函数`中主要用于构建窗口的分区、排序、以及窗口范围等逻辑，其构建的逻辑等价于下面的SQL的窗口函数语意:

```sql
over(partition by xx order by xxx rows between xx and xx)
```



<br>


**下面从几个例子来看Window对象如何构建窗口信息**



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





# 3、窗口范围

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





# 4、窗口计算函数

窗口计算相关函数的API都以over开头， 凡是API名字后缀不带S都表示新生成一个窗口列到FI2.  如果后缀名带S表示自定义生成到某个列



## 4.1、ROW_NUMBER

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





## 4.2、RANK

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



## 4.3、DENSE_RANK

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





## 4.4、PERCENT_RANK

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





## 4.5、Count

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





## 4.6、Sum

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



## 4.7、Avg

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





## 4.8、Max

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



## 4.9、Min

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





## 4.10、Lag

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



## 4.11、Lead

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





## 4.12、NthValue

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



## 4.13、 FirstValue

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





## 4.14、LastValue

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



## 4.15、Ntile

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





## 4.16、CumeDist

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
