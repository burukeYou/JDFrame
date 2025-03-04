
#


# read

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



# show

> show()
>
> show(int n)



打印前N行数据成表格到控制台



```java
 SDFrame.read(studentList).show(30);
```


# stream

> stream()



获取Frame模型的stream流



```jade
SDFrame<Student> frame = SDFrame.read(studentList)
Stream<Student> = frame.stream();
```



# isEmpty

> isEmpty()



判断模型列表数据是否为空



```java
boolean flag = SDFrame.read(studentList).isEmpty();
```





# isNotEmpty

> isNotEmpty()



判断模型列表数据是否不为空



```java
boolean flag = SDFrame.read(studentList).isNotEmpty();
```



# contains

> contains(T other)



是否存在指定元素



```java
boolean flag = SDFrame.read(studentList).contains(new Student("a","b"));
```



# containsValue

>  containsValue(Function<T,U> valueFunction, U value)



某列是否存在指定值



```java
// 判断学生里是否存在名字叫张三的同学
boolean flag = SDFrame.read(studentList).containsValue(Student::getName,"张三");
```

# distinct

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


