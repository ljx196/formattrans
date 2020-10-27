package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;

import com.google.common.collect.Lists;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LatexConstants;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.RegexBuilder;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.RegexMatcher;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.RegexUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Latex 字符串过滤器
 * 对不规范的latex格式进行修正
 *
 * @author B1-101
 *         <p/>
 *         重构
 *         Modified by Lizo on 2015-01-08
 */
public class LatexFilter {

    /**
     * $与一些元素的非法组合
     */
    private final String[] containDollarSetStrings = {
            "^\\$:", "$",
            "^\\$∵\\$", "∵",
            "^\\$∴\\$", "∴",
            "^\\$，\\$", "，",
            "^\\$,\\$", ",",
            ",\\$", "$,",
            "。\\$", "$,",
            ";\\$", "$,",
            "；\\$", "$,",
            "^\\$∵", "∵$",
            "^\\$∴", "∴$",
            "^\\$，", "，$",
            "^\\$,", ",$",
            "^\\$\\.(?!\\.\\.)", "$",
            "^\\$。", "$",
            "(?<!\\.\\.)\\.\\$", "$",
            "\\$(\\)|）)", "$",
            "(\\(|（)\\$", "($",
            "\\\\\\[", "[",
            "\\\\\\]", "]",
            "\\{Rt\\}", "Rt"
    };


    /**
     * 处理多余$$的情形
     */
    private final String[] spareDollarSet = {
            "\\$△\\$",
            "\\$Rt△\\$",
            //"\\$(x|y)\\$轴",
            "^\\$\\([0-9]\\)\\$",
            "^\\$(\\+|\\*|×|/|÷|-|﹣|:|−|=|>|<|≥|≤|≠|≈|≡|＜|,)+\\$$"
    };
    private final String spareDollarSetRegex = RegexBuilder.createRegexOrByStringList(spareDollarSet);


    public void excuteImpl(StringBuffer answerStringBuffer) {
        filterBefor(answerStringBuffer);
        Matcher matcher = RegexMatcher.getMatcher(LatexConstants.latexregex, answerStringBuffer.toString());
        List<ReplaceData> replaceDatas = Lists.newLinkedList();

        while (matcher.find()) {
            String latexString = matcher.group();
            StringBuffer tempStringBuffer = new StringBuffer(latexString);

            this.removeLatexFlag(tempStringBuffer);

            this.dealIllegalLatexFlag(tempStringBuffer);

            if (!latexString.equals(tempStringBuffer.toString())) {
                replaceDatas.add(new ReplaceData(tempStringBuffer.toString(), matcher.start(), matcher.end()));
            }
        }

        Collections.sort(replaceDatas);
        for (int i = replaceDatas.size() - 1; i >= 0; i--) {
            ReplaceData replaceData = replaceDatas.get(i);
            answerStringBuffer.replace(replaceData.start, replaceData.end, replaceData.target);
        }
    }

    /**
     * 处理多余$$
     *
     * @param element str
     */
    public void filterBefor(StringBuffer element) {
        for (String string : spareDollarSet) {
            Matcher matcher = RegexMatcher.getMatcher(string, element.toString());
            while (matcher.find()) {
                String group = matcher.group();
                element.replace(matcher.start(), matcher.end(), group.replace("$", ""));
                matcher = RegexMatcher.getMatcher(string, element.toString());
            }
        }
    }


    //删除一些不是数学公式，但是用latexflag包含的
    public void removeLatexFlag(StringBuffer latex) {
        Matcher matcher = RegexMatcher.getMatcher(spareDollarSetRegex, latex.toString());
        while (matcher.find()) {
            String matchString = matcher.group();
            latex.replace(matcher.start(), matcher.end(), matchString.replace("$", ""));
            matcher = RegexMatcher.getMatcher(spareDollarSetRegex, latex.toString());
        }
    }

    /**
     * 消除一些元素与$的不合法组合
     *
     * @param latex latex
     * @return result
     */
    private StringBuffer dealIllegalLatexFlag(StringBuffer latex) {
        for (int i = 0; i < containDollarSetStrings.length - 1; i = i + 2) {
            String s1 = containDollarSetStrings[i];
            String s2 = containDollarSetStrings[i + 1];
            RegexUtils.stringBufferReplaceAll(latex, s1, s2);
        }

        return latex;
    }

}

