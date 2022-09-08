package com.example.testpachong1;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class testPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    static String url = "https://so.csdn.net/api/v3/search?q=" +
            "{keyword}" +
            "&t=blog&p={currentPage}" +
            "&s=0&tm=0&lv=-1&ft=0&l=&u=&ct=-1&pnt=-1&ry=-1&ss=-1&dct=-1&vco=-1&cc=-1&sc=-1&akt=-1&art=-1&ca=-1&prs=&pre=&ecc=-1&ebc=-1&ia=1&dId=&cl=-1&scl=-1&tcl=-1&platform=pc";
    static String keyWord = "爬虫";
    static int currentPage = 1;
    static int totalPage;
    static List<CSDNArticleBean> allArticleList = new ArrayList<>();

    @Override
    public void process(Page page) {
        //page.addTargetRequests(page.getHtml().links().regex("(https://liyifeng.blog.csdn.net/article/details/\\d+)").all());
        String json = page.getRawText();
        List<String> articleInfoList = new JsonPathSelector("$.result_vos").selectList(json);
        JsonPathSelector articelIdSelector = new JsonPathSelector("$.articleid");
        JsonPathSelector authorSelector = new JsonPathSelector("$.author");
        JsonPathSelector createTimeSelector = new JsonPathSelector("$.created_at");
        JsonPathSelector titleSelector = new JsonPathSelector("$.title");
        JsonPathSelector introductionSelector = new JsonPathSelector("$.description");
        JsonPathSelector articleUrlSelector = new JsonPathSelector("$.url");
        for (int i = 0; i < articleInfoList.size(); i++) {
            CSDNArticleBean csdnArticleBean = new CSDNArticleBean();
            String currentArticleInfo = articleInfoList.get(i);
            csdnArticleBean.setArticleId(articelIdSelector.select(currentArticleInfo));
            csdnArticleBean.setAuthor(authorSelector.select(currentArticleInfo));
            csdnArticleBean.setTime(createTimeSelector.select(currentArticleInfo));
            csdnArticleBean.setTitle(titleSelector.select(currentArticleInfo).replace("<em>","").replace("</em>",""));
            csdnArticleBean.setBriefIntroduction(introductionSelector.select(currentArticleInfo).replace("<em>","").replace("</em>",""));
            csdnArticleBean.setArticleUrl(articleUrlSelector.select(currentArticleInfo));
//            System.out.println("csdnArticleBean = " + csdnArticleBean);
            allArticleList.add(csdnArticleBean);
        }
        totalPage = (int)Double.parseDouble(new JsonPathSelector("$.total_page").select(json));
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name")==null){
//            //skip this page
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
        if(currentPage<=3){
            currentPage++;
            page.addTargetRequest(url.replace("{keyword}",keyWord).replace("{currentPage}",String.valueOf(currentPage)));
        }
        System.out.println("size="+allArticleList.size());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {



        Spider.create(new testPageProcessor())
                .addUrl(url.replace("{keyword}",keyWord).replace("{currentPage}",String.valueOf(currentPage)))
                .addPipeline(new ConsolePipeline())
                .thread(1)
                .run();
        Iterator<CSDNArticleBean> iterator = allArticleList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }
}