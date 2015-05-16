# Max Probability Segment

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