package edu.uestc.transformation.nlp.nlpforehandle.Latex;

import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: tjc
 * @create: 2020/9/22
 * @description:
 */

public class RegexMatcher {
    private static final Logger LOG = LogFactory.getLogger(edu.uestc.transformation.nlp.nlpforehandle.Latex.RegexMatcher.class);

    public RegexMatcher() {
    }

    public static Matcher getMatcher(String regex, String dataString) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(dataString);
    }
}
