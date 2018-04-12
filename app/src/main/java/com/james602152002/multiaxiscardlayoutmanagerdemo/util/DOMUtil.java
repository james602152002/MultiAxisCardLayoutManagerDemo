package com.james602152002.multiaxiscardlayoutmanagerdemo.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DOMUtil {
    public static String justify_text_with_max_lines_str;

    public static String fetchJustifyTextDOM(String content, float text_size, int max_lines) {
        if (justify_text_with_max_lines_str == null) {
            Document doc = Jsoup.parse("");
            doc.head().appendElement("style").attr("type", "text/css")
                    .append("p{ font-size:%spx; text-overflow: ellipsis; overflow: hidden; display: -webkit-box; -webkit-line-clamp: %s; -webkit-box-orient: vertical;}");
            doc.body().appendElement("p").attr("style", "text-align:justify").append("%s");
            justify_text_with_max_lines_str = doc.outerHtml();
        }

        return String.format(justify_text_with_max_lines_str, text_size, max_lines, content);
    }
}
