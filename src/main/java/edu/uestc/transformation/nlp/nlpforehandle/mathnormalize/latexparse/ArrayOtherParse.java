package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

/**
 * Created by Lizo on 2014/10/23.
 */
public class ArrayOtherParse {
    public String arrayParse(String match, String orign) {
        //清楚多余的部分
        String removeStrings[] = {
                "\\opencurlybrace\\begin{matrix}",
                "\\{\\begin{array}{*{20}{c}}",
                "\\{\\begin{array}{*{35}{l}}",
                "\\{{\\begin{array}{*{20}{c}}",
                "\\{{\\begin{array}{{20}{c}}",
                "\\{{\\begin{array}{*{20}{l}}",// WangSK  QG2201707W
                "\\{\\begin{array}{l}",
                "\\{{\\begin{array}{l}",
                "\\{{\\begin{array}{1}",
                "\\{\\begin{array}{1}",
                "\\{{\\begin{array}",
                "\\{\\begin{align}",
                "\\begin{cases}",
                "\\{\\begin{aligned}",
                "\\{\\begin{gathered}",
                "\\{\\begin{matrix}",
                "{\\begin{gathered}",
                "{\\begin{matrix}",
                "\\\\\\\\end{gathered}",
                "\\\\\\end{gathered}",
                "\\\\end{gathered}",
                "\\end{gathered}",
                "\\end{matrix}",
                "\\end{array}}",
                "\\end{array}",
                "\\\\\\end{array}",
                "\\end{align}",
                "\\end{cases}",
                "\\end{aligned}"
        };

        String tempString = match.replace(" ", "");//清楚空格和$
        for (String removeString : removeStrings) {
            tempString = tempString.replace(removeString, "");
        }

//        if (tempString.indexOf("\\\\") == -1)
//            return orign;
        String regex = "(,|)(\\\\\\\\\\\\|\\\\\\\\)(?!(frac|sqrt|bar))";
        String replaceString = tempString.replaceAll(regex, "#");
        replaceString = removeOuterBrackets(replaceString);
        replaceString = deal(replaceString);
//        regex = "\\\\(?!(frac|sqrt|bar))";
//        replaceString = replaceString.replace(regex, Tools.array_flag);
        return orign.replace(match, replaceString);

    }

    private String deal(String string) {
        String[] exprList = StringUtils.split(string, "#");
        Matcher matcher;
        for (int i = 0; i < exprList.length; i++) {
            matcher = RegularExpressionTools.getRegexMatcher("(,)([^\\$]+?)(\\$|$)", exprList[i]);
            if (matcher.find()) {
                String con = matcher.group(2);
                exprList[i] = exprList[i].replace(con, "(" + con + ")").replaceFirst(",", "");
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < exprList.length; i++) {
            sb.append(exprList[i]);
            if (i != exprList.length - 1)
                sb.append("#");
        }
        return sb.toString();
    }

    private String removeOuterBrackets(String arrayFunction) {
        String[] functions = arrayFunction.split("#");
        String result = "";
        for (String function : functions) {
            if (existOuterBrackets(function)) {
                function = function.substring(1, function.length() - 1);
            }
            result = result + function + "#";
        }
        return result.substring(0, result.length() - 1);
    }

    private boolean existOuterBrackets(String expr) {
        if (expr.startsWith("{") && expr.endsWith("}")) {
            char[] words = expr.toCharArray();
            int leftBracketNums = 0;
            for (int i = 0; i < words.length; i++) {
                if (words[i] == '{')
                    leftBracketNums++;
                if (words[i] == '}')
                    leftBracketNums--;
                if (leftBracketNums == 0) {
                    return i == words.length - 1;
                }
            }
        }
        return false;
    }
}
