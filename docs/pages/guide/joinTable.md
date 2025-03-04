# join

> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> joinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> joinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);
>
> joinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join)



<br>



`内连接： ` 根据关联条件`JoinOn`关联其他表， 如果关联成功，将关联的两个对象回调给`Join`函数或者`VoidJoin`函数

- `join`:     内连接其他数据.  (与SQL `inner join`语义一致)
- `joinOnce`：    与join不同， 只会内连接一条数据
- `joinVoid`:     执行内连接操作， 与join区别是不会改变Frame的数据内容和行数，只做关联操作。 并把关联的两个对象回调给VoidJoin对象函数
- `joinOnceVoid`:   与joinVoid不同， 只会执行一次内连接操作



<br>



示例代码:

```java
// 学生表
SDFrame<Student> stuFrame = SDFrame.read(studentList);
// 用户表
SDFrame<Student> userFrame = SDFrame.read(userInfoList);

/* 等价于SQL语意:
		selet s.name,u.id,u.user_name
		from student s inner join user u  on  s.name = u.name
*/
SDFrame<StuDTO> data =  stuFrame.join(userFrame,
                             // 关联条件                 
                             JoinOn.on(Student::getName, UserInfo::getName),
                             // 关联操作                     
                             (stu, user) -> {
                                    StuDTO dto = new StuDTO();
                                    dto.setName(stu.getName());
                                    dto.setUserId(user.getId());
                                    dto.setUserName(user.getUserName());
                                    return dto;
                               });
```







# leftJoin

> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> leftJoinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> leftJoinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);
>
> leftJoinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join)



<br>



`左连接:`  以当前表为主表，关联其他表， 如果根据关联条件`JoinOn`关联成功，将关联的两个对象回调给`Join`函数或者`VoidJoin`函数， 如果关联失败，只会将主表对象回调给`Join`函数， 而关联对象会回调为null

- `leftJoin`:     左连接其他数据。 (与SQL `left join`语义一致)
- `leftJoinOnce`：    与leftJoin不同， 只会左连接一条数据
- `leftJoinVoid`:     执行左连接操作， 与leftJoin区别是不会改变Frame的数据内容和行数，只做关联操作。 并把关联的两个对象回调给VoidJoin对象函数
- `leftJoinOnceVoid`:   与leftJoinVoid不同， 只会执行一次左连接操作



<br>



示例代码:

```java
// 学生表
SDFrame<Student> stuFrame = SDFrame.read(studentList);
// 用户表
SDFrame<Student> userFrame = SDFrame.read(userInfoList);

/* 等价于SQL语意:
		selet s.name,u.id,u.user_name
		from student s left join user u  on  s.name = u.name
*/
SDFrame<StuDTO> data =  stuFrame.leftJoin(userFrame,
                         // 关联条件                 
                         JoinOn.on(Student::getName, UserInfo::getName),
                         // 关联操作                     
                         (stu, user) -> {
                                if(user = null){
                                  // 如果左连接失败，user会回调为null,需手动判断
                                  user = new User();
                                }
                           
                                StuDTO dto = new StuDTO();
                                dto.setName(stu.getName());
                                dto.setUserId(user.getId());
                                dto.setUserName(user.getUserName());
                                return dto;
                           });
```









# rightJoin

> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> rightJoinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join)
>
> rightJoinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);
>
> rightJoinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join)



<br>



`右连接:`  以其他表为主表，关联当前表， 如果根据关联条件`JoinOn`关联成功，将关联的两个对象回调给`Join`函数或者`VoidJoin`函数， 如果关联失败，只会将主表对象回调给`Join`函数， 而关联对象会回调为null

- `rightJoin`:     左连接其他数据。 (与SQL `right join`语义一致)
- `rightJoinOnce`：    与rightJoin不同， 只会右边连接一条数据
- `rightJoinVoid`:     执行右连接操作， 与rightJoin区别是不会改变Frame的数据内容和行数，只做关联操作。 并把关联的两个对象回调给VoidJoin对象函数
- `rightJoinOnceVoid`:   与rightJoinVoid不同， 只会执行一次左连接操作



<br>



示例代码:

```java
    // 学生表
    SDFrame<Student> stuFrame = SDFrame.read(studentList);
    // 用户表
    SDFrame<Student> userFrame = SDFrame.read(userInfoList);

    /* 等价于SQL语意:
            selet s.name,u.id,u.user_name
            from student s right join user u  on  s.name = u.name
    */
    SDFrame<StuDTO> data =  stuFrame.rightJoin(userFrame,
                                 // 关联条件                 
                                 JoinOn.on(Student::getName, UserInfo::getName),
                                 // 关联操作                     
                                 (stu, user) -> {
                                        if(stu = null){
                                          // 如果右连接失败，stu会回调为null,需手动判断
                                          stu = new Student();
                                        }
                                   
                                        StuDTO dto = new StuDTO();
                                        dto.setName(stu.getName());
                                        dto.setUserId(user.getId());
                                        dto.setUserName(user.getUserName());
                                        return dto;
                                   });
```







