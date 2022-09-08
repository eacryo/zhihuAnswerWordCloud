package com.eacryo.zhihuAnswerWordCloud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class zhihuAnswerWordCloudApplicationTests {

    @Test
    void contextLoads() {
        String t = "<p data-pid=\"-xGFzKp0\">" +
                "这个回答下面已经有1600多条回复了。</p>" +
                "<p data-pid=\"4ABBBVet\">" +
                "今天翻了下，发现大家的焦点已经转到辅导班上去了，<img src>2222222<>而且关于辅导班的观点基本上是两边倒。</p>";
        System.out.println(t.replaceAll("<.*?>"," "));
    }

}
