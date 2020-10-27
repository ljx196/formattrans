package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;


import edu.uestc.transformation.nlp.nlpforehandle.Latex.ResourceFinder;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LatexPattern;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.RegexMatcher;
import edu.uestc.transformation.nlp.nlpforehandle.jsontorelationtools.UtilFunction;
import edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse.*;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Latex字符串解析成普通符号函数
 *
 * @author wangyong
 */
public class LatexParse {


    private static final Logger LOG = LogFactory.getLogger(LatexParse.class);

    private static final String LATEX_PATH = "xml/latexdir/latex_local.xml";
    private static  Pattern REGEX =  Pattern.compile("#[a-zA-Z0-9]+#");
    private static  Pattern REGEX1 =  Pattern.compile("(A|C)_(\\d+)\\^(\\d+)");

    private static final LatexParse latexParser = new LatexParse();

    private List<LatexPattern> patternset = new ArrayList<>();

    private LatexParse() {
        try {
            initFromFile(LATEX_PATH);
        } catch (JDOMException e) {
            LOG.error("failed to parse {}", LATEX_PATH);
        } catch (IOException e) {
            LOG.error("failed to find {}", LATEX_PATH);
        }
    }

    public static LatexParse getInstance() {
        return latexParser;
    }

    private void initFromFile(String path) throws JDOMException, IOException {
        Document doc = ResourceFinder.readXml(path);
        Element root = doc.getRootElement();
        List rules = root.getChildren("pattern");
        for (Object rule1 : rules) {
            LatexPattern pattern = new LatexPattern();
            Element rule = (Element) rule1;
            pattern.setTagName(rule.getAttributeValue("tag"));
            pattern.setPatrern(rule.getText());
            patternset.add(pattern);
        }
    }

    /**
     * 对整个字符串进行解析
     */

    public String parseAll(String str) {
        Matcher matcher = RegexMatcher.getMatcher("\\$\\$[.[^@\\$]]+?\\$\\$", str);
        // 实例化的结论就不需要再次进行转maple
        // 如 $#simp#{k_n}=k_1+d*(n-1):{k_n}&k_1&d$
      //  Pattern regex = Pattern.compile("#[a-zA-Z0-9]+#");

        // 当第一次调用LaTeX转maple 的时候会将实例化中的坐标添加$符号，会对后面的分析照成影响，因此要修正一下
        str = correctionStr(str, REGEX);
        while (matcher.find()) {
            String oldstring = matcher.group();
            if (!REGEX.matcher(oldstring).find()) {
                String newstring = parse(oldstring.replace("$$", "$"));
                str = str.replace(oldstring, newstring);
            }

        }
        Matcher matcher2 = RegexMatcher.getMatcher("\\$[.[^@\\$]]+?\\$", str);

        while (matcher2.find()) {
            String oldstring = matcher2.group();
            if (!REGEX.matcher(oldstring).find()) {
                String newstring = parse(oldstring);
                str = str.replace(oldstring, newstring);
            }

        }
        str = parseAC(str);

        return str;
    }

    /**
     * 修正一下第一次实例化结论调用LaTeX转maple的时候，会将结论中含有坐标的部分添加$ 符号，这会对后面的分析有影响，需要去除。
     * 如 函数f(x)在区间$(a,b)$上的最小值为#minOfFunWitnInterval#f(x)&$(a,b)$ ,
     * 修改成 函数f(x)在区间$(a,b)$上的最小值为#minOfFunWitnInterval#f(x)&(a,b)
     *
     * @param str   原字符串
     * @param regex 获得实例化结论的正则
     * @return 返回修改后的字符串
     */
    private String correctionStr(String str, Pattern regex) {
        StringBuilder sb = new StringBuilder();
        if (regex.matcher(str).find()) {
            if (str.indexOf("#")>2&&str.charAt(str.indexOf("#")-1) != '$') {
                // 说明是第一次调用LaTeX 转maple，需要注意实例化中的，坐标可能会加上$,会对后面的分析照成影响，因此要去除
                sb.append(str, 0, str.indexOf("#"));
                str = str.substring(str.indexOf("#"));
                str = str.replace("$", "");
                sb.append(str);
                str = sb.toString();
            }
        }
        return str;
    }

