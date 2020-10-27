package edu.uestc.transformation.nlp.nlpforehandle.Latex;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author: tjc
 * @create: 2020/9/23
 * @description:
 */

public class RegexUtils {
    private static final Logger LOG = LogFactory.getLogger(RegexUtils.class);

    public RegexUtils() {
    }

    public static boolean stringBufferReplaceAll(StringBuffer sb, String originRegex, String target) {
        if (sb == null) {
            return false;
        } else {
            boolean flag = false;
            Matcher matcher = RegexMatcher.getMatcher(originRegex, sb.toString());
            List<Integer> posBegin = new ArrayList();

            ArrayList posEnd;
            int i;
            int start;
            for(posEnd = new ArrayList(); matcher.find(); flag = true) {
                i = matcher.start();
                start = matcher.end();
                posBegin.add(i);
                posEnd.add(start);
            }

            for(i = posBegin.size() - 1; i >= 0; --i) {
                start = (Integer)posBegin.get(i);
                int end = (Integer)posEnd.get(i);
                sb.replace(start, end, target);
            }

            return flag;
        }
    }
}
