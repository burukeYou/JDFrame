# JDFrame
-------
![travis](https://travis-ci.org/nRo/DataFrame.svg?branch=master)
[![License](http://img.shields.io/badge/license-apache%202-brightgreen.svg)](https://github.com/burukeYou/fast-retry/blob/main/LICENSE)


`JDFrame`是一个仿SQL数据处理工具， 一个流式处理工具， 一个JVM层级的仿DataFrame模型工具，提供了DataFrame模型的若干基本功能比如复杂数据筛选、分组聚合、窗口函数、连接矩阵。除此之外，还会语意化和简化一些Java8的stream流式处理功能，提供更加强大的流式处理能力。

# 特征
- **无侵入**：  非常轻量级无需任何依赖和配置环境， 使用它就像使用一个Util那么简单
- **DataFrame模型**:    JVM层级的仿DataFrame模型实现，涵盖了模型的大部分常用功能。同时，支持强类型，既不会导致数据类型的丢失，也无需进行硬编码操作。
- **类SQL的语义**： 如果您会 SQL 语言，基本上不需要花费什么学习精力。多数 API 基本都是依据 SQL 的语义来进行 Java 的实现，具有极高的可读性
- **强大的流式处理能力:**     在原有 Java8 的 stream 流的基础之上，提供了更为强大且更为简洁的流式处理能力。就理念而言，它并非是对 stream 的替换，而是对 stream 的继承
- **极简式API:**     把业务功能或者冗长繁杂的数据处理功能凝聚成一个极简且语意明晰的 API，以供开发者使用



# 使用之前
假设有需求，统计每个学校的里学生年龄不为空并且年龄在9到16岁间的合计分数，并且获取合计分前2名的学校

如果没有使用`JDFrame`， 你的代码`可能`会是这样的

```java

    public void main(){
        List<Student> students = new ArrayList();
        LinkedHashMap<String, BigDecimal> map = students.stream()
                .filter(s -> s.getAge() != null && s.getAge() >= 9 && s.getAge() <= 16)
                .collect(Collectors.groupingBy(Student::getSchool,Collectors.reducing(BigDecimal.ZERO, Student::getScore, BigDecimal::add)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(2)
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(oldVal, newVal) -> oldVal,LinkedHashMap::new));
    }

```

# 使用之后

但如果使用 `JDFrame`后 你的代码会是这样的，  无论是可读性、可手写性、扩展性都极大提升。 更多高级的功能见[使用文档](pages/guide/quick-start.md)

```java
    public void main(){
            // 等价于SQL:
            //       select school,sum(score)  
            //       from students
            //       where age is not null and age >=9 and age <= 16
            //       group by school
            //       order by sum(score) desc
            //       limit 2
            List<Student> studentList = new ArrayList();
            SDFrame<FI2<String, BigDecimal>> sdf2 = SDFrame.read(studentList)
                   .whereNotNull(Student::getAge)
                   .whereBetween(Student::getAge,9,16)
                   .groupBySum(Student::getSchool, Student::getScore)
                   .sortDesc(FI2::getC2)
                   .cutFirst(2);
            
    }

```

