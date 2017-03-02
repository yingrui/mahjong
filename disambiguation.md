## 中文歧义消解

**歧义**是在中文里面经常出现的，并且在不同的领域，对歧义的定义也有所不同。比如在医疗领域：双肺/N 是明确的人体部位，是一个词。但是在别的一些领域，可能会认为这应该分为：双/M 肺/N。

所以为了解决中文歧义的问题，Mahjong将基于词典的最大概率分词与序列标注算法结合，设计了一个可以适用到各种领域的歧义解决方案。

**Mahjong**将出现的歧义进行标注，得到一份序列标注语料。然后训练一个CRF模型来对分词歧义进行处理。

## 歧义的标注

训练一个歧义模型，首先需要已经分好词的语料。例如：
```sh
近来/D 包块/N 间断/V 出现/V
```

然后**Mahjong**将对数据进行标注，得到标注结果如下：
```sh
近来 A
包 SB
块 SE
间断 A
出现 A
```

**A**代表无歧义或未知歧义，**SB**代表一个包含歧义，这个词应该是正确词的开始，**SE**代表这个词应该是正确词的结尾。所有的标注如下：

| 标签 | 说明                            | 例子                            |
|----|-------------------------------|-------------------------------|
| A  | 无歧义或未知歧义                      | 近来/A 包/SB 块/SE 间断/A 出现/A      |
| SB | 一个词属于正确的词的开始                  | 近来/A 包/SB 块/SE 间断/A 出现/A      |
| SM | 一个词属于正确的词的中间                  | 在/A 半/SB 梦/SM 半/SM 醒/SE  之间/A |
| SE | 一个词属于正确的词的结束                  | 近来/A 包/SB 块/SE 间断/A 出现/A      |
| SH | 一个双字词，应该分为两部分，并且将两个字分别加入前后两个词 | 神经病/SB 变成/SH 为/SE             |
| FL | 一个词应该分为两部分，首字属于前词，其余单独成词      | 济南/SB 市政府/FL                  |
| LC | 一个词的最后一个字是下一个词的首字             | 精神病/LC 人/SE                   |
| U  | 当前词由两个词组成，两个词长度相等，或第一个词长度为1   | 中国/A 跨世纪/U 发展/A               |
| UT | 当前词由三个字组成                     | 离不开/UT                        |

## 测试歧义识别的效果

首先通过maven进行编译、打包。
```sh
mvn -pl lib-segment install -DskipTests
mvn -pl lib-segment-apps package -DskipTests
```

检查一下，在没有训练模型之前，对样本数据测分词准确率：
```sh
./scripts/disambiguation/test-disambiguation-model.sh --corpus-file ./scripts/disambiguation/disambiguation-demo-corpus.txt
```
结果并不理想：
```sh
Recall rate of segment is: 0.9051948051948052
Precision rate of segment is: 0.8123543123543123
F is: 0.8562653562653563
```
现在训练一下模型，第一个参数是用于训练的语料，第二个参数是生成的模型文件。
```sh
./scripts/disambiguation/demo.sh ./scripts/disambiguation/disambiguation-demo-corpus.txt disambiguation.model
```
结果准确率可以提高到**97.9%**
```sh
Recall rate of segment is: 0.9792207792207792
Precision rate of segment is: 0.9804941482444733
F is: 0.9798570500324887
```