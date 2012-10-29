/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.performance;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;

/**
 * @author ray
 */
public class DisambiguationSegmentTest {

    @Test
    public void should_scan_context_freq_for_Computer_and_Chance() {
        String str = "计算机会成本将会大大增加成功的机会。（Context: 欢呼我们的机会！机会！机会！）";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        SegmentResult words = worker.segment(str);
        System.out.println(words);

        boolean old = worker.isUseContextFreqSegment();
        worker.setUseContextFreqSegment(true);
        words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(1), "机会");
        worker.setUseContextFreqSegment(old);
    }

    @Test
    public void should_scan_context_freq_for_Student_and_Biolog() {
//        String str = "学生物的这些曾经最优秀的学生，如果80%以上的人奋斗了一辈子，都过不上decent的生活，这个行业还能让人看到未来吗？？  21世纪将是生命科学的世纪，很可能是一个大笑话。  难道学生物的都将是“被跨掉”的一代？？  相信很大一部分正学生物，做生物的同行们都有同感。   转载一些写实的报道，大家看看是不是这么回事。  从哈佛退学(生物) 发信人: sorcin (退一步海阔天空), 信区: Biology 标  题: Re: Re: 决心 退了，大家别留我吧！！ 发信站: BBS 未名空间站 (Sat Sep  8 23:18:21 2007)  我大二就开始在实验室工作了.  暑假三个月也不回家, 就留在实验室做实验. 另外加 上三年硕士, 拿到硕士学位又在学校呆了一年作实验. 所以我出国之前就发过5篇SCI的 文章.   算来到我退的时候已经做了16年的生物研究了. 在我拿到PHD之前包括在国内读硕士的 时候,我都保持早上9点到实验室,晚上11-12点离开实验室(有课就去上课). 一周六天如 此, 周日如果没其他事一般也是呆在实验室.   另外出文章的速度不是线性的,是有加速度的. 你在一个实验室一般前两年出不了什么 东西.两年之后实验室的技术和系统都熟悉了,做实验出结果的速度就会大大加快.只要 你做实验的时候足够细心保证不出任何低级错误, 再加上实验设计充分,一般实验的成 功率很大.    唉,一说到实验我还是痴心不改. 其实都俱往已.  说实话做生物这么多年让我最寒心的 是在我付出了将近20年的超乎寻常的努力之后,仍然是两眼一摸黑, 不知道前途在何方. 所以在当我老板在哈佛做了4年AP因没有拿到funding而被一脚踢出门, 实验室关门之 际, 我对生物彻底失望了: 且不说我找faculty职位一直未果,就算是找到了还有这么多 人照样是被扫地出门. 与其说再找一个实验室再干4-5年 (谁又能保证这4-5年内就一定 能实验顺利,发好文章,然后5年后找到一个职位, 找到职位后还能继续顺利, 发好文章, 拿到funding, 拿到tenure, 这每一步都很可能出问题的, 一旦出了问题又何去何从?) , 还不如咬咬牙,退了.    借Oncogene善意提示结束本帖：                                     1. 无论POSTDOC还是学生, 选择老板时, 其人品, FUNDING, 科研水平要综合考虑. 如  果其中任何一个因素有严重缺陷, 请做好炼狱的心理准备.    2. 基础生物学研究学术道路基本时间表：PHD STUDY 5年 ＋ POSTDOC 5年 ＋   Assistant Professor 5年 = Tenured Faculty.（特别声明：一般观点认为，只有10％  甚至更少的生物学PHD学位持有者最终能够成为AP，因此，选择生物学作为你的终身职  业前请务必三思）  退吧,早退早超生.   为了让你感觉好一点, 告诉你我的情况吧, 我是国内P大毕业, 美国TOP10的学校PHD毕 业, 然后在Boston H大做了3年Postdoc, 2年Instructor, 发了21篇5~6分的文章(一作16篇), 得过 两个小GRANTS, 若干学会的New Investigator awards.  最后还是找不到Faculty位置, 咬咬牙也就退了. 现在找Faculty位置, 得满足以下要求中的至少两个,";
        String str = "学生物的这些曾经最优秀的学生，如果80%以上的人奋斗了一辈子，都过不上decent的生活，这个行业还能让人看到未来吗？？  21世纪将是生命科学的世纪，很可能是一个大笑话。  难道学生物的都将是“被跨掉”的一代？？  相信很大一部分正学生物，做生物的同行们都有同感。   转载一些写实的报道，大家看看是不是这么回事。  从哈佛退学(生物) 发信人: sorcin (退一步海阔天空), 信区: Biology 标  题: Re: Re: 决心 退了，大家别留我吧！！ 发信站: BBS 未名空间站 (Sat Sep  8 23:18:21 2007)  我大二就开始在实验室工作了.  暑假三个月也不回家, 就留在实验室做实验. 另外加 上三年硕士, 拿到硕士学位又在学校呆了一年作实验. 所以我出国之前就发过5篇SCI的 文章.   算来到我退的时候已经做了16年的生物研究了. 在我拿到PHD之前包括在国内读硕士的 时候,我都保持早上9点到实验室,晚上11-12点离开实验室(有课就去上课). 一周六天如 此, 周日如果没其他事一般也是呆在实验室.   另外出文章的速度不是线性的,是有加速度的. 你在一个实验室一般前两年出不了什么 东西.两年之后实验室的技术和系统都熟悉了,做实验出结果的速度就会大大加快.只要 你做实验的时候足够细心保证不出任何低级错误, 再加上实验设计充分,一般实验的成 功率很大.    唉,一说到实验我还是痴心不改. 其实都俱往已.  说实话做生物这么多年让我最寒心的 是在我付出了将近20年的超乎寻常的努力之后,仍然是两眼一摸黑, 不知道前途在何方. 所以在当我老板在哈佛做了4年AP因没有拿到funding而被一脚踢出门, 实验室关门之 际, 我对生物彻底失望了: 且不说我找faculty职位一直未果,就算是找到了还有这么多 人照样是被扫地出门. 与其说再找一个实验室再干4-5年 (谁又能保证这4-5年内就一定 能实验顺利,发好文章,然后5年后找到一个职位, 找到职位后还能继续顺利, 发好文章, 拿到funding, 拿到tenure, 这每一步都很可能出问题的, 一旦出了问题又何去何从?) , 还不如咬咬牙,退了.    借Oncogene善意提示结束本帖：                                     1. 无论POSTDOC还是学生, 选择老板时, 其人品, FUNDING, 科研水平要综合考虑. 如  果其中任何一个因素有严重缺陷, 请做好炼狱的心理准备.    2. 基础生物学研究学术道路基本时间表：PHD STUDY 5年 ＋ POSTDOC 5年 ＋   Assistant Professor 5年 = Tenured Faculty.（特别声明：一般观点认为，只有10％  甚至更少的生物学PHD学位持有者最终能够成为AP，因此，选择生物学作为你的终身职  业前请务必三思）  退吧,早退早超生.   为了让你感觉好一点, 告诉你我的情况吧, 我是国内P大毕业, 美国TOP10的学校PHD毕 业, 然后在Boston H大做了3年Postdoc, 2年Instructor, 发了21篇5~6分的文章(一作16篇), 得过 两个小GRANTS, 若干学会的New Investigator awards.  最后还是找不到Faculty位置, 咬咬牙也就退了. 现在找Faculty位置, 得满足以下要求中的至少两个,";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "学生");
        Assert.assertEquals(words.getWord(1), "物");

        boolean old = worker.isUseContextFreqSegment();
        worker.setUseContextFreqSegment(true);
        words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "学");
        Assert.assertEquals(words.getWord(1), "生物");

        worker.setUseContextFreqSegment(old);
    }

    @Test
    public void should_scan_context_freq_for_Pingpang_and_Auction() {
        String str = "今天的拍卖非常成功，王励勤的乒乓球拍卖完了";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        boolean old = worker.isUseContextFreqSegment();
        worker.setUseContextFreqSegment(true);
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(8), "乒乓球");
        Assert.assertEquals(words.getWord(9), "拍卖");

        worker.setUseContextFreqSegment(old);
    }

    @Test
    public void should_scan_context_freq_for_PingpangRackets_and_Sale() {
        String str = "乒乓球拍卖完了。这里所有的球拍都物美价廉，这最后几个球拍便宜一点，你就都拿走吧？要不再送你几个其它球拍。（Context: 球拍球拍球拍球拍球拍球拍球拍。乒乓乒乓乒乓乒乓乒乓乒乓）";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        boolean old = worker.isUseContextFreqSegment();
        worker.setUseContextFreqSegment(true);
        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(0), "乒乓");
        Assert.assertEquals(words.getWord(1), "球拍");
        Assert.assertEquals(words.getWord(2), "卖");

        worker.setUseContextFreqSegment(old);
    }

    @Test
    public void should_scan_context_freq_for_Learning_and_Student() {
        String str = "该学生物理成绩不甚理想，学生物比较好！生物相比物理简单";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(8), "学生");

        boolean old = worker.isUseContextFreqSegment();
        worker.setUseContextFreqSegment(true);
        words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(8), "学");

        worker.setUseContextFreqSegment(old);
    }

    @Test
    public void should_know_And_and_Kimono() {
        String str = "化妆和服装";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        SegmentResult words = worker.segment(str);
        System.out.println(words);

        Assert.assertEquals(words.getWord(1), "和");
    }

    @Test
    public void should_know_Handle_and_TakeAway() {
        String str = "这个把手坏了。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        SegmentResult words = worker.segment(str);
        System.out.println(words);
        Assert.assertEquals(words.getWord(1), "把手");

        str = "请把 手拿开";
        words = worker.segment(str);
        // TODO: Should break 把手 to 把 and 手.
        Assert.assertEquals(words.getWord(1), "把");
        System.out.println(words);
    }

    @Test
    public void should_know_General_and_Would() {
        String str = "产量三年中 将增长两倍。";
        SegmentEngine engine = SegmentEngine.getInstance();
        SegmentWorker worker = engine.getSegmentWorker();

        SegmentResult words = worker.segment(str);
        System.out.println(words);
        // TODO: Should break 中将 to 中 and 将.
        Assert.assertEquals(words.getWord(3), "中");

        str = "将军任命了一名中将";
        words = worker.segment(str);
        Assert.assertEquals(words.getWord(5), "中将");
        System.out.println(words);
    }
}
