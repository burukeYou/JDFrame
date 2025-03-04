#

# sort

> sort(Sorter<T> comparator)



根据Sorter进行排序



```java
// 将学生先按照学校名升序排序，如果顺序一样再按照年龄降序排序
// 等价于SQL语意：   select * from student order by school asc, age desc;
SDFrame
  .read(studentList)
  .sort(Sorter.sortAscBy(Student::getSchool).sortDesc(Student::getAge));
```





# sortAsc

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





#  sortDesc

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


