package com.example.testpachong1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ZhihuPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

//    static String url = "https://www.zhihu.com/api/v4/questions/63201551/feeds?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cattachment%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Cis_labeled%2Cpaid_info%2Cpaid_info_content%2Creaction_instruction%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_recognized%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cvip_info%2Cbadge%5B*%5D.topics%3Bdata%5B*%5D.settings.table_of_content.enabled&offset=&limit=3&order=default&platform=desktop";
    static String url = "https://www.zhihu.com/api/v4/questions/321741816/feeds?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cattachment%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Cis_labeled%2Cpaid_info%2Cpaid_info_content%2Creaction_instruction%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_recognized%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cvip_info%2Cbadge%5B*%5D.topics%3Bdata%5B*%5D.settings.table_of_content.enabled&offset=0&limit=5&order=default&platform=desktop";
    static List<ZhihuAnswerBean> allArticleList = new ArrayList<>();

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JsonPathSelector articleInfoSelector = new JsonPathSelector("$.data");
        JsonPathSelector contentSelector = new JsonPathSelector("$.target.content");
        JsonPathSelector commentCountSelector = new JsonPathSelector("$.target.comment_count");
        JsonPathSelector voteUpCountSelector = new JsonPathSelector("$.target.voteup_count");
        String nextPageUrl = new JsonPathSelector("$.paging.next").select(rawText);
        String isEnd = new JsonPathSelector("$.paging.is_end").select(rawText);
        String currentPage = new JsonPathSelector("$.paging.page").select(rawText);
        List<String> articleInfoList = articleInfoSelector.selectList(rawText);
        List<ZhihuAnswerBean> zhihuAnswerBeanListInPage = new ArrayList<>();
        for (int i = 0; i < articleInfoList.size(); i++) {
            ZhihuAnswerBean answerBean = new ZhihuAnswerBean();
            String currentArticleInfo = articleInfoList.get(i);
//            answerBean.setContent(contentSelector.select(currentArticleInfo).replaceAll("<p data-pid=\".*?\">|</p>"," "));
            answerBean.setContent(contentSelector.select(currentArticleInfo).replaceAll("<.*?>"," "));
            answerBean.setComment_count(commentCountSelector.select(currentArticleInfo));
            answerBean.setVoteup_count(voteUpCountSelector.select(currentArticleInfo));
            allArticleList.add(answerBean);
            zhihuAnswerBeanListInPage.add(answerBean);
        }
        //把获取的答案的bean写到page里面
        page.putField("zhihuAnswers",zhihuAnswerBeanListInPage);
        if("false".equals(isEnd)){
            page.addTargetRequest(nextPageUrl);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new ZhihuPageProcessor())
                .addUrl(url)
//                .addPipeline(new ConsolePipeline())
//                .addPipeline(new FilePipeline("C:\\Users\\admin\\Desktop"))
                .addPipeline(new ZhihuAnswer2TxtPipeLine("C:\\Users\\admin\\Desktop\\20220908zhihu1111.txt"))
                .thread(5)
                .run();
//        Iterator<ZhihuAnswerBean> iterator = allArticleList.iterator();
//        while (iterator.hasNext()){
//            System.out.println(iterator.next().toString());
//        }
        System.out.println("共获得"+allArticleList.size()+"条回答");
    }
}