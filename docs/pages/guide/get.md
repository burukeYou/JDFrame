

#

# head

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



# tail

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



# page

> page(int page,int pageSize)



获取第page页的元素返回



```java
// 每页大小是5， 以此获取第3页的数据返回
List<Student> pageList = SDFrame.read(studentList).page(3,5);
```









# col

> col(Function<T, R> function)



获取Frame表格的某一列数据



```java
// 获取所有学生姓名
List<String> nameList =  SDFrame.read(studentList).col(Student::getName);
```
