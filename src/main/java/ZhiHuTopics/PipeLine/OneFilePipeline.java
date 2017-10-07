package ZhiHuTopics.PipeLine;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

/**
 * {@link OneFilePipeline} 类实现了 {@link Pipeline} 接口，自定义的爬取结果输出类。
 * 实现把同一 topic 下的 question 放置到以 fileName 为文件名的文件里面。若该文件已经存在，默认效果为追加。
 *
 * @author kissx on 2017/9/19.
 */
public class OneFilePipeline implements Pipeline {

    private String fileName;
    private boolean isAppend = true;

    /**
     * @param fileName 要保存文件的文件名
     */
    public OneFilePipeline(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param fileName 要保存文件的文件名
     * @param isAppend 若该文件已存在，是否追加。true 表示追加
     */
    public OneFilePipeline(String fileName, boolean isAppend) {
        this.fileName = fileName;
        this.isAppend = isAppend;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(fileName, isAppend))) {
            printWriter.println("url:\t" + resultItems.getRequest().getUrl());
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.println(entry.getKey() + ":");
                    for (Object o : value) {
                        printWriter.println(o);
                    }
                } else {
                    printWriter.println(entry.getKey() + ":\t" + entry.getValue());
                }
            }
            printWriter.println();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
