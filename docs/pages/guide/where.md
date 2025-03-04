#

# whereNull

>  whereNull(Function<T, R> function)



过滤数据值为null的数据，兼容的字符串类型的判断，如果字符串为空也认为是null。 `等价于SQL语义:  where name is null or name = ''`



```java
// 过滤学校名字为null的数据。  等价于SQL语义:  where name is null or name = ''
SDFrame
  .read(studentList)
  .whereNull(Student::getName)
  .show()
```





# whereNotNull

>  whereNotNull(Function<T, R> function)



过滤数据值不为null的数据，兼容的字符串类型的判断，字符串不为null也不为空才算非null. `等价于SQL语义:  where name is not null and name != ''`



```java
// 过滤学校名字不为null的数据               
SDFrame
  .read(studentList)
  .whereNotNull(Student::getName)
  .show()
```





# whereBetween

>  whereBetween()      -过滤范围在[start,end]之间数据，区间是前闭后闭
>
> whereBetweenN()     -过滤范围在(start,end)之间数据，区间是前开后开
>
> whereBetweenR()     -过滤范围在(start,end]之间数据，区间是前开后闭
>
> whereBetweenL()   -过滤范围在[start,end)之间数据，区间是前闭后开
>
> whereNotBetween()  -过滤范围在[start,end]之外的数据， 包含边界值
>
> whereNotBetweenN()    -过滤范围在(start,end)之外的数据， 不包含边界值





```java
SDFrame
  .read(studentList)
  .whereBetween(Student::getAge,3,6)  // 过滤年龄在[3，6]岁间的数据。 包含3岁、6岁
  .whereBetweenN(Student::getAge,3,6)  // 过滤年龄在(3，6)岁间的数据。 不包含3岁、6岁
  .whereBetweenR(Student::getAge,3,6)  // 过滤年龄在(3，6]岁间的数据。 包含6岁,不包含3岁
  .whereBetweenL(Student::getAge,3,6)  // 过滤年龄在[3，6)岁间的数据。 包含3岁,不包含6岁
  .whereNotBetween(Student::getAge,3,6)  // 过滤年龄在[3，6]之外的数据。 包含3岁、6岁
  .whereNotBetweenN(Student::getAge,3,6)  // 过滤年龄在[3，6]之外的数据。 不包含3岁、6岁
  .show()
```



等价于SQL语意:

```sql
where age between 3 and 6
```





# whereIn

> whereIn(Function<T, R> function, List<R> list)



筛选数据值在指定列表内的数据. 等价于SQL语意 `where age in (3,7,8) `



```java
// 过滤年龄为3岁 或者7岁 或者 8岁的数据
SDFrame
  .read(studentList)
  .whereIn(Student::getAge,  Arrays.asList(3,7,8)) 
  .show()
```





# whereNotIn

> whereNotIn(Function<T, R> function, List<R> list)



筛选数据值不在指定列表内的数据. 等价于SQL语意 `where age not in (3,7,8) `



```java
// 过滤年龄不为3岁,7岁,8岁的数据
SDFrame
  .read(studentList)
  .whereNotIn(Student::getAge,  Arrays.asList(3,7,8)) 
  .show()
```







# whereTrue

>  whereTrue(Predicate<T> predicate).



过滤断言条件为true的数据



```java
// 过滤学生年龄大于10岁或者年级等于3的数据
SDFrame
  .read(studentList)
  .whereTrue(student -> student.getAge() > 10 or student.getLevel() = 3)
  .show()
```





使用建议： 有复杂过滤条件或者or关系时使用



# whereNotTrue

>  whereNotTrue(Predicate<T> predicate).



过滤断言条件不为true的数据， 相当于whereTrue取反



```java
// 过滤学生年龄不等于3的数据
SDFrame
  .read(studentList)
  .whereNotTrue(student -> student.getLevel() = 3)
  .show()
```





# whereEq

> whereEq(Function<T, R> function, R value)



过滤数据值为指定值的数据。  等价于SQL语意:  `where age = 3`



```java
// 过滤学生年龄等于3岁的数据
SDFrame
  .read(studentList)
  .whereEq(Student::getAge,3)
  .show()
```





# whereNotEq

> whereNotEq(Function<T, R> function, R value)



过滤数据值不为指定值的数据。  等价于SQL语意:  `where age != 3`



```java
// 过滤学生年龄不等于3岁的数据
SDFrame
  .read(studentList)
  .whereNotEq(Student::getAge,3)
  .show()
```





# whereGt

> whereGt(Function<T, R> function, R value)



过滤数据值大于指定值的数据。  等价于SQL语意:  `where age > 3`



```java
// 过滤学生年龄大于3岁的数据
SDFrame
  .read(studentList)
  .whereGt(Student::getAge,3)
  .show()
```



# whereGe

> whereGe(Function<T, R> function, R value)



过滤数据值大于等于指定值的数据。  等价于SQL语意:  `where age >= 3`



```java
// 过滤学生年龄大于等于3岁的数据
SDFrame
  .read(studentList)
  .whereGe(Student::getAge,3)
  .show()
```



# whereLt

> whereLt(Function<T, R> function, R value)



过滤数据值小于指定值的数据。  等价于SQL语意:  `where age < 3`



```java
// 过滤学生年龄小于于3岁的数据
SDFrame
  .read(studentList)
  .wherelt(Student::getAge,3)
  .show()
```





# whereLe

> whereLe(Function<T, R> function, R value)



过滤数据值小于等于指定值的数据。  等价于SQL语意:  `where age <= 3`



```java
// 过滤学生年龄小于等于3岁的数据
SDFrame
  .read(studentList)
  .whereLe(Student::getAge,3)
  .show()
```





# whereLike

> whereLike(Function<T, R> function, R value)



模糊查询数据值包含指定值的数据。  等价于SQL语意:  `where name like "%jay%"`



```java
// 模糊查询学生姓名包含jay的数据
SDFrame
  .read(studentList)
  .whereLike(Student::getName,"jay")
  .show()
```







# whereNotLike

> whereNotLike(Function<T, R> function, R value)



模糊查询数据值不包含指定值的数据。  等价于SQL语意:  `where name not like "%jay%"`



```java
// 模糊查询学生姓名不包含jay的数据
SDFrame
  .read(studentList)
  .whereNotLike(Student::getName,"jay")
  .show()
```



# whereLikeLeft

>  whereLikeLeft(Function<T, R> function, R value)
>
>

模糊查询数据值包含指定前缀值的数据。  等价于SQL语意:  `where name  like "jay%"`





```java
// 模糊查询学生姓名前缀为jay的数据
SDFrame
  .read(studentList)
  .whereLikeLeft(Student::getName,"jay")
  .show()
```



# whereLikeRight

>  whereLikeRight(Function<T, R> function, R value)
>
>

模糊查询数据值包含指定后缀值的数据。  等价于SQL语意:  `where name  like "%jay"`



```java
// 模糊查询学生姓名后缀为jay的数据
SDFrame
  .read(studentList)
  .whereLikeLeft(Student::getName,"jay")
  .show()
```
