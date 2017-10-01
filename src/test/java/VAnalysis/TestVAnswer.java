package VAnalysis;

import VAnalysis.pageProcess.VAnswerPageProcess;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * @author kissx on 2017/10/1.
 */
public class TestVAnswer {

    @Test
    public void test() {
        Spider spider = Spider.create(new VAnswerPageProcess())
                .addUrl("https://www.zhihu.com/people/sgai/answers?page=1")
                .addPipeline(new ConsolePipeline());
        spider.run();
    }

}
