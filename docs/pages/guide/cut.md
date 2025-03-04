#

# cutFirst

> cutFirst(int n)



保留前N个元素



```java
SDFrame<Student> frame = SDFrame.read(studentList).cutFirst(10);
```



# cutLast

> cutLast(int n)



保留后N个元素



```java
SDFrame<Student> frame = SDFrame.read(studentList).cutLast(10);
```



# cut

> cut(Integer startIndex,Integer endIndex



保留索引范围[startIndex,endIndex)内的元素.



```java
SDFrame<Student> frame = SDFrame.read(studentList).cut(3,5);
```



# cutPage

> cutPage(int page,int pageSize)



保留第page页的元素



```java
// 每页大小是5， 以此保留第3页的元素
SDFrame<Student> frame = SDFrame.read(studentList).cutPage(3,5);
```


