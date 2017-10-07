package ZhiHuTopics;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author kissx on 2017/9/18.
 *         {@link ZhihuTopicPageProcessor} 类获取指定 topic（话题【topicName 属性指定】）对应的 topicId。为爬取该话题下的 Question 做准备。
 *         （继承 {@link BasePageProcessImp} ，实现 {@link PageProcessor} ）
 */
public class ZhihuTopicPageProcessor extends BasePageProcessImp implements PageProcessor {

    private String topicName;
    private Site site;

    /**
     * 构造 ZhihuTopicPageProcessor 类，即 topic 首页（第一个页面）处理类。一般生成匿名对象为 {@link us.codecraft.webmagic.Spider} 类构造提供参数。
     *
     * @param topicName 指定 topic
     * @param cookie    “z_c0” 对应的 Cookie 值
     */
    public ZhihuTopicPageProcessor(String topicName, String cookie) {
        this.topicName = topicName;
        site = Site.me()
                .setRetrySleepTime(10)
                .setSleepTime(1000)
                .setTimeOut(10000)
                .setCharset("UTF-8")
                .setDomain("www.zhihu.com")
                .addCookie("z_c0", cookie);     //设置 Cookie
    }

    /**
     * topicList 得到是一系列 topic 对应的 topicId，topicNameList 则获取的是一系列 topic 对应名称。它们一一对应，故通过索引可以得到指定 topic 对应的 topicId。
     */
    @Override
    public void process(Page page) {
        List<String> topicList = page.getHtml().xpath("//li[@class=\"zm-topic-cat-item\"]/@data-id").all();
        List<String> topicNameList = page.getHtml().xpath("//li[@class=\"zm-topic-cat-item\"]/a/text()").all();
        int i = topicNameList.indexOf(topicName.substring(0, topicName.lastIndexOf(".")));
        startUpSpider(page.getHtml(), topicName, topicList.get(i));
    }

    @Override
    public Site getSite() {
        return site;
    }
}
