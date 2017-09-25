package ZhiHuTopics;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author kissx on 2017/9/18.
 *         爬取知乎互联网 ----- topic
 */
public class ZhihuTopPageProcessor extends BasePageProcessImp implements PageProcessor {

    private String fileName;
    private Site site;

    public ZhihuTopPageProcessor(String fileName, String cookie) {
        this.fileName = fileName;
        site = Site.me()
                .setRetrySleepTime(10)
                .setSleepTime(1000)
                .setTimeOut(10000)
                .setCharset("UTF-8")
                .setDomain("www.zhihu.com")
                .addCookie("z_c0", cookie);
    }

    @Override
    public void process(Page page) {
        List<String> topicList = page.getHtml().xpath("//li[@class=\"zm-topic-cat-item\"]/@data-id").all();
        List<String> topicNameList = page.getHtml().xpath("//li[@class=\"zm-topic-cat-item\"]/a/text()").all();
        int i = topicNameList.indexOf(fileName.substring(0, fileName.lastIndexOf(".")));
        startUpSpider(page.getHtml(), fileName, topicList.get(i));
    }

    @Override
    public Site getSite() {
        return site;
    }
}
