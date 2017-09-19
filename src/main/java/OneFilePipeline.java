import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.*;
import java.util.Map;

/**
 * @author kissx on 2017/9/19.
 */
public class OneFilePipeline implements Pipeline{

    private String fileName;

    public OneFilePipeline(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(fileName,true))) {
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
