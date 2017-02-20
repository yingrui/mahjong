# Max Probability Segment

## 简介
**麻将**一直是广受欢迎的传统娱乐活动，字牌的组合千变万化。汉字的组合也是变化多端，**Mahjong**这个项目希望能从汉字组合中发现汉语的秘密，为自然语言处理提供好的中文分词工具。

[![Build Status](https://secure.travis-ci.org/yingrui/mahjong.png?branch=master)](https://travis-ci.org/yingrui/mahjong)

**Mahjong**是基于Scala语言实现的中文分词软件包。这个项目的核心是最大概率分词，整个项目专为搜索引擎、文本信息抽取和自然语言处理设计，参考我[关于中文分词的一些思考](./about.md)来实现。性能优异、速度快。整个分词包易于使用，测试覆盖率高。

## Overview
**Mahjong** is a java library for Chinese Breaking and written in Scala.

[![Build Status](https://secure.travis-ci.org/yingrui/mahjong.png?branch=master)](https://travis-ci.org/yingrui/mahjong)

This project implements max probability segment algorithm. The project has been well designed for Search Engine, Web Information Extraction and Nature Language Processing.

We built an independent jar file, it is easy to use, and all codes are fully tested.

It support user dictionary, synonyms, pinyin recognition etc.

## Online Demonstrate

Open link <http://mahjong.yingrui.me/>, and you could try it. 

Any question, please contact me <yingrui.f@gmail.com>.

## Experimental Development
**CRF** is the state-of-the-art algorithm for serial label classify problem, it has been widely adopted to apply to NLP tasks.

**Mahjong** also implemented CRF algorithm and trying to balance the between different algorithms.

**Deep Learning** is also become the hottest topic in machine learning. Our project also implements **Word2Vec**, it is going to integrate with max probability algorithm as well.

[Experiments](./experiments.md)