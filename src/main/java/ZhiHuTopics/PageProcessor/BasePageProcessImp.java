package ZhiHuTopics.PageProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

public class BasePageProcessImp implements BasePageProcessInterface {

    /**
     * Json 解析
     */
    public Html jsonProcess(Page page) {
        Json json = page.getJson();
        String content = json.jsonPath("$..msg").get();
        // 防止返回内容为空
        if (content.length() > 4) {
            content = content.substring(2, content.length() - 2);
            // 特殊符号转换
            content = transfer(content);
        } else
            page.setSkip(true);
        return new Html(content);
    }

    /**
     * 特殊符号转换
     */
    private String transfer(String jsonStr) {
        jsonStr = jsonStr.replaceAll("\\\\\"", "\"");
        jsonStr = jsonStr.replaceAll("\\\\n", "<br/>");
        jsonStr = jsonStr.replaceAll("\\\\/", "/");
        return jsonStr;
    }
}
