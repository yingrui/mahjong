# Mahjong中文分词

## 简介
**麻将**一直是广受欢迎的传统娱乐活动，字牌的组合千变万化。汉字的组合也是变化多端，**Mahjong**这个项目希望能从汉字组合中发现汉语的秘密，为自然语言处理提供好的中文分词工具。

[![Build Status](https://secure.travis-ci.org/yingrui/mahjong.png?branch=master)](https://travis-ci.org/yingrui/mahjong)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.yingrui.mahjong/lib-segment/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.yingrui.mahjong/lib-segment/)

**Mahjong**是基于Scala语言实现的中文分词软件包。这个项目的核心是最大概率分词，整个项目专为搜索引擎、文本信息抽取和自然语言处理设计，参考我[关于中文分词的一些思考](./about.md)来实现。性能优异、速度快。整个分词包易于使用，测试覆盖率高。

#### 在线展示

打开链接 <http://mahjong.yingrui.me/>，您可以自己测试它的功能。 有任何问题，可以发邮件至： <yingrui.f@gmail.com>。

## 分词歧义解决方案
分词的方法有很多，现在越来越多的研究认为分词是一个的序列标注问题。但是基于词典的方法速度快，容易快速应用在领域应用中。

如何平衡基于词典的最大概率分词与基于标注的分词算法呢？**Mahjong**将这两种算法结合起来，用来解决分词歧义的问题。

[Mahjong的中文分词歧义解决方案](./disambiguation.md)

## Mahjong的使用方法

在pom.xml中加入以下依赖即可使用：
```xml
<dependency>
    <groupId>me.yingrui.mahjong</groupId>
    <artifactId>lib-segment</artifactId>
    <version>1.2</version>
</dependency>
<dependency>
    <groupId>org.scala-lang</groupId>
    <artifactId>scala-library</artifactId>
    <version>2.11.8</version>
</dependency>
<dependency>
    <groupId>org.scala-lang.modules</groupId>
    <artifactId>scala-parser-combinators_2.11</artifactId>
    <version>1.0.5</version>
</dependency>
```

#### 在Java中使用

```
Map<String, String> params = new HashMap<String, String>();
params.put("minimize.word", "true");
SegmentWorker worker = SegmentWorkerBuilder.build(params);
SegmentResult result = worker.segment(sentence);
System.out.println(result);
```

#### 在Scala中使用

```
val worker = SegmentWorker("minimize.word" -> "true")
val words = worker.segment(str)
println(words)
```

#### Mahjong的默认参数

```
# 支持查询语法，常在搜索引擎内使用，对搜索表达式中的特殊符号不分词
support.querysyntax     = false

# 加载用户词典
load.userdictionary     = true

# 加载领域词典
load.domaindictionary   = true

# 加载英语词典
load.englishdictionary  = true

# 分词结果的颗粒度尽可能小
minimize.word           = false

# 识别姓名
recognize.chinesename   = true
# 将姓名的姓和名分开
separate.xingming       = false

# 识别词性
recognize.partOfSpeech  = true

# 全角半角转换
convert.tohalfshape     = false
# 字母的大小写转换
convert.touppercase     = false

# 汉字转拼音，基于隐马尔可夫模型实现
recognize.pinyin        = false

# 基于文章的上下文分词
segment.context         = false
```

####



## 实验性功能
**CRF** 目前被广泛应用来解决序列标注问题，Mahjong实现了CRF算法，并基于此构建了自己的分词器。并结合基于词典的分词，形成了独特的歧义解决方案。

**深度学习** 随着深度学习越来越火， Mahjong实现了**Word2Vec**算法，并基于字向量实现了基于神经网络的分词器。

实现结果显示，基于字向量的分词并不理想，必须按照歧义解决方案类似，使用基于词向量来实现分词器。

[更多阅读](./experiments.md)

## 2.0计划新增功能

[项目计划](https://github.com/yingrui/mahjong/projects/1)

#### 新功能
1. 支持微博的用户ID和主题的语法
2. 支持IP地址、邮件、URL、GUID、英语数字、中英文混合词
3. 支持日本姓名的识别
4. 支持中文日期、英文日期转ISO日期格式
5. 支持Barcode的解析

#### 增强
1. 在核心词典中增加著名地区和景点
2. 更新世界名人、商标、商品、公司等
3. 优化核心词典，加快分词速度
4. 增强地名识别

#### 试验功能
1. RNN分词
2. 支持中文繁体字分词