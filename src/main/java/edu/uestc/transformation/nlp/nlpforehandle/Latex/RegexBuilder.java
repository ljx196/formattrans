package edu.uestc.transformation.nlp.nlpforehandle.Latex;

import org.apache.logging.log4j.Logger;

/**
 * @author: tjc
 * @create: 2020/9/22
 * @description:
 */

public class RegexBuilder {
    private static final Logger LOG = LogFactory.getLogger(edu.uestc.transformation.nlp.nlpforehandle.Latex.RegexBuilder.class);

    public RegexBuilder() {
    }

    public static String createRegexOrByStringList(String[] regexs) {
        StringBuilder stringBuilder = new StringBuilder("(");
        String[] arr$ = regexs;
        int len$ = regexs.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            stringBuilder.append("(");
            stringBuilder.append(s);
            stringBuilder.append(")");
            stringBuilder.append("|");
        }

        int len = stringBuilder.length();
        stringBuilder.replace(len - 1, len, ")");
        return stringBuilder.toString();
    }
}