    private String parseAC(String sorString) {
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
      //  Pattern pattern = Pattern.compile("(A|C)_(\\d+)\\^(\\d+)");
        Matcher matcher = REGEX1.matcher(sorString);
        while (matcher.find()) {
            int i = matcher.start();
            int j = matcher.end();
            String a = matcher.group();

            String out;
            if (a.startsWith("A_")) {
                out = "Arr_" + matcher.group(2) + "_" + matcher.group(3);
            } else {
                out = "Com_" + matcher.group(2) + "_" + matcher.group(3);
            }
            stringBuilder.append(sorString.substring(pos, i));
            stringBuilder.append(out);
            pos = j;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * latex 解析函数
     */
    public String parse(String str) {
        //处理表达式多余的括号
        str = makeBrackets(str);
        //过滤掉干扰字符
        int loopcount = maxNestDeep(str);//嵌套的深度
        //解决表达式嵌套的情况
        while (loopcount-- != 0) {
            for (LatexPattern pattern : patternset) {
                String tagName = pattern.getTagName();

                String old = str;
                Matcher match = RegularExpressionTools.getRegexMatcher(pattern.getPatrern().replaceAll(" ", ""), str);
                boolean outloop = false;
                while (match.find()) {
                    if (tagName.equals("分式")) {
                        FracParse fracParse = new FracParse();
                        StringBuffer stringBuffer = new StringBuffer(str);
                        str = fracParse.fracParse(old, match.start(), stringBuffer);
                    } else if (tagName.equals("根式")) {
                        SqrtParse sqrtParse = new SqrtParse();
                        str = sqrtParse.sqrtParse(old, match.start(), str);
                    } else if (tagName.equals("幂")) {
                        SuperScriptParse superScript = new SuperScriptParse();
                        str = superScript.superScriptParse(old, match.start(), str);
                    } else if (tagName.equals("下标")) {
                        str = subScriptParse(old, match.start(), str);
                    } else if (tagName.equals("ImprFrac")) {
                        ImproperFrac improperFrac = new ImproperFrac();
                        str = improperFrac.improperFracParse(old, match.start(), str);
                    } else if (tagName.equals("补集")) {
                        ComplementParse arrayOtherParse = new ComplementParse();
                        str = arrayOtherParse.Parse(old, match.start(), str);
                    } else if (tagName.equals("Log")) {
                        LogParse logParse = new LogParse();
                        str = logParse.LogParse2(old, match.start(), str);
                    } else if (tagName.equals("Log2")) {
                        LogParse logParse = new LogParse();
                        str = logParse.LogParse(old, match.start(), str);
                    } else if (tagName.equals("Array") || tagName.equals("Array_2") || tagName.equals("Array_3") || tagName.equals("Array_4") ||
                            tagName.equals("Array_5") || tagName.equals("Array_6") ||
                            tagName.equals("Array_7") || tagName.equals("Array_8") ||
                            tagName.equals("Array_9") || tagName.equals("Array_10") || tagName.equals("Array_11") || tagName.equals("Array_12") ||
                            tagName.equals("Array_13") || tagName.equals("Array_14") || tagName.equals("Array_15") || tagName.equals("Array_16")
                    ) {
                        ArrayOtherParse arrayOtherParse = new ArrayOtherParse();
                        str = arrayOtherParse.arrayParse(match.group(), str);
                        //str = str.replace(",", "");
                    } else if (tagName.equals("向量")) {
                        VectorParse vectorParse = new VectorParse();
                        str = vectorParse.parse(match.group(), str);
                    } else if (tagName.equals("Adjoin")) {

                        AdJoinParse adjoinParse = new AdJoinParse();
                        StringBuffer tagStr = new StringBuffer();
                        str = adjoinParse.parse(str, match.start(), tagStr);
                        if (tagStr.length() > 0)
                            match = Pattern.compile("/[0-9a-z]").matcher(str);
                        tagStr.setLength(0);
                    } else if (tagName.equals("Trigonometric")) {
                        Trigonometric trigonometric = new Trigonometric();
                        str = trigonometric.trigonometricParse(old, match.start(), str);
                    } else if (tagName.equals("bar")) {
                        BarParse barParse = new BarParse();
                        str = barParse.barParse(old, match.start(), str);
                    }
                }
                if (outloop) break;
            }
        }
        str = removLatexUselessBraces(str);
        str = moreTypeParse(str);
        str = removeSpareDoubleBraces(str);
        str = removeUselessBraces(str);//去除多余的括号
        str = removeLastSymbol(str);
        return str;
    }

    /**
     * 消除最后一个多余的"#"
     *
     * @param str
     * @return
     */
    private String removeLastSymbol(String str) {
        if (str.endsWith("#$")) {
            str = str.substring(0, str.length() - 2) + "$";
        }
        return str;
    }

    /**
     * 消除几何双括号
     *
     * @param str
     * @return
     */
    private String removeSpareDoubleBraces(String str) {
        Matcher matcher = RegularExpressionTools.getRegexMatcher("\\{\\{([A-Z]('|_[0-9]|)|∠|△)+\\}\\}", str);
        while (matcher.find()) {
            String tempstr = matcher.group();
            tempstr = tempstr.replace("{", "");
            tempstr = tempstr.replace("}", "");
            str = str.replace(matcher.group(), tempstr);
        }
        return str;
    }

    private String removeUselessBraces(String s) {
       if(s.contains("sqrt")) return s;
        //下标
        String underLine = "(_(\\d+|[a-z]|\\([a-z](\\+|\\-|\\*|/)\\d+\\)))?";
        //函数名
        String functionName = "(\\(|)" + "((\\(|)(f|p|q|g|h|F|G|H|S|φ)(\\)|))" + underLine + "('|\\^\\(\\-1\\))?" + "(\\)|)";
        //整体情况
        String regex = "(?<!ln|lg|sin|cos|tan|]|abs|" + functionName + ")\\((\\d+|[a-zA-Z]" + underLine + "|ln|lg|sin|cos|tan|abs|\\+|\\-|=|" + functionName + ")\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        boolean is = true;
        while (is) {
            matcher.reset();
            StringBuffer sb2 = new StringBuffer();
            while (matcher.find()) {
                String ss = matcher.group();
                String sb = ss.substring(1, ss.length() - 1);
                matcher.appendReplacement(sb2, sb);
            }
            matcher.appendTail(sb2);
            s = sb2.toString();
            matcher = RegularExpressionTools.getRegexMatcher(regex, s);
            is = matcher.find();
        }

        matcher = RegularExpressionTools.getRegexMatcher("\\({2}[^\\(\\)]+?\\){2}", s);
        while (matcher.find()) {
            String ss = matcher.group();
            s = s.replace(ss, ss.substring(1, ss.length() - 1));
        }

        matcher = RegularExpressionTools.getRegexMatcher("(?<=⌒)\\([A-Z]{2,3}\\)", s);//⌒ (BC)变为⌒BC
        while (matcher.find()) {
            String ss = matcher.group();
            s = s.replace(ss, ss.substring(1, ss.length() - 1));
        }
        //处理类似{(1/a_n)}变为{1/a_n}
        if (s.startsWith("${(") && s.endsWith(")}$") && !s.contains("|")) {
            s = "${" + s.substring(3, s.length() - 3) + "}$";
        }
        //处理△AF_1(F_2)的情况
        matcher = RegularExpressionTools.getRegexMatcher("(?<=[A-Z](_\\d|)[A-Z](_\\d|))\\([A-Z](_\\d|)\\)", s);
        if (matcher.find()) {
            String ss = matcher.group();
            s = s.replace(ss, ss.substring(1, ss.length() - 1));
        }

        //处理F_1(F_2)的情况
        matcher = RegularExpressionTools.getRegexMatcher("(?<=[A-Z](_\\d|))\\([A-Z](_\\d|)\\)", s);
        if (matcher.find()) {
            String ss = matcher.group();
            s = s.replace(ss, ss.substring(1, ss.length() - 1));
        }
        //处理(log[(1/2)])(1/3)的情况
        matcher = RegularExpressionTools.getRegexMatcher("\\(log\\[[\\(\\)1-9a-z\\+\\-\\*/\\^]+\\]\\)", s);
        if (matcher.find()) {
            String ss = matcher.group();
            s = s.replace(ss, ss.substring(1, ss.length() - 1));
        }
        //处理((λ)_1)的情况
        matcher = RegularExpressionTools.getRegexMatcher("\\(\\(λ\\)_[1-9]\\)", s);
        if (matcher.find()) {
            s = s.replaceAll("\\(", "").replaceAll("\\)", "");
        }
        return s;
    }

    /**
     * 从latex形式中去除无用的花括号
     * 若此处出问题,请找liri
     *
     * @Data: 17/03.
     */
    private String removLatexUselessBraces(String str) {
        //去除区间中形如({-1,2})的花括号
        Matcher matcher = RegularExpressionTools.getRegexMatcher("\\$\\(\\{[^\\u4E00-\\u9FA5\\}]+?,[^\\u4E00-\\u9FA5\\{]+?\\}\\)\\$", str);
        while (matcher.find()) {
            String replace = matcher.group().replace("({", "(").replace("})", ")");
            str = str.replace(matcher.group(), replace);
        }
        return str;
    }

    /**
     * latex 下标解析函数
     *
     * @return
     */
    private String subScriptParse(String str, int start, String replacestr) {
        int loop = 1;
        int end = -1;
        for (int i = start + 2; i < str.length(); i++) {
            if (str.charAt(i) == '}') loop--;
            if (str.charAt(i) == '{') loop++;
            if (loop == 0) {
                end = i;
                break;
            }
        }
        if (end > start) {
            String oldstring = str.substring(start, end + 1);
            String subStr = str.substring(start + 2, end);
            String newstr;
            if (subStr.length() == 1 || subStr.matches("\\d+")) {//若下标为纯数字或者单个字符则不加括号,否则下标加括号.如a_{n}变为a_n,而a_{n+1}变为a_(n+1)
                newstr = "_" + str.substring(start + 2, end) + "";
            } else {
                newstr = "_(" + str.substring(start + 2, end) + ")";
            }

            return replacestr.replace(oldstring, newstr);
        }
//        if (end > start) {
//            String oldstring = str.substring(start, end + 1);
//            String newstr = "_" + str.substring(start + 2, end) + "";
//            return replacestr.replace(oldstring, newstr);
//        }
        return replacestr;
    }

    private String moreTypeParse(String basestr) {
        // 这里会对集合中的大括号替换成圆括号，暂时取消正则替换
        // 如A={1,2,3}或者 B= {x|x>1}
//        basestr = basestr.replaceAll("(?<!\\\\)\\{", "(");
        basestr = basestr.replaceAll("\\\\\\{", "{");
//        basestr = basestr.replaceAll("(?<!\\\\)\\}", ")");
        basestr = basestr.replaceAll("\\\\\\}", "}");

        //对等式或不等式进行最外层括号剥离,例如  $((AB)^(2)+(BC)^(2)=(EF)^(2))$ 变为 $(AB)^(2)+(BC)^(2)=(EF)^(2)$
        if (basestr.matches(".*[\\u4E00-\\u9FA5].*")) {
            return basestr;
        }
        if ((!UtilFunction.isInEquality(basestr)) && (!basestr.contains("="))) {
            return basestr;
        }
        String[] checkingS = basestr.split("=|≥|>|＜|≤|<|≠");
        if (checkingS.length != 2) {
            return basestr;
        }
        if ((!checkingS[0].startsWith("$(")) || (!checkingS[1].startsWith(")$"))) {
            return basestr;
        }
        if (StringUtils.countMatches(checkingS[0], "(") != StringUtils.countMatches(checkingS[0], ")") + 1) {
            return basestr;
        }
        if (StringUtils.countMatches(checkingS[1], ")") != StringUtils.countMatches(checkingS[1], "(") + 1) {
            return basestr;
        }
        basestr = "$" + basestr.substring(2, basestr.length() - 2) + "$";
        return basestr;
    }

    /**
     * latex 解析无实际意义的{}部分
     *
     * @param basestr
     * @return
     */
    private String noneTypeParse(String basestr) {
        Matcher matcher = RegularExpressionTools.getRegexMatcher("(\\{.+?\\|.+?\\})|(\\{[^,]+(,[^,]+)*?\\})", basestr);
        if (!matcher.find()) {
            basestr = basestr.replace("{", "");
            basestr = basestr.replace("}", "");
        }
        return basestr;
    }

    /**
     * 计算latex的最大嵌套深度
     *
     * @return 最大深度
     */
    private int maxNestDeep(String latexString) {
        int deep = 0;
        int max = 1;
        for (int i = 0; i < latexString.length(); i++) {
            char c = latexString.charAt(i);
            if (c == '{') deep++;
            if (c == '}') deep--;
            if (deep > max) max = deep;
        }
        return max;
    }

    /**
     * 表达式
     *
     * @return 去除多余括号之后
     */

    private String makeBrackets(String tempStr) {
        Pattern pattern = Pattern.compile("(\\$\\{)(?=\\\\begin)(.*)(?<=end\\{cases\\})(\\}\\$)");
        Matcher matcher = pattern.matcher(tempStr);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String str = matcher.group();
            stringBuilder.append(tempStr.substring(pos, start));
            stringBuilder.append("$").append(str.substring(2, str.length() - 2)).append("$");
            pos = end;
        }
        stringBuilder.append(tempStr.substring(pos, tempStr.length()));
        return stringBuilder.toString();
    }

//    public static void main(String[] args) {
//        LatexParse latexParse = new LatexParse();
//        String parse = latexParse.parseAll("$\\left\\{ \\begin{array}{l}x = 1 + 3\\cos t,\\\\y =  - 2 + 3\\sin t\\end{array} \\right.$ ($t$为参数)");
//        String parse1 = latexParse.parse("\\frac{1}{a_{1}}+\\frac{1}{a_{2}}+\\cdots+\\frac{1}{a_{n}}<\\frac{3}{2}");
//        String parse2 = latexParse.parse("S_{n} \\leq S_{4}");
//        String parse3 = latexParse.parse("f(x)=\\sin(2x-\\frac{\\Pi}{6})+2(\\cos x)^2-1,x\\in R");
//        System.out.println(parse);
//    }
}



    