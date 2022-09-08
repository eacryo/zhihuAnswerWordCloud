package com.eacryo.zhihuAnswerWordCloud;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class KumoWordCloud {
    public void generatePic(String txtFilePath,String picFilePath) throws Exception {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
//        frequencyAnalyzer.setMinWordLength(2);
//        frequencyAnalyzer.setMaxWordLength(5);
        frequencyAnalyzer.setWordFrequenciesToReturn(100);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(txtFilePath);
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        // 我也不知道有啥用 但是不加中文会乱码
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 18);
        wordCloud.setKumoFont(new KumoFont(font));
        //图片中词语和词语之间的间距
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        //字体大小范围
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        System.out.println(wordFrequencies);
        wordCloud.writeToFile(picFilePath);
    }
}
