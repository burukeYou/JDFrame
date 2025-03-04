

# joining

> joining(Function<T,U> joinField,CharSequence delimiter)

将数值按照指定分隔符拼接起来返回


```java
// 将学生的id按照分隔符“，”拼接起来返回
String idStr = SDFrame.read(studentList).joining(Student::getId,",");
```


# sum

> sum(Function<T, R> function)



计算数据值的和

- 会自动过滤掉null值的计算， 防止空指针异常



```java
// 计算学生的年龄和
BigDecimal sum = SDFrame.read(studentList).sum(Student::getAge);
```





# Avg

> avg(Function<T, R> function)



计算数据值的平均值

- 会自动过滤掉null值， null值不会参与平均值的计算



```java
// 计算学生的年龄的平均值
BigDecimal sum = SDFrame.read(studentList).avg(Student::getAge);
```





# max

>  max(Function<T, R> function)



计算数值最大的对象



```java
// 获取年龄最大的学生
Student maxStudent = SDFrame.read(studentList).max(Student::getAge);
```





# maxValue

> maxValue(Function<T, R> function)



计算最大的数值



```java
// 获取最大的年龄
Integer maxAge = SDFrame.read(studentList).maxValue(Student::getAge);
```



# min

> min(Function<T, R> function)



计算数值最小的对象



```java
// 获取年龄最小的学生
Student maxStudent = SDFrame.read(studentList).min(Student::getAge);
```





# minValue

> minValue(Function<T, R> function)



计算最小的数值



```java
// 获取最小的年龄
Integer minAge = SDFrame.read(studentList).minValue(Student::getAge);
```







# maxMin

> maxMin(Function<T, R> function)



计算数值最大的对象和数值最小的对象



```java
// 获取年龄最大和年龄最小的学生
MaxMin<Student> mm = SDFrame.read(studentList).maxMin(Student::getAge);
```



# maxMinValue

> maxMinValue(Function<T, R> function)



计算最大和最小的数值



```java
// 获取最大的年龄和最小的年龄
MaxMin<Integer> mm = SDFrame.read(studentList).maxMinValue(Student::getAge);
```



# count

> count()



获取行数



```java
// 获取数量
long cunt = SDFrame.read(studentList).count();
```





# countDistinct

> countDistinct(Comparator<T> comparator)   -根据Comparator去重后获取行数
>
> countDistinct(Function<T, R> function)     -根据字段数值去重后获取行数





```java
// 获取不重复的学生名数量
long count = SDFrame.read(studentList).countDistinct(Student::Name);
```

