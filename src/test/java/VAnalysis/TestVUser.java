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
        String cookie = "Mi4xZWNrSUFnQUFBQUFBWUlJNjZNaDFEQmNBQUFCaEFsVk41U1g0V1FDU1BpVXFWQ08zd2MtZUR4cTZsZ3NBWTFnLUVn|1506842853|af59e6155fd037eb05f11a92cfe09b9dad512a0a";
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
