package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;

import edu.uestc.transformation.nlp.nlpforehandle.jsontorelationtools.UtilFunction;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpression;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZZH on 2016/9/18.
 */
public class SplitWithTag {
    private static final Logger LOG = LogFactory.getLogger(SplitWithTag.class);

    public static String[] lineRegex = {
//            "垂直平分线",
//            "延长线",
            //xkb201210W
//            "准线",
            "长轴",
            "直线",
            "连线",
//            "平行线",
//            "(一条|)渐近线",

//            "切线",
//            "中垂线",
            "垂线(?!与)",
    };
    public static String[] analogyLineRegex = {
            "弦",
            "中线",
    };

    public static String[] circleRegex = {
            "圆",
    };

    public static String[] sequenceRegex = {
            "等比数列",
            "等差数列",
            "数列",
    };

    public static String[] angleRegex = {
            "角",
    };

    public static String[] algebra = {
            "解",
    };

    public static String[] complexRegex = {
            "共轭复数",
    };


    public static String[] pointRegex = {
            "((左|右)|)顶点",
            "中点",
            "点",
            "圆心",
            "原点",
    };

    public static String[] dualVerb = {
            "互为",
    };

    public static String[] dualLine = {
            "两条切线",
    };


    public static String[] verb = {
            "经过",
            "(互相|相互|)垂直",
            "位于",
            "对应",
//            "关于",
            //求抛物线y=x^2+ax+1的顶点所在的象限
//            "所在",
            "平行(?!( |),)",
            "恒过",
            "到",
            "在",
            "过",
            "交(?!于)",

    };

    public static String[] conj = {
            "(分别|)与",
            "和",
    };




    public static String[] ddSentence = {
            "角( [^&\\s]{1,10}&&angle )的平分线",
            "( [^&\\s]{1,10}&&line |)(边上|)(的|)(中线|弦)",
    };

