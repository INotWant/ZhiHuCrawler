package VAnalysis;

import VAnalysis.pageProcess.VUserPageProcess;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * @author kissx on 2017/10/1.
 */
public class TestVUser {

    @Test
    public void test() throws InterruptedException {
        String cookie = "您的 cookie";
        Spider spider = Spider.create(new VUserPageProcess(cookie))
                .addUrl("https://www.zhihu.com/people/zhang-jia-wei/following")
                .addUrl("https://www.zhihu.com/people/kaifulee/following")
//                .addUrl("https://www.zhihu.com/people/zhang-jia-wei/following")
                .addPipeline(new ConsolePipeline());
        spider.run();
//        QueueScheduler scheduler = (QueueScheduler) spider.getScheduler();
//        while (true){
//            Thread.sleep(5000);
//            System.out.println("数量：" + scheduler.getTotalRequestsCount(spider));
//        }
    }

}
