package com.eacryo.zhihuAnswerWordCloud;

import us.codecraft.webmagic.Spider;

public class generatePicture {

    public static void main(String[] args) {
        //知乎回答的第一条url
        String url = "https://www.zhihu.com/api/v4/questions/63201551/feeds?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Cis_sticky%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Ceditable_content%2Cattachment%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Ccreated_time%2Cupdated_time%2Creview_info%2Crelevant_info%2Cquestion%2Cexcerpt%2Cis_labeled%2Cpaid_info%2Cpaid_info_content%2Creaction_instruction%2Crelationship.is_authorized%2Cis_author%2Cvoting%2Cis_thanked%2Cis_nothelp%2Cis_recognized%3Bdata%5B*%5D.mark_infos%5B*%5D.url%3Bdata%5B*%5D.author.follower_count%2Cvip_info%2Cbadge%5B*%5D.topics%3Bdata%5B*%5D.settings.table_of_content.enabled&offset=&limit=3&order=default&platform=desktop";


        long currentTimeMillis = System.currentTimeMillis();
        String timestamp = String.valueOf(currentTimeMillis / 1000);
        //保存回答文本的txt文件路径
        String filePath = "C:\\Users\\admin\\Desktop\\zhihuanswer"+ timestamp +".txt";
        //生成图片文件的路径
        String picturePath = "C:\\Users\\admin\\Desktop\\"+ timestamp +".png";
        Spider.create(new ZhihuPageProcessor()).addUrl(url).addPipeline(new ZhihuAnswer2TxtPipeLine(filePath)).thread(5).run();
        KumoWordCloud kumoWordCloud = new KumoWordCloud();
        try {
            kumoWordCloud.generatePic(filePath,picturePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
