package ZhiHuTopics.PageProcessor;

import ZhiHuTopics.PipeLine.OneFilePipeline;
import ZhiHuTopics.manager.SpiderManage;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.net.URLEncoder;
import java.util.List;

/**
 * {@link ZhiHuTopicPageProcessor} 类获取指定 topic（话题【通过 topicName 属性指定】）对应的 topicId。为爬取该话题下的 Question 做准备。
 * （继承 {@link BasePageProcessImp} ，实现 {@link PageProcessor} ）
 *
 * @author kissx on 2017/9/18.
 */
public class ZhiHuTopicPageProcessor extends BasePageProcessImp implements PageProcessor {

    private String topicName;
    private Site site;

    /**
     * 构造 ZhiHuTopicPageProcessor 类，即 topic 首页（第一个页面）处理类。一般生成匿名对象为 {@link us.codecraft.webmagic.Spider} 类构造提供参数。
     *
     * @param topicName 指定 topic
     * @param cookie    “z_c0” 对应的 Cookie 值
     */
    public ZhiHuTopicPageProcessor(String topicName, String cookie) {
        this.topicName = topicName;
        site = Site.me()
                .setRetrySleepTime(10)
                .setSleepTime(1000)
                .setTimeOut(10000)
                .setCharset("UTF-8")
                .setDomain("www.zhihu.com")
                .addCookie("z_c0", cookie);
    }

    /**
     * topicList 得到是一系列 topic 对应的 topicId，topicNameList 则获取的是一系列 topic 对应名称。它们一一对应，故通过索引可以得到指定 topic 对应的 topicId。
     * 然后开启新的爬虫，利用 POST 方式提交 topicId、offset 参数以获取指定 topic 下指定位置的 question。
     */
    @Override
    public void process(Page page) {
        List<String> topicList = page.getHtml().xpath("//li[@class=\"zm-topic-cat-item\"]/@data-id").all();
        List<String> topicNameList = page.getHtml().xpath("//li[@class=\"zm-topic-cat-item\"]/a/text()").all();
        int i = topicNameList.indexOf(topicName.substring(0, topicName.lastIndexOf(".")));
        String topic_id = topicList.get(i);
        try {
            // 下面拼接的 URL 是能够爬取指定 topic 下指定位置下 question 的关键
            Request request = new Request("https://www.zhihu.com/node/TopicFeedList?method=next&params=" + URLEncoder.encode("{\"offset\":" + 0 + ",\"topic_id\":" + topic_id + ",\"feed_type\":\"smart_feed\"}", "utf-8"));
            request.setMethod(HttpConstant.Method.POST);
            Spider spider = Spider.create(new ZhiHuQuestionProcessor(topic_id))
                    .addPipeline(new ConsolePipeline())
                    .addPipeline(new OneFilePipeline(topicName))
                    .addRequest(request);
            Thread.sleep(1000);
            SpiderManage spiderManage = SpiderManage.getInstance();
            spiderManage.addSpider(spider);
            spider.runAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
