package com.example.testpachong1;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.FrequencyFileLoader;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
public class TagWordCloudService {

    // 照片纵横比
    private double ratio = 1;
    // 获取词云图片
    // wordFile：单词及其频率文件路径
    // pngOutputPath：图片输出路径，应该以.png结尾
    // shapePicPath：词云形状图片路径，其背景应为透明背景，格式为png
    public void generate(String wordFile, String pngOutputPath, String shapePicPath) throws IOException {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
//         共检索多少个词
        frequencyAnalyzer.setWordFrequenciesToReturn(1000);
//         单词最短长度，一个汉字和一个英文字符都是1
        frequencyAnalyzer.setMinWordLength(2);
//        frequencyAnalyzer.setStopWords(loadStopWords());
//         设置中文支持，另一种加载方式不用设置
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        final List<WordFrequency> wordFrequencies;
        // 加载词云有两种方式，一种是在txt文件中统计词出现的个数，另一种是直接给出每个词出现的次数，这里使用第二种
        // 文件格式如下
//        100: frog
//        94: dog
//        43: cog
//        20: bog
        FrequencyFileLoader frequencyFileLoader = new FrequencyFileLoader();
        File file = new File(wordFile);
//        wordFrequencies = frequencyFileLoader.load(file);
        wordFrequencies = frequencyAnalyzer.load(file);
        // 生成图片的像素大小
        final Dimension dimension = new Dimension(1024, (int)(1024*ratio));
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        // 调节词云的稀疏程度，越高越稀疏
        wordCloud.setPadding(10);

        //设置背景色
        wordCloud.setBackgroundColor(new Color(255,255,255));
        //设置背景图片
        wordCloud.setBackground(new PixelBoundaryBackground(shapePicPath));

        // 颜色模板，不同频率的颜色会不同
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        // 设置字体
        java.awt.Font font = new java.awt.Font("楷体", 0, 20);
        wordCloud.setKumoFont(new KumoFont(font));
        // 设置偏转角，角度为0时，字体都是水平的
//        wordCloud.setAngleGenerator(new AngleGenerator(0, 90, 9));
        wordCloud.setAngleGenerator(new AngleGenerator(0));
        // 字体的大小范围，最小是多少，最大是多少
        wordCloud.setFontScalar(new SqrtFontScalar(5, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(pngOutputPath);
    }
}

