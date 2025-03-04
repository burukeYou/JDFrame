#


# toLists

> toLists()



将Frame模型转换成列表



```jade
SDFrame<Student> frame = SDFrame.read(studentList)
List<Student> data = frame.toLists();
```





# toArray

> toArray()



将Frame模型转换成数组





```jade
SDFrame<Student> frame = SDFrame.read(studentList)
Student[] arr = frame.toArray();
```





# toMap

> toMap(Function<T, K> keyMapper, Function<T,V> valueMapper)



将Frame模型转换成Map.  key就是keyMapper， value就是valueMapper



```java
Map<Integer,String> map = SDFrame.read(studentList).toMap(Student::getId, Student::getName);
```





# toMulti2Map

> toMulti2Map(Function<T, K> keyMapper, Function<T, K2> key2Mapper, Function<T, V> valueMapper)



将Frame模型转换成二层级Map.

- 第一层Map的key就是keyMapper，第二层的Map的key就是key2Mapper，  value就是valueMapper



```java
Map<String,Map<Integer,Long>> map = SDFrame
    .read(studentList)
    .toMulti2Map(Student::getSchool, Student::getLevel,Student::getId);
```



# toMulti3Map

> toMulti3Map(Function<T, K> keyMapper, Function<T, K2> key2Mapper, Function<T, K3> key3Mapper,Function<T, V> valueMapper)



将Frame模型转换成三层级Map.

- 第一层Map的key就是keyMapper，第二层的Map的key就是key2Mapper，第三层的Map的key就是key3Mapper， value就是valueMapper



```java
Map<String,Map<Integer,Map<String,Long>>> map = SDFrame
    .read(studentList)
    .toMulti3Map(Student::getSchool, Student::getLevel,Student::getName,Student::getId);
```


# map

> map(Function<T,R> map)



映射准换成其他数据类型,   与stream().map 语意一致



```java
// SDFrame<Student>  转换成   SDFrame<UserInfo>
SDFrame<UserInfo> frame = SDFrame.read(studentList).map(e -> new UserInfo(e.getId));
```





# mapParallel

> mapParallel(Function<T,R> map)



并行映射准换成其他数据类型， 注意映射顺序可能与列表顺序不一致





```java
// SDFrame<Student>  转换成   SDFrame<UserInfo>
SDFrame<UserInfo> frame = SDFrame.read(studentList).mapParallel(e -> new UserInfo(e.getId));
```


# partition

> partition(int n)



分区： 将表格切割成多个小表格，每个表格行数为n（最后一个表格行数可能小于n），用于将大任务拆成小任务



```java
// 将列表分区，每个区大小为5个
List<List<Student>> taskList = SDFrame.read(studentList).partition(5).toLists();
```

