package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;

import com.google.common.base.Joiner;
import edu.uestc.transformation.nlp.nlpforehandle.jsontorelationtools.UtilFunction;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;
import edu.uestc.transformation.nlp.nlpforehandle.util.Constant;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数学归一化
 * 重构
 * Modified by ZZH on 2015-11-20
 */
public class MathematicsNormalizeUtil {
    //初中代数常见实体
    private static final String[] entity = {"代数式", "不等式(组|)", "((一|二)元(一|二)次|)方程(组|)", "多项式", "抛物线"};
    //大题的语义同义化
    public static String normalize(String answerStr) {
        if (StringUtils.isEmpty(answerStr)) {
            return "";
        }

        //刪除多余的空格
        answerStr = answerStr.replaceAll("(?<!(&&[a-zO]{1,15}))(\\s)(?!([^&]+&&))", "").replaceAll("\\{\\}", "");
        //同义词转换
        String afterSimilar = SimilarWord.getInstance().similarWord(answerStr);
        afterSimilar = noStemProcess(afterSimilar);
        //删除一些不需要字符串
        afterSimilar = DeleteUncareStringUtil.deleteUncare(afterSimilar);

        StringBuffer ansStringBuffer = new StringBuffer(afterSimilar);
        //latex转换
        new LatexFilter().excuteImpl(ansStringBuffer);
        //处理一些不标准的格式，人为的进行一些简单的修正
        FormatAnswerStringUtil.format(ansStringBuffer);
        //Latex解析，将latex转换成书写方式，比如frac{{a},{b}}，转换为a/b
        String result = LatexParse.getInstance().parseAll(ansStringBuffer.toString());

        // todo 暂时取消$ 的替换
//        Matcher matcher2 = RegexMatcher.getMatcher("\\$[.[^@\\$]]+?\\$", result);
//        while (matcher2.find()) {
//            String oldstring = matcher2.group();
//            if (oldstring.contains("↑")) {
//                if (oldstring.endsWith("=$")) {
//                    result = result.replace("=$", "$=");
//                }
//                continue;
//            }
//
//            String newString = oldstring.replace("$", "");
//            result = result.replace(oldstring, newString);
//        }
        result = result.replaceAll("(?<!(&&[a-zO]{1,15}))( )(?!([^&]+&&))", "").replaceAll("\\{\\}", "");
        /**
         * 题干中容易出现是圆⊙O的切线的句式，导致解析后成"圆圆O"的形式
         */
        result = result.replaceAll("圆圆", "圆");
        result = checkSquare(result);
        StringBuffer stringBuffer = new StringBuffer(result);
        DeleteUncareStringUtil.deleteUncare(stringBuffer);
        while (stringBuffer.toString().endsWith("#%#")) {
            stringBuffer = stringBuffer.delete(stringBuffer.length() - 3, stringBuffer.length());
        }
        return delSymbolBeforeFunctionNumber(stringBuffer.toString());

    }

    /**
     * 把这种---> D(E^2)=A(D^2)+A(E^2) 变为---> (DE)^2=(AD)^2+(AE)^2
     * 这种情形纯属录题错误，我们只是义务帮忙修正，如果不测，直接注销此方法
     */
    public static String checkSquare(String sorString) {
        String regexLine = "([A-Z])\\(([A-Z])\\^2\\)";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String beginL = m.group(1);
            String endL = m.group(2);
            String resultL = "(" + beginL + endL + ")^2";
            int start = m.start();
            int end = m.end();
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(resultL);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();

    }


    public static String normalizeLineString(String answerString) throws Exception {
//        数学语义同一化
        return MathematicsNormalizeUtil.normalize(answerString);
    }

    private static String delSymbolBeforeFunctionNumber(String str) {
        List<String> stringList = UtilFunction.stringToListString(str);
        if (stringList.size() > 2) {
            if (RegularExpressionTools.getRegexMatcher(Constant.EQUATION_REFER, stringList.get(stringList.size() - 1)).find()) {
                if (RegularExpressionTools.getRegexMatcher(Constant.SYMBOL, stringList.get(stringList.size() - 2)).find()) {
                    stringList.remove(stringList.size() - 2);
                }
            }
        }
        return Joiner.on("").skipNulls().join(stringList);
    }

    private static String noStemProcess(String str) {
        if (str.startsWith("(1)"))
            return "#%#" + str;
        return str;
    }

    /**
     * 把代词替换为之前出现过的实体（不含有表达式）
     * @param stem
     * @param subStem
     * @return
     */
    public static String[] replaceWord (String stem, String subStem) {
        //结果数组
        String[] result = new String[2];
        //暂存句子中的实体
        List<String> temp = new ArrayList<>();
        String text = stem + subStem;
        String regex = LTools.createRegexOrByStringList(entity);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            //把句子中的实体存储起来
            temp.add(matcher.group());
        }
        if (temp.size() >= 1) {
            //把代词替换成暂存的第一个实体
            stem = stem.replaceAll("它", temp.get(0));
            subStem = subStem.replaceAll("它", temp.get(0));
        }
        result[0] = stem;
        result[1] = subStem;
        return result;
    }

}