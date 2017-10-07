package ZhiHuTopics.PageProcessor;

import ZhiHuTopics.manager.SpiderManage;
import ZhiHuTopics.manager.Statistics;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ZhiHuQuestionProcessor} 类实现 {@link PageProcessor} 接口，作为页面处理类，主要有两个中处理：
 * （一）处理问题页面（e.g. https://www.zhihu.com/question/66284095）,此时会获取 question 对应的标题、关注数、浏览数等
 * （二）处理 post 请求返回的 Json 数据，产生两种新的 url。一种，是新的 post 请求用以爬取下一个偏移的 question ，请求返回同样是 Json 数据。另一种，是问题 url （e.g. https://www.zhihu.com/question/66284095）。
 *
 * @author kissx on 2017/9/18.
 */
public class ZhiHuQuestionProcessor extends BasePageProcessImp implements PageProcessor {

    private String topic_id;

    /**
     * @param topic_id 指定 topic 对应的 topicId
     */
    public ZhiHuQuestionProcessor(String topic_id) {
        this.topic_id = topic_id;
    }

    private Site site = Site.me()
            .setRetrySleepTime(10)
            .setSleepTime(1000)
            .setTimeOut(10000)
            .setCharset("UTF-8");

    /**
     * 页面处理主方法（包含两种处理，见上述）
     */
    @Override
    public void process(Page page) {
        String URL_QUESTION = "https://www\\.zhihu\\.com/question/.*";
        // if 条件满足，则进行第一种，即问题页面处理
        if (page.getUrl().regex(URL_QUESTION).match()) {
            page.putField("标题", page.getHtml().xpath("//h1[@class=\"QuestionHeader-title\"]/text()").get());
            List<String> list = page.getHtml().xpath("//div[@class=\"NumberBoard-value\"]/text()").all();
            if (list != null && list.size() >= 2) {
                page.putField("关注者", list.get(0));
                page.putField("浏览人数", list.get(1));
                if (Statistics.getInstance() != null) {
                    Statistics statistics = Statistics.getInstance();
                    statistics.increase();
                    if (!statistics.compareDemandNum())
                        SpiderManage.getInstance().deleteAll();
                }
            } else
                page.setSkip(true);
        } else {
            // 以下进行第二种页面处理
            Html html = jsonProcess(page);
            List<String> urlList = html.xpath("//a[@class=\"question_link\"]/@href").all();
            List<String> newUrlList = new ArrayList<>();
            for (String urlStr : urlList) {
                urlStr = "https://www.zhihu.com" + urlStr;
                newUrlList.add(urlStr);
            }
            page.addTargetRequests(newUrlList);
            List<String> list = html.xpath("//div[@class=\"feed-item feed-item-hook  folding\"]/@data-score").all();
            if (list != null && list.size() > 0 && topic_id != null) {
                String offset = list.get(list.size() - 1);
                try {
                    // 下面拼接的 URL 是能够爬取指定 topic 下指定位置下 question 的关键
                    Request request = new Request("https://www.zhihu.com/node/TopicFeedList?method=next&params=" + URLEncoder.encode("{\"offset\":" + offset + ",\"topic_id\":" + topic_id + ",\"feed_type\":\"smart_feed\"}", "utf-8"));
                    request.setMethod(HttpConstant.Method.POST);
                    page.addTargetRequest(request);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
