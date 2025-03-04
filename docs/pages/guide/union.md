
# unionAll

> unionAll(Collection<T> other)



求当前集合和其他集合的并集， 但是不会去除重复



```java

// 计算studentList和otherList的并集
// 等价于SQL语意：    select * from studentList union all select * from otherList
List<Student> studentList, otherList;
List<Student> data = SDFrame.read(studentList).unionAll(otherList).toList();
```



# union

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





# retainAll

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





# retainAllOther

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



# intersection

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





# different

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



# differentOther

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



# subtract

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