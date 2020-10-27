package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

import com.google.common.collect.Maps;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;

import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by liuyy on 2016/1/8.
 */
public class VectorParse {

    //List<String> regex= Lists.newArrayList("\\\\overrightarrow", "\\\\vec","\\\\overline");
    Map<String, String> regexmap = Maps.newHashMap();

    public VectorParse() {
        this.regexmap.put("\\\\overrightarrow", "↑");
        this.regexmap.put("\\\\vec", "↑");
        this.regexmap.put("\\\\overline", "conjugate_");

    }

    public String parse(String group, String str) {
        String old = group;
        Matcher matcher;
        for (Map.Entry<String, String> entry : regexmap.entrySet()) {
            matcher = RegularExpressionTools.getRegexMatcher(entry.getKey(), old);
            if (matcher.find()) {
                old = matcher.replaceAll(entry.getValue());
            }
        }
//        for (String s : regex) {
//            matcher= RegularExpressionTools.getRegexMatcher(s, old);
//            if (matcher.find()){
//                old=matcher.replaceAll("↑");
//            }
//        }

        if (old.startsWith("↑{")) {
            int start = old.indexOf("{");
            int flag = 1;
            int end = -1;
            for (int i = start + 1; i < old.length(); i++) {
                if (old.charAt(i) == '{') {
                    flag++;
                }
                if (old.charAt(i) == '}') {
                    flag--;
                }
                if (flag == 0) {
                    end = i;
                    break;
                }
            }
            if (flag == 0) {
                String now = old.substring(start, end + 1);
                String re = old.substring(start + 1, end);
                old = old.replace(now, re);
            }
        }

        return str.replace(group, old);
    }
}
