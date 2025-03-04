


# 1、爆炸函数

`explod爆炸函数：` 将集合、数组、结构体中的元素按照某种规则拆分成多行。



## 1.1、explodeString

> explodeString(Function<T,String> getFunction, String delimiter)   爆炸后的值放到FI2的c2列中
>
> explodeString(Function<T,String> getFunction, SetFunction<T,String> setFunction,String delimiter) 爆炸后的值放到SetFunction中



对`字符串`进行爆炸：  对某列字符串类型数值按照指定分隔符进行分隔， 将每个分隔后的值单独作为新的一行加入到Frame中.  分隔符支持正则表达式.



```java
// 将学生标签字段tag的值按照分隔符";"进行爆炸拆分成多行.
// 比如tag字段值格式： 二次元;唱歌;舞蹈
SDFrame.read(studentList).explodeString(Student::getTag,";");
```





## 1.2、explodeJsonArray

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





## 1.3、explodeCollection

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



## 1.4、explodeCollectionArray

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

# 2、条目补充函数

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




# 3、生成行号

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



# 4、生成排名

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

