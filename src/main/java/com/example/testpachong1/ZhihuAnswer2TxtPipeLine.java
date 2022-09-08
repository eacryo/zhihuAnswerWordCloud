package com.example.testpachong1;

import lombok.Data;
import lombok.SneakyThrows;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Data
public class ZhihuAnswer2TxtPipeLine implements Pipeline {
    //写入的txt文件的完整路径
    String txtFilePath;
    public ZhihuAnswer2TxtPipeLine(String txtFilePath){
        this.setTxtFilePath(txtFilePath);
    }
    @SneakyThrows
    @Override
    public void process(ResultItems resultItems, Task task) {
        String filePath = this.txtFilePath;
        List<ZhihuAnswerBean> list = resultItems.get("zhihuAnswers");
        String content = "";
        for (int i = 0; i < list.size(); i++) {
            content = content + list.get(i).getContent() + System.getProperty("line.separator");
        }
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.write(content);
        System.out.println("写入成功！");
    }
}