    /**
     * 处理 " AB&&line 边上的中线 DE=2cm&&expr ,"这种情形,变为 "线段 AB&&line 边上的中线 DE&&line, DE=2cm&&expr "
     *
     * @param sorString
     * @return
     */
    private static String dealSentence(String sorString) {

        StringBuilder stringBuilder = new StringBuilder();
//        初中迁移
//        String regex = "(?<=(高(线|)|中线|弦|对角线|直径|半径))( [^&\\s]+&&expr )";
        String regex0 = "(?<=(" + LTools.createRegexOrByStringList(ddSentence) + ") )" + "([^&\\s]+)(?=&&expr )";
        int pos = 0;
        Pattern pattern0 = Pattern.compile(regex0);
        Matcher matcher = pattern0.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, start));
            String check = matcher.group();
            String re = check;
            if (check.contains("=")) {
                String e[] = check.split("=");
                if (e.length > 1) {
                    String segment = e[0];
                    if (segment.matches(RegularExpression.BIG_WORD_CONTAIN + "{2}")) {
                        re = segment + "&&line  ,  " + check;
                    }
                }
            }
            stringBuilder.append(re);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 "直线 y=1+x&&expr 与 y+3&&expr "这种情形,变为 "直线 y=1+x&&expr 与直线 y+3&&expr "
     *
     * @param sorString
     * @return
     */
    private static String addExprType(String sorString) {
        String regexLine = "(直线|射线|曲线)( [^&\\s]+&&expr )(与|和|,)( [^&\\s]+&&expr )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String type = m.group(1);
            String add = m.group(4);
            int start = m.start(4);
            int end = m.end(4);
//            以y=开头的可能是直线
            if (add.startsWith(" y=")) {
                stringBuilder.append(sorString.substring(pos, start));
                stringBuilder.append(type).append(add);
            } else {
                stringBuilder.append(sorString.substring(pos, start)).append(add);
            }
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));

        return stringBuilder.toString();
    }

    /**
     * 处理 "双曲线 C&&line 与 C_1&&line "这种情形,变为 "双曲线 C&&line 与双曲线 C_1&&line "
     *
     * @param sorString
     * @return
     */
    private static String addLineType(String sorString) {
        String regexLine = "(曲线|圆|⊙|椭圆|双曲线|抛物线|区间|直线)( [^&\\s]+&&line )(与)( [^&\\s]+&&line )(?!相交|相切|的交点)";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String type = m.group(1);
            String add = m.group(4);
            int start = m.start(4);
            int end = m.end(4);
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(type).append(add);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));

        return stringBuilder.toString();
    }

    /**
     * 处理 "双曲线 C&&line : afafa&&expr 与 C_1&&line : afafa&&expr "这种情形,变为 "双曲线 C&&line : fafafa&&expr 与双曲线 C_1&&line : afafa&&expr "
     *
     * @param sorString
     * @return
     */
    private static String addLineTypeWithExpr(String sorString) {
        String regexLine = "(曲线|圆|⊙|椭圆|双曲线|抛物线|直线)( [^&\\s]+&&line )(:)( [^&\\s]+&&expr )(与)( [^&\\s]+&&line : [^&\\s]+&&expr )(?!相交|相切|的交点)";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String type = m.group(1);
            String add = m.group(6);
            int start = m.start(6);
            int end = m.end(6);
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(type).append(add);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));

        return stringBuilder.toString();
    }

    /**
     * 处理"函数 y=xabs(x)+px&&expr  , x&&expr ∈ R&&set的最大值是--"这种类型的句子，变为“函数 y=xabs(x)+px&&expr  , x&&expr ∈ R&&set  , y=xabs(x)+px&&expr的最大值是--”
     *
     * @param sorString
     * @return
     */
    private static String addFunctionTypeWithExpr(String sorString) {
        String regexLine = "(函数)( [^&\\s]+&&expr  )(，|,)(  [a-z]+&&expr )(∈)( [^&\\s]+&&set )(是|为|的最大值是|的最大值为)";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String add = m.group(2);
            int start = m.end(6);
            int end = m.end(7);
            stringBuilder.append(sorString.substring(pos, start)).append(" , ");
            stringBuilder.append(add).append(m.group(7));
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));

        return stringBuilder.toString();
    }

    public static String handleComplexSentence(String sorString) {
        String tempString = sorString;
        try {
            tempString = addLineTypeWithExpr(tempString);
            tempString = addExprType(tempString);
            tempString = addLineType(tempString);
            tempString = splitAlgebra(tempString);
            tempString = splitLine(tempString);
            tempString = splitDualLine(tempString);
            tempString = splitLineDraw(tempString);
            tempString = splitPoint(tempString);
            tempString = splitComplexNum(tempString);
            tempString = lineAnalysisType2(tempString);
            tempString = lineAnalysisType3(tempString);
//            tempString = pointAnalysisType1(tempString);
            tempString = sequenceAnalysis(tempString);
            tempString = funcTranslation(tempString);
//            tempString = circleAnalysisType1(tempString);
            tempString = complexNumAnalysisType1(tempString);
            tempString = dealSentence(tempString);
            tempString = splitAngleSide(tempString);
            tempString = transformThreePointToTriangle(tempString);
            tempString = transformThreePointToTriangle2(tempString);
            tempString = transformFourPointToQuadrangle(tempString);
            tempString = amendAngleTag(tempString);
            tempString = fixAngleFillInTag(tempString);
            tempString = fixAxisTag(tempString);
//            tempString = fixAngleTag(tempString);
//            tempString = fixExprTag(tempString);
//            tempString = fixNumberFillInTag(tempString);
            tempString = fillExprWithTag(tempString);
            tempString = ExprWithTag(tempString);
//            tempString = compareTwoExprTag(tempString);
            tempString = addFunctionTypeWithExpr(tempString);
            tempString = transformTriangleToThreePoint(tempString);
            tempString = positiveNegativeExprTag(tempString);
            tempString = parabolaTranslateInfos(tempString);
            tempString = solveFunctionTag(tempString);
            tempString = twoExprWithTag(tempString);
            tempString = numOmitTag(tempString);
            tempString = fixSetWithTag(tempString);
            tempString = fixSetWithTag1(tempString);
            tempString = fixSetWithTag2(tempString);
        } catch (Exception e) {
             tempString = sorString;
            LOG.warn(e.getMessage());
        }
        return tempString;
    }

    /**
     * 处理 x=±3&&expr 情况 ：这种情形,变为 " x=3&&expr 或 x=-3&&expr "
     *
     * @param sorString
     * @return
     */
    private static String positiveNegativeExprTag(String sorString) {
        String regexLine = "(?<hand>[^&\\s]+=)(±)(?<finish>[^&\\s]+&&expr )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String hand = m.group("hand");
            String finish = m.group("finish");
            int start = m.end(1);
            int end = m.start(3);
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(finish);
            stringBuilder.append(" 或 ");
            stringBuilder.append(hand).append("-");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 比较大小标签打错 ：" 3__-2&&expr "这种情形,变为 " 3&&expr __ -2&&expr "
     *
     * @param sorString
     * @return
     */
    private static String compareTwoExprTag(String sorString) {
        String regexLine = "( [^&\\s]+)(__)([^&\\s]+&&expr )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.end(1);
            int end = m.start(3);
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append("&&expr ");
            stringBuilder.append(m.group(2) + " ");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    private static String fixNumberFillInTag(String srcString) {
        String regexLine = " __([^&\\s]+&&expr )"; //感觉不太完善，要注意
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(srcString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start();
            String str = m.group(1);
            stringBuilder.append(srcString.substring(pos, start));
            if (str.equals("cm&&expr ") || str.equals("°&&expr ")) {
                stringBuilder.append("__ ");
            } else {
                stringBuilder.append("__ ").append(str); //其实就是交换一下 空格 与 __ 的位置
            }
            int end = m.end();
            pos = end;
        }
        stringBuilder.append(srcString.substring(pos, srcString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 表达式含“填空标志”标签打错 ：" x=__&&expr ,x__&&expr"这种情形,变为 "x&&expr =__ "
     *
     * @param sorString
     * @return
     */
    private static String fillExprWithTag(String sorString) {
        String regexLine = "([^&\\s]+)((=|)_(cm|°|)&&expr )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start(1);
            int end = m.end(2);
            String str = m.group(1);
            if (str.substring(str.length() - 1).equals("=")) {
                str = str.substring(0, str.length() - 1);
            }
            stringBuilder.append(sorString.substring(pos, start)).append(str);
            stringBuilder.append("&&expr  =_");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 表达式含“填空标志”标签打错 ：" x=__&&expr ,x__&&expr"这种情形,变为 "x&&expr =__ "
     *
     * @param sorString
     * @return
     */
    private static String twoExprWithTag(String sorString) {
        String regexLine = "(?<one>[^&\\s]+)(&&expr )(?<fu>=|≠|≤|＜|＞|≥) (?<two>[^&\\s]+&&expr )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String one = m.group("one");
            String two = m.group("two");
            String fu = m.group("fu");
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(one).append(fu).append(two);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理标签打错 ：" k∈&&set Z&&set"这种情形,变为 "k&&expr ∈ Z&&set "
     *
     * @param sorString
     * @return
     */
    private static String fixSetWithTag(String sorString) {
        String regexLine = "(?<one>[a-z])∈&&set  (?<two>[A-Z])&&set ";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String expr = m.group("one");
            String set = m.group("two");
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(" ").append(expr).append("&&expr ").append("∈ ").append(set).append("&&set");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理标签打错 ："{x|(x^2)-2x+1=0}为{1}&&set"这种情形,变为 "k&&expr ∈ Z&&set "
     *
     * @param sorString
     * @return
     */
    private static String fixSetWithTag1(String sorString) {
        String regexLine = "(?<one>[^&\\s]+)(?<word>是|为|等于|属于|和|与)(?<two>[^&\\s]+)&&set ";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String fristSet = m.group("one");
            String secendSet = m.group("two");
            String word = m.group("word");
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(fristSet).append("&&set ").append(word).append(" ").append(secendSet).append("&&set ");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理标签打错 ："A&&point ⊊ B&&point "这种情形,变为 "A&&set ⊊ B&&set"
     *
     * @param sorString
     * @return
     */
    private static String fixSetWithTag2(String sorString) {
        String regexLine = "(?<one>[^&\\s]+)&&(point|line|set|expr) (?<word>⊊|⊆|∪|∩|⊋|∉) (?<two>[^&\\s]+)&&(point|set|line|expr) ";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String fristSet = m.group("one");
            String secendSet = m.group("two");
            String word = m.group("word");
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append(fristSet).append(word).append(secendSet).append("&&set ");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 表达式以等号结尾 ：" x=&&expr ？"这种情形,变为 "x&&expr =__ "
     *
     * @param sorString
     * @return
     */
    private static String ExprWithTag(String sorString) {
        String regexLine = "([^&\\s]+)(=&&expr )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start(1);
            int end = m.end(2);
            String str = m.group(1);
            stringBuilder.append(sorString.substring(pos, start)).append(str);
            stringBuilder.append("&&expr  =_");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 角标签打错 ：" ∠3&&expr "这种情形,变为 " ∠ 3&&angle "
     *
     * @param srcString
     * @return
     */
    private static String fixAngleTag(String srcString) {

        String regexLine = " ∠[1-9a-z]&&expr ";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(srcString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;

        while (m.find()) {
            String angle1 = m.group(0);
            int start = m.start(0);
            int end = m.end(0);
            stringBuilder.append(srcString.substring(pos, start));
            stringBuilder.append(srcString.substring(start, start + 2) + " " + srcString.substring(start + 2, start + 5));
            stringBuilder.append("angle ");
            pos = end;
        }

        stringBuilder.append(srcString.substring(pos, srcString.length()));
        return stringBuilder.toString();

    }


    /**
     * 处理 "(n=3,4,...)"这种情形,变为 "n≥3"
     *
     * @param srcString
     * @return
     */
    private static String numOmitTag(String srcString) {

        String regexLine = "n=(?<num>\\d),\\d,\\.\\.\\.";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(srcString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String num = m.group("num");
            stringBuilder.append(srcString.substring(pos, start));
            stringBuilder.append("n≥").append(num);
            pos = end;
        }
        stringBuilder.append(srcString.substring(pos, srcString.length()));
        return stringBuilder.toString();

    }

    /**
     * 处理 角标签打错 ：" x&&expr 轴 或者 x&&line 轴"这种情形,变为 " x轴 "
     *
     * @param srcString
     * @return
     */
    private static String fixAxisTag(String srcString) {

        String regexAxis = " (?<axis>(x|y|X|Y))(&&expr|&&line) 轴";
        Pattern p = Pattern.compile(regexAxis);
        Matcher m = p.matcher(srcString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;

        while (m.find()) {
            String axis = m.group("axis");
            int start = m.start(0);
            int end = m.end(0);
            stringBuilder.append(srcString.substring(pos, start));
            stringBuilder.append(axis + "轴");
            pos = end;
        }
        stringBuilder.append(srcString.substring(pos, srcString.length()));
        return stringBuilder.toString();

    }

    private static String fixExprTag(String srcString) {
        String regexLine = " (-|)[0-9.]{1,}=&&expr ";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(srcString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;

        while (m.find()) {
            String expr = m.group(0);
            int start = m.start(0);
            int end = m.end(0);
            stringBuilder.append(srcString.substring(pos, start));

            String[] exprSplit = expr.split("=");
            stringBuilder.append(exprSplit[0]);
            stringBuilder.append("&&expr = ");
            pos = end;
        }

        stringBuilder.append(srcString.substring(pos, srcString.length()));
        return stringBuilder.toString();

    }

    /**
     * 处理 角标签打错 ："∠APC=_度&&expr "这种情形,变为 "∠APC&&expr =_度 "
     *
     * @param srcString
     * @return
     */
    private static String fixAngleFillInTag(String srcString) {

        String regexLine = " ∠[A-Z]{3}=__度&&expr ";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(srcString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;

        while (m.find()) {
            String angle1 = m.group(0);
            int start = m.start(0);
            int end = m.end(0);
            stringBuilder.append(srcString.substring(pos, start));
            stringBuilder.append(srcString.substring(start, start + 5));
            stringBuilder.append("&&expr =__度 ");
            pos = end;
        }

        stringBuilder.append(srcString.substring(pos, srcString.length()));
        return stringBuilder.toString();
    }

    /**
     * 处理 角标签打错 ："∠&&expr BCD&&angle "这种情形,变为 "∠ BCD&&angle "
     *
     * @param sorString
     * @return
     */
    private static String amendAngleTag(String sorString) {
        String regexLine = "( ∠&&expr )( [^&\\s]+&&angle )";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            String angle1 = m.group(1);
            int start = m.start(1);
            int end = m.end(1);
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append("∠");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * @param sorString 该函数将四个点组的四边形转换为一个四边形
     *                  例如 以点A、B、C、D为顶点的四边形 ----->四边形ABCD
     */
    public static String transformFourPointToQuadrangle(String sorString) {
        StringBuilder stringBuilder = new StringBuilder();
        String regex = "(以|使得|从)((格|)点|) (?<point1>[^\\s&]+)&&point ( , |,)(点|) (?<point2>[^\\s&]+)&&point ( , |,)(点|) (?<point3>[^\\s&]+)&&point ( , |,)(点|) (?<point4>[^\\s&]+)&&point (四(个|)点|)(为顶点|围成)的(?<quadrangleType>(四边形))";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.start();
            String addString = matcher.group();
            stringBuilder.append(sorString.substring(pos, end));
            String point1 = matcher.group("point1");
            String point2 = matcher.group("point2");
            String point3 = matcher.group("point3");
            String point4 = matcher.group("point4");
            String quadrangleType = matcher.group("quadrangleType");
            if (point1.matches(RegularExpression.BIG_WORD_CONTAIN) && point2.matches(RegularExpression.BIG_WORD_CONTAIN) && point3.matches(RegularExpression.BIG_WORD_CONTAIN) && point4.matches(RegularExpression.BIG_WORD_CONTAIN)) {
                addString = quadrangleType + " " + point1 + point2 + point3 + point4 + "&&quadrangle ";
            }
            stringBuilder.append(addString);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * @param sorString 该函数将三个点组成三角形的三点转换为一个三角形
     *                  例如 以点A、B、C为顶点的三角形 ----->三角形ABC
     */
    public static String transformThreePointToTriangle(String sorString) {

        StringBuilder stringBuilder = new StringBuilder();
        String regex = "(以|使得|)((格|)点|) (?<point1>[^\\s&]+)&&point ( , |,)(点|) (?<point2>[^\\s&]+)&&point ( , |,)(点|) (?<point3>[^\\s&]+)&&point (三(个|)点|)(为(顶点|頂点)|组成|构成)的(?<triangleType>(等边|直角|等腰|)三角形)";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.start();
            String addString = matcher.group();
            stringBuilder.append(sorString.substring(pos, end));
            String point1 = matcher.group("point1");
            String point2 = matcher.group("point2");
            String point3 = matcher.group("point3");
            String triangleType = matcher.group("triangleType");
            if (point1.matches(RegularExpression.BIG_WORD_CONTAIN) && point2.matches(RegularExpression.BIG_WORD_CONTAIN) && point3.matches(RegularExpression.BIG_WORD_CONTAIN)) {
                addString = triangleType + " " + point1 + point2 + point3 + "&&triangle ";
            }
            stringBuilder.append(addString);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * @param sorString 该函数将一个三角形转换成三个点
     *                  例如 三角形ABC三个顶点 ----->三角形ABC的三个顶点A,B,C
     */
    public static String transformTriangleToThreePoint(String sorString) {
        StringBuilder stringBuilder = new StringBuilder();
        String regex = "(?<!&&point (分别|)(是|为))(?<triangle>(等边|直角|等腰|Rt|)(三角形|△)) (?<point1>[A-Z](_\\d+|))(?<point2>[A-Z](_\\d+|))(?<point3>[A-Z](_\\d+|))(&&triangle) ((的|)三(个|)顶点)(?!(分别|)(是|为|\\s[^&\\s]+&&point))";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.start();
            String triangle = matcher.group("triangle");
            String point1 = matcher.group("point1");
            String point2 = matcher.group("point2");
            String point3 = matcher.group("point3");
            stringBuilder.append(triangle).append(" ").append(point1).append(point2).append(point3).append("&&triangle  ,  ");
            stringBuilder.append(sorString.substring(pos, end));
            if (point1.matches(RegularExpression.BIG_WORD_CONTAIN) && point2.matches(RegularExpression.BIG_WORD_CONTAIN) && point3.matches(RegularExpression.BIG_WORD_CONTAIN)) {
                stringBuilder.append("点 " + point1 + "&&point , " + point2 + "&&point , " + point3 + "&&point ");
            }
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * @param sorString 该函数将三个点组成三角形的三点转换为一个三角形
     *                  例如 A、B、C为一个三角形的三个顶点 ----->三角形ABC
     */
    public static String transformThreePointToTriangle2(String sorString) {

        StringBuilder stringBuilder = new StringBuilder();
        String regex = "(?<point1>[^\\s&]+)&&point ( , |,)(点|) (?<point2>[^\\s&]+)&&point ( , |,)(点|) (?<point3>[^\\s&]+)&&point (是|为)(一个|)(?<triangleType>(等边|直角|等腰|)三角形)的(三(个|)|)(顶点)";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.start();
            String addString = matcher.group();
            stringBuilder.append(sorString.substring(pos, end));
            String point1 = matcher.group("point1");
            String point2 = matcher.group("point2");
            String point3 = matcher.group("point3");
            String triangleType = matcher.group("triangleType");
            if (point1.matches(RegularExpression.BIG_WORD_CONTAIN) && point2.matches(RegularExpression.BIG_WORD_CONTAIN) && point3.matches(RegularExpression.BIG_WORD_CONTAIN)) {
                addString = triangleType + " " + point1 + point2 + point3 + "&&triangle ";
            }
            stringBuilder.append(addString);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    // ∠ EAF&&angle 的两边分别与射线 CB&&line  , DC&&line 相交于点 E&&point  , F&&point
    public static String splitAngleSide(String sorString) {
        StringBuilder stringBuilder = new StringBuilder();
        String regex = "(角|∠|底角|顶角) " + RegularExpression.BIG_WORD_CONTAIN + "{3}&&angle " + "(的|)两(条|个|)边" + "(?!( [^&\\s]+&&line))";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.start();
            String angle = matcher.group();
            String addString = angle;
            stringBuilder.append(sorString.substring(pos, end));
            String line1Regex = "(?<=((角|∠|底角|顶角) ))" + RegularExpression.BIG_WORD_CONTAIN + "{2}";
            String line2Regex = RegularExpression.BIG_WORD_CONTAIN + "{2}(?=&&angle ((的|)两(条|个|)边))";
            Matcher lineMatcher1 = RegularExpressionTools.getRegexMatcher(line1Regex, angle);
            Matcher lineMatcher2 = RegularExpressionTools.getRegexMatcher(line2Regex, angle);
            String line1 = "", line2 = "";
            if (lineMatcher1.find()) {
                line1 = lineMatcher1.group();
            }
            if (lineMatcher2.find()) {
                line2 = lineMatcher2.group();
            }
            if (StringUtils.isNotEmpty(line1) && StringUtils.isNotEmpty(line2)) {
                addString = " " + line1 + "&&line 和 " + line2 + "&&line ";
            }
            stringBuilder.append(addString);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String funcTranslation(String sorString) {

//        String regex = "(?<=单位)(长度|)(后|)((\\s|),(\\s|))((所得((到|)(的|))图象|得到|(则|)平移后(的|)图象)|所对应的函数)";
        String regex = "(?<=( [^&\\s]{1,10}&&expr (倍)|单位))(长度|)(后|)((\\s|),(\\s|))(((所|)得((到|)(的|))(图象|函数)|(则|)平移后(的|)图象)|所对应的函数)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sorString);
        String replaceString = "得到函数 f_1(x)&&expr  , 函数 y=f_1(x)&&expr ";
        if (m.find()) {
            int po = m.end();
            int en = sorString.indexOf(" , ", po);
            if (en < po) {
                en = sorString.length();
            }
            if (en > po) {
                String subStrings = sorString.substring(po, en);
                Matcher m1 = RegularExpressionTools.getRegexMatcher("(?<!((对称轴)(是|为)|直线)) [^&\\s]+&&expr", subStrings);
                if (m1.find()) {
                    return sorString;
                }
            }
            sorString = sorString.replace(m.group(), replaceString);
        }
        return sorString;
    }

    //试用题型qg200102w ,但是与BX2516冲突,
    public static String circleAnalysisType1(String conjTemp) {
        String lines = LTools.createRegexOrByStringList(circleRegex);
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(?<=(在))[^在]+?的" + lines + "(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(conjTemp.substring(pos, end));

            String randomVar = "R_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String addString = " " + randomVar + "&&circle  , 圆" + " R_" + RandomVarsSave.count++ + "&&circle ";
            stringBuilder.append(addString);
            pos = end;
        }
        stringBuilder.append(conjTemp.substring(pos, conjTemp.length()));
        result = stringBuilder.toString();
        return result;
    }

    //过F作直线交。。。。
    private static String splitLineDraw(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String lines = LTools.createRegexOrByStringList(lineRegex);
        String verbs = LTools.createRegexOrByStringList(UtilFunction.addTwoArray(verb, conj));
        String regex = "作" + lines + "(?=" + verbs + ")";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, end));

            String randomVar = "l_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String b = " " + randomVar + "&&line  ," + "  l_" + RandomVarsSave.count++ + "&&line ";
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    /**
     * 抛物线平移信息处理 （Q_20210504396831）
     *
     * @param sorString
     * @return
     */
    private static String parabolaTranslateInfos(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String regex = "(抛物线 [^&\\s]+&&expr )(向下平移后)((经|)过|与|和)";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end(2);
            stringBuilder.append(sorString.substring(pos, end));
            String b = "抛物线" + " C_" + RandomVarsSave.count++ + "&&line ";
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    //的中垂线交AB与D
    private static String splitLine(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String lines = LTools.createRegexOrByStringList(lineRegex);
        String verbs = LTools.createRegexOrByStringList(UtilFunction.addTwoArray(verb, conj));
        String regex = "的" + lines + "(?=" + verbs + ")";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, end));

            String randomVar = "l_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String b = "为" + " " + randomVar + "&&line  ," + "  l_" + RandomVarsSave.count++ + "&&line ";
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    //的中垂线交AB与D
    private static String splitDualLine(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String lines = LTools.createRegexOrByStringList(dualLine);
        String verbs = LTools.createRegexOrByStringList(verb);
        String regex = "的" + lines + "(?=" + verbs + ")";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, end));

            String randomVar1 = "l_" + RandomVarsSave.count;

            int i = RandomVarsSave.count + 1;
            String randomVar2 = "l_" + i;
            RandomVarsSave.addToSave(randomVar1);
            RandomVarsSave.addToSave(randomVar2);

            String b = "为" + " " + randomVar1 + "&&line  ,  " + randomVar2 + "&&line  ,  " + randomVar1 + "&&line 和 " + randomVar2 + "&&line ";
            RandomVarsSave.count = RandomVarsSave.count + 2;
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    //...解互为相反数
    private static String splitAlgebra(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String lines = LTools.createRegexOrByStringList(algebra);
        String verbs = LTools.createRegexOrByStringList(dualVerb);
        String regex = "的" + lines + "(?=" + verbs + ")";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, end));
            int c = RandomVarsSave.count + 1;

            String randomVar1 = "v_" + RandomVarsSave.count;
            String randomVar2 = "v_" + c;
            RandomVarsSave.addToSave(randomVar1);
            RandomVarsSave.addToSave(randomVar2);

            String b = "为" + " v_" + RandomVarsSave.count + "&&expr  ,  v_" + c + "&&expr " + " ,  v_" + RandomVarsSave.count + "&&expr  ,  v_" + c + "&&expr ";
            RandomVarsSave.count += 2;
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    //AB的中点交EF于D
    private static String splitPoint(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String lines = LTools.createRegexOrByStringList(pointRegex);
        String verbs = LTools.createRegexOrByStringList(UtilFunction.addTwoArray(verb, conj));
        String regex = "的" + lines + "(?=" + verbs + ")";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, end));

            String randomVar = "P_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String b = "为" + " P_" + RandomVarsSave.count + "&&point  ,  " + "点 P_" + RandomVarsSave.count++ + "&&point ";
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    //的共轭复数经过.....
    private static String splitComplexNum(String sorString) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String complexNum = LTools.createRegexOrByStringList(complexRegex);
        String verbs = LTools.createRegexOrByStringList(UtilFunction.addTwoArray(verb, conj));
        String regex = "的" + complexNum + "(?=" + verbs + ")";
        int pos = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(sorString.substring(pos, end));

            String randomVar = "c_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String b = "为" + " c_" + RandomVarsSave.count + "&&expr  ,  " + "复数 c_" + RandomVarsSave.count++ + "&&expr ";
            stringBuilder.append(b);
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    //Q到椭圆C的焦点的距离为3
    public static String pointAnalysisType1(String conjTemp) {
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(?<=(和|关于|与|到))[^和到与关于]+?的(((左|右)|)顶点|((左|右)|)焦点|圆心|点)(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        if (matcher.find()) {

            String randomVar = "P_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String tempLine = "点 P_" + RandomVarsSave.count++ + "&&point ";
            int i = matcher.start();
            int j = matcher.end();
            stringBuilder.append(conjTemp.substring(pos, i));
            stringBuilder.append(tempLine);
            stringBuilder.append(conjTemp.substring(j, conjTemp.length()));
            String startString = matcher.group() + "为" + tempLine + ", ";
            result = startString + stringBuilder.toString();
            return result;
        }
        return conjTemp;
    }

    //ax+i的共轭复数的最大值为
    public static String complexNumAnalysisType1(String conjTemp) {
        String complexNum = LTools.createRegexOrByStringList(complexRegex);
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(的)" + complexNum + "(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        while (matcher.find()) {
            int end = matcher.end();

            String randomVar = "c_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            stringBuilder.append(conjTemp.substring(pos, end));
            String addString = "为 c_" + RandomVarsSave.count + "&&expr  , " + "复数 c_" + RandomVarsSave.count++ + "&&expr ";
            stringBuilder.append(addString);
            pos = end;
        }
        stringBuilder.append(conjTemp.substring(pos, conjTemp.length()));
        result = stringBuilder.toString();
        return result;
    }

    //经过椭圆C的直线的中垂线为AB
    public static String lineAnalysisType2(String temp) {
        String lines = LTools.createRegexOrByStringList(lineRegex);
        StringBuilder sorSentence = new StringBuilder();
        String result;
        int pos = 0;
        Pattern pattern = Pattern.compile("(经过|过)[^过交在,]+?的" + lines + "(?=的)");
        Matcher m = pattern.matcher(temp);
        if (m.find()) {

            String randomVar = "l_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String tempLine = " l_" + RandomVarsSave.count++ + "&&line ";
            int i = m.start();
            int j = m.end();
            sorSentence.append(temp.substring(pos, i));
            sorSentence.append(tempLine);
            sorSentence.append(temp.substring(j, temp.length()));
            String startString = m.group() + "为" + tempLine + " , ";
            result = startString + sorSentence.toString();
            return result;
        }
        return temp;
    }

    public static String sequenceAnalysis(String temp) {
        String lines = LTools.createRegexOrByStringList(sequenceRegex);
        StringBuilder sorSentence = new StringBuilder();
        String result;
        int pos = 0;
        Pattern pattern = Pattern.compile("(?<=的)" + lines + "(?=的)");
        Matcher m = pattern.matcher(temp);
        if (m.find()) {
            String tempLine = " {K_n}&&sequ  ,  {K_n}&&sequ ";
            int j = m.end();
            sorSentence.append(temp.substring(pos, j));
            sorSentence.append(tempLine);
            sorSentence.append(temp.substring(j, temp.length()));
            result = sorSentence.toString();
            return result;
        }
        return temp;
    }


    // (在|以)。。。的(切线|直线|准线|线段)的。。。
    // (在|以)点A的(切线|直线|准线|线段)的斜率。。。
    public static String lineAnalysisType3(String conjTemp) {
        String lines = LTools.createRegexOrByStringList(lineRegex);
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(?<=(在|以))[^以在,]+?的" + lines + "(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(conjTemp.substring(pos, end));

            String randomVar = "l_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String addString = "为 l_" + RandomVarsSave.count + "&&line  , " + " l_" + RandomVarsSave.count++ + "&&line ";
            stringBuilder.append(addString);
            pos = end;
        }
        stringBuilder.append(conjTemp.substring(pos, conjTemp.length()));
        result = stringBuilder.toString();
        return result;
    }

    /**
     * 处理 用"并"字隔开的长句子没有逗号的情况
     *
     * @param sorString
     * @return
     */
    private static String solveFunctionTag(String sorString) {
        String regexLine = "(&&expr )(并|求)";
        Pattern p = Pattern.compile(regexLine);
        Matcher m = p.matcher(sorString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (m.find()) {
            int start = m.start(1);
            int end = m.end(1);
            stringBuilder.append(sorString.substring(pos, start));
            stringBuilder.append("&&expr ");
            stringBuilder.append(" , ");
            pos = end;
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


}
