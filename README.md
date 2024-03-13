

# 背景
一个jvm层级的仿DataFrame工具，语意化和简化java8的stream流式处理

目前市面上其实也存在一些java的DataFrame框架比如 tablesaw，joinery
但是他们得硬编码去指定字段名，这对于有代码洁癖的实在难以忍受
在一些场景下能不能使用匿名函数去指定的字段处理去处理，于是便有了这个

# 快速开始

```java
        // 获取学生年龄在9到16岁的学学校合计分数最高的前10名的学校
        SDFrame<FI2<String, BigDecimal>> sdf2 = SDFrame.read(studentList)
                .whereNotNull(Student::getAge)
                .whereBetween(Student::getAge,9,16)
                .groupBySum(Student::getSchool, Student::getScore)
                .sortDesc(FI2::getC2)
                .subFirst(10);
```

其他具体API见 IFrame接口
JDFrame 与 SDFrame区别 ，JDFrame的所有操作都是实时生效的 

# 其他
感觉还有很多api可以扩展，欢迎你的建议，或者一起扩展