#

# forEachDo

> forEachDo(Consumer<? super T> action)



迭代遍历每个元素给到Consumer函数。  等价于List.forEach



```java
 SDFrame.read(studentList).forEachDo((student) -> {
     System.out.println("id: " + student.getId());
 });
```



# forEachParallel

> forEachParallel(Consumer<? super T> action)



并行的遍历每个元素给到Consumer函数， 注意此时遍历顺序可能与列表顺序不一致



```java
 SDFrame.read(studentList).forEachParallel((student) -> {
     System.out.println("id: " + student.getId());
 });
```







# forEachIndexDo

> forEachIndexDo(ConsumerIndex<? super T> action)



迭代遍历每个元素和当前遍历的下标索引(从0开始)给到ConsumerIndex函数



```java
SDFrame.read(studentList).forEachIndexDo((index, student) -> {
    System.out.println("name: " + student.getName() + ", index:" + index);
})
```



# forEachPreDo

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





# forEachNextDo

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


