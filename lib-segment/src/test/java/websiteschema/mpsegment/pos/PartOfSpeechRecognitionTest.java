/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.mpsegment.pos;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import websiteschema.mpsegment.core.POSRecognizer;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.util.SerializeHandler;

/**
 * @author ray
 */
public class PartOfSpeechRecognitionTest {

    @Test
    public void should_recognize_POS_of_words() {
        String testInput = "世界之大，无奇不有。";
        SegmentResult result = SegmentEngine.getInstance().getSegmentWorker().segment(testInput);
        String expected = "世界/N 之/U 大/A ，/W 无奇不有/I 。/W";
        Assert.assertEquals(expected, result.toString().trim());

        testInput = "用基于统计的最大概率分词算法对自然语言文本进行分词处理。";
        result = SegmentEngine.getInstance().getSegmentWorker().segment(testInput);
        System.out.println(result.toString());
        expected = "用/V 基于/P 统计/V 的/U 最/D 大/A 概率/N 分词/N 算法/N 对/P 自然/N 语言/N 文本/N 进行/V 分词/N 处理/V 。/W";
        Assert.assertEquals(expected, result.toString().trim());


        testInput = "放眼全世界，云计算喊得最响的、新闻报道最多的，非中美两国莫属。";
        result = SegmentEngine.getInstance().getSegmentWorker().segment(testInput);
        System.out.println(result.toString());
        expected = "放眼/V 全世界/N ，/W 云/NR 计算/V 喊/V 得/U 最/D 响/A 的/U 、/W 新闻/N 报道/V 最/D 多/M 的/U ，/W 非/J 中/J 美/J 两/M 国/N 莫/D 属/V 。/W";
        Assert.assertEquals(expected, result.toString().trim());

        testInput = "此外，她还创作了193首诗，被认为是英国一位天才型的女作家。";
        result = SegmentEngine.getInstance().getSegmentWorker().segment(testInput);
        System.out.println(result.toString());
        expected = "此外/C ，/W 她/R 还/D 创作/V 了/Y 193首/M 诗/N ，/W 被/P 认为/V 是/V 英国/NS 一/M 位/Q 天才/N 型/K 的/U 女/B 作家/N 。/W";
        Assert.assertEquals(expected, result.toString().trim());

        testInput = "《大卫·科波菲尔》通过主人公大卫一生的悲欢离合，多层次地揭示了当时社会的真实面貌，突出地表现了金钱对婚姻、家庭和社会的腐蚀作用。";
        result = SegmentEngine.getInstance().getSegmentWorker().segment(testInput);
        System.out.println(result.toString());
        expected = "《/UN 大卫/NR ·/UN 科/NR 波/NR 菲/NR 尔/NR 》/UN 通过/P 主人公/N 大卫/NR 一生/N 的/U 悲欢离合/I ，/W 多层次/N 地/U 揭示/V 了/U 当时/T 社会/N 的/U 真实/A 面貌/N ，/W 突出/A 地/U 表现/VN 了/U 金钱/N 对/P 婚姻/N 、/W 家庭/N 和/C 社会/N 的/U 腐蚀/VN 作用/N 。/W";
        Assert.assertEquals(expected, result.toString().trim());
    }

    @Test
    public void should_save_POS_Matrix() throws IOException {
        POSRecognizer recognizer = new POSRecognizer();
        File file = new File("pos.dat");
        SerializeHandler writeHandler = new SerializeHandler(file, SerializeHandler.MODE_WRITE_ONLY);
        recognizer.save(writeHandler);
    }

    @Test
    public void should_load_POS_Matrix() throws IOException {
        POSRecognizer recognizer = new POSRecognizer();
        File file = new File("pos.dat");
        SerializeHandler readHandler = new SerializeHandler(file, SerializeHandler.MODE_READ_ONLY);
        recognizer.load();
        file.deleteOnExit();
    }
}
