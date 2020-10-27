package edu.uestc.transformation.nlp.nlpforehandle.jsontorelationtools;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpression;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;
import edu.uestc.transformation.nlp.nlpforehandle.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 转关系时用到的一些公共函数
 */
public class UtilFunction {
    private static final Logger LOG = LogFactory.getLogger(UtilFunction.class);

    /**
     * 获取去掉括号的条件
     * 输入：y=10x+100()；得到expr: y=10x+100; 返回空集合
     * 输入：y=10x+100(0≤t≤20)；得到expr: y=10x+100; 返回{0≤t≤20}
     *
     * @param expr 原表达式，通过该方法会去掉后面的条件
     * @return 去掉括的条件集合，如果没有则返回空字符串
     */
   /* public static List<String> getConditionListWithoutBracket(StringBuffer expr) {
        List<String> conditionList = Lists.newArrayList();
        String condition = findConditionString(expr);
        if (StringUtils.isEmpty(condition)) {
            if (expr.toString().endsWith("()")) {
                expr.delete(expr.length() - 2, expr.length());
            }
        } else {
            if (condition.startsWith("(") && condition.endsWith(")")) {
                condition = condition.substring(1, condition.length() - 1);
                condition = condition.replaceAll("[a-z]=\\d,\\d", "");//删去条件k=1,2
                if (condition.contains(",")) {
//                    for(String s : Splitter.on(",").omitEmptyStrings().splitToList(condition)){
//                        if(s.matches("\\d+") && conditionList.size() != 0 ){
//                            s = conditionList.get(conditionList.size()-1).replaceAll("(?<==)\\d+", s);
//                        }
//                        conditionList.add(s);
//                    }
                    String test = NewAnalysisQuestionsStems.getNERChunkStrWithoutOAndBlank(MergeTag.Merge(condition));
                    String[] t = test.split(" , ");
                    conditionList = Lists.newArrayList(Collections2.transform(Lists.newArrayList(t), new Function<String, String>() {
                        @Override
                        public String apply(String input) {
                            return deleteTags(input);
                        }
                    }));
                } else {
                    conditionList.add(condition);
                }
            }
        }
        return conditionList;
    }

    /**
     * 输入：y=10x+100(0≤t≤20)
     * 输出：(0≤t≤20)
     * 找到表达式中的条件
     *
     * @param expr 原始串
     * @return 含有括号的条件，如果没找到返回null
     */
    public static String findConditionString(StringBuffer expr) {
        if (expr.toString().contains("#")) {
            return "";
        }
        if (expr.lastIndexOf(")") == expr.length() - 1) {
            int fb = findFrontBracket(expr.toString());
            if (fb > 0) {
                //括号的前一个字符，如果前一个字符是(+|-|*|/)中的一个的话，则不是条件
                int frontChar = fb - 1;
                String c = expr.charAt(frontChar) + "";
                String condString = expr.substring(fb);
                if ((isContainOperator(condString) || isSpecialNum(expr.toString())) && !c.matches("\\+|\\-|\\*|/")) {
                    String local = expr.toString().substring(0, fb);
                    expr.delete(0, expr.length());
                    expr.append(local);
                    return condString;
                }
            }
        }
        return "";
    }

    public static String findExprWithoutCondition(String expr) {
        String conditionString = findConditionString(new StringBuffer(expr));
        if (conditionString != null && conditionString.startsWith("(") && conditionString.endsWith(")")) {
            expr = expr.substring(0, expr.length() - conditionString.length());
        }
        return expr;
    }

    public static String[] splitConditionFromExpr(String expr) {
        String condition = findConditionString(new StringBuffer(expr));
        if (StringUtils.isEmpty(condition)) {
            if (expr.endsWith("()")) {
                expr = expr.substring(0, expr.length() - 2);
            }
            return new String[]{expr};
        } else {
            expr = expr.substring(0, expr.length() - condition.length());
            if (condition.startsWith("(") && condition.endsWith(")")) {
                condition = condition.substring(1, condition.length() - 1);
            }
            return new String[]{expr, condition};
        }
    }

    /**
     * 把a=3或-1变为a=3或a=-1的形式
     */
    public static String dealOrNum(String orString) {
//        orString = orString.replace("$m=1$","m=1");
        String regex = "([a-z])(=)((\\-|)\\d{1,3})(\\$?或)((\\-|)\\d{1,3})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(orString);
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(orString.substring(pos, start));
            int end = matcher.end();

            String b = matcher.group(1);
            String c = matcher.group(2);
            String d = matcher.group(3);
            String f = matcher.group(5);
            String g = matcher.group(6);
            String out = b + c + d + f + b + c + g;
            stringBuilder.append(out);
            pos = end;

        }
        stringBuilder.append(orString.substring(pos, orString.length()));
        return stringBuilder.toString();
    }

    /**
     * 统计特定字符出现的次数
     * @param str
     * @param src
     * @return
     */
    public static int findChar(String str, char src) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == src) {
                count++;
            }
        }
        return count;
    }

    /**
     * 删除字符串两边的配对的括号
     * @param string
     * @return
     */
    public static String deleteBracket(String string) {
        if (string.startsWith("(") && string.endsWith(")") && findFrontBracket(string) == 0) {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }
    public static boolean isInEquality(String expr) {

        String tempString = findExprWithoutCondition(expr);
        if (StringUtils.isNotEmpty(tempString)) {
            Matcher matcher = RegularExpressionTools.getRegexMatcher("(≥|>|＜|≤|<|≠)", tempString);
            return matcher.find();
        }
        return false;
    }

    /**
     * 当字符串的最后一位是后括号的时候，找出对应的前括号的位置
     * 返回-1表示表达式本身括号就不配对
     * @param expr
     * @return
     */
    public static int findFrontBracket(String expr) {
        int level = 1;
        for (int i = expr.length() - 2; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')' || c == ']') {
                level++;
            } else if (c == '(' || c == '[') {
                level--;
            }
            if (level == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param taggedText
     * @return 对于打了标签的句子，去除标签，和分词空格
     */
    public static String deleteTags(String taggedText) {
        String regex = "((&&[^\\s&{2}]+?\\s)|(&&[^\\s&{2}]+?$))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(taggedText);
        while (matcher.find()) {
            taggedText = taggedText.replace(matcher.group(), "");
        }
        return taggedText.replace(" ", "");
    }




    //获得点指代的key值
    public static String getPointKey(String pointKey) {
        if (StringUtils.isEmpty(pointKey)) {
            return pointKey;
        }
        if (pointKey.lastIndexOf(")") == pointKey.length() - 1) {
            int index = findFrontBracket(pointKey);
            if (index != 0) {
                return pointKey.substring(0, index);
            }
        }
        return pointKey;
    }

    //判断是否是(是|为)(正|负|)(整|实|常|有理|)数
    private static boolean isSpecialNum(String string) {
        Matcher matcher = RegularExpressionTools.getRegexMatcher(RegularExpression.Special_Number, string);
        return matcher.find();
    }

    //是否包含运算符号
    public static boolean isContainOperator(String s) {
        Matcher matcher = RegularExpressionTools.getRegexMatcher(RegularExpression.OPERATOR, s);
        Matcher matcher1 = RegularExpressionTools.getRegexMatcher("∈", s);
        return matcher.find() || matcher1.find();
    }



    /**
     * 找到所有的操作符然后返回
     *
     * @return List<String>
     */
/*    public static List<String> findAllOperator(String expr) {
        List<String> list = Lists.newArrayList();
        Matcher matcher = Pattern.compile(OperatorUtil.RelationalOperatorsRegex).matcher(expr);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static String findOperator(String expr) {
        Matcher matcher = Pattern.compile(OperatorUtil.RelationalOperatorsRegex).matcher(expr);
        return matcher.find() ? matcher.group() : "";
    }

    public static Set<String> findAllOperatorWithoutRepeat(String expr) {
        Set<String> set = Sets.newHashSet();
        Matcher matcher = Pattern.compile(OperatorUtil.RelationalOperatorsRegex).matcher(expr);
        while (matcher.find()) {
            set.add(matcher.group());
        }
        return set;
    }*/


    //去除%*%类型的不合法数据在不能确定变量数量的时候使用
    public static List<String> getValidData(String[] str) {
        List<String> list = Lists.newArrayList();
        for (String data : str) {
            if (!data.contains("%")) {
                list.add(data);
            }
        }
        return list;

    }

    /**
     * 将两个字符串数组放入一个字符串数组中
     * 如:arr1 ={"123","567"};arr1={"abc","def"} ;
     * addTwoAraay(arr1,arr2) = {"123","567","abc","def"};
     *
     * @param arr1 需融合的字符串数组一
     * @param arr2 需融合的字符串数组二
     * @return 融合了两个字符串数组的新数组
     */
    public static String[] addTwoArray(String[] arr1, String[] arr2) {
        String[] c = new String[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, c, 0, arr1.length);
        System.arraycopy(arr2, 0, c, arr1.length, arr2.length);
        return c;
    }

    //一个element只有一个实体的时候使用
    public static String getStringDataFromJsonObject(JsonObject root, String element) {
        String res = "";
        if (root.has(element)) {
            JsonElement jsonElement = root.get(element);
            try {
                res = jsonElement.getAsString();
            } catch (UnsupportedOperationException e) {
                LOG.info("getStringDataFromJsonObject方法中,getAsString()异常");
                LOG.info(jsonElement.toString());
            }
        }
        return res.matches("%(.*)%") ? "" : res;
    }



    /**
     * 点的坐标会和逗号分隔符混淆！！
     */
    public static List<String> getListStringFromJson(JsonObject jsonObject, String element, String splitChar) {
        String result = getStringDataFromJsonObject(jsonObject, element);
        if (StringUtils.isEmpty(result))
            return new ArrayList<>();
        return new ArrayList<>(Collections2.filter(Splitter.onPattern(splitChar).trimResults().splitToList(result), new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return !s.matches("%.*?%");
            }
        }));
    }

    public static List<String> getPointListStringFromJson(JsonObject jsonObject) {
        String result = getStringDataFromJsonObject(jsonObject, Constant.POINT);
        if (StringUtils.isEmpty(result)) {
            return Lists.newArrayList();
        }
//        List<String> pointList = Lists.newArrayList(result.split(";"));

//        Pattern pattern = Pattern.compile(RegularExpression.ABS_POINT_REGEX_1);
//        Matcher matcher = pattern.matcher(result);
//        while (matcher.find()) {
//            pointList.add(matcher.group());
//        }
        return Lists.newArrayList(result.replace("%point[1]%", "").split(";"));
//        if (splitChar.length() > 1) {
//            String[] array = result.split(splitChar);
//            return Arrays.asList(array);
//        } else {
//            return StringCollectionUtils.split(result, splitChar);
//        }
    }

    /**
     * json数据为Array形式
     *
     * @param jsonObject 源jsonObject
     * @param element    json字段名
     * @return 该json中对应的字符串集合
     */
    public static List<String> getListStringFromJson(JsonObject jsonObject, String element) {
        if (!jsonObject.has(element)) {
            return Lists.newArrayList();
        }

        List<String> strList = Lists.newArrayList();
        try {
            JsonArray jsonArray = jsonObject.get(element).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                strList.add(jsonElement.getAsString());
            }
        } catch (IllegalStateException e) {
            LOG.warn("ignored: is not JsonArray ", e);
            strList.add(getStringDataFromJsonObject(jsonObject, element));
        }
        return strList;
    }



    public static JsonArray getJsonArrayDataFromJson(JsonObject root, String element) {
        return root.has(element) ? root.getAsJsonArray(element) : null;
    }

    public static JsonObject getJsonObjectDataFromJson(JsonObject root, String element) {
        return root.has(element) ? root.getAsJsonObject(element) : null;
    }

    /**
     * 从json中找出圆锥曲线三要素，名字，表达式，类型
     */
    public static Map<String, String> getConicCurveMapFromJsonObj(JsonObject root, String element) {
        Map<String, String> conicCurveMap = Maps.newHashMap();
        if (root.has(element)) {
//            JsonObject conicCurve = UtilFunction.getJsonObjectDataFromJson(root, Constant.CONIC_CURVE);
            JsonObject conicCurve = UtilFunction.getJsonObjectDataFromJson(root, element);
            String conic = UtilFunction.getStringDataFromJsonObject(conicCurve, Constant.CURVE);
            conicCurveMap.put(Constant.CURVE, conic.equals("%line%") ? "" : conic);
            String expr = UtilFunction.getStringDataFromJsonObject(conicCurve, Constant.EXPR);
            conicCurveMap.put(Constant.EXPR, expr.equals("%expr%") ? "" : expr);
            conicCurveMap.put(Constant.TYPE, UtilFunction.getStringDataFromJsonObject(conicCurve, Constant.TYPE));
        }
        return conicCurveMap;
    }

    /**
     * 从json中找出数列二要素：名字，类型
     */
    public static Map<String, String> getSequenceMapFromJsonObj(JsonObject root, String element) {
        Map<String, String> sequenceDataMap = Maps.newHashMap();
        if (root.has(element)) {
            JsonObject sequenceData = UtilFunction.getJsonObjectDataFromJson(root, Constant.SEQUENCE_DATA);
            String sequence = UtilFunction.getStringDataFromJsonObject(sequenceData, Constant.SEQUENCE);
            sequenceDataMap.put(Constant.SEQUENCE, sequence.equals("%sequence%") ? "" : sequence);
            String type = UtilFunction.getStringDataFromJsonObject(sequenceData, Constant.TYPE);
            sequenceDataMap.put(Constant.TYPE, (type.equals("NULL") ? "" : type));
        }
        return sequenceDataMap;
    }

    /**
     * 从json中找出直线二要素，名字，表达式
     */
    /*
     * pm于2016.7.11将UtilFunction.getJsonObjectDataFromJson(root, Constant.STRAIGHTLINE);
     * 修改为以下(root, element),TJ201305W
     */
    public static Map<String, String> getStraightLineMapFromJsonObj(JsonObject root, String element) {
        Map<String, String> straightLineMap = Maps.newHashMap();
        if (root.has(element)) {
            JsonObject straightLine = UtilFunction.getJsonObjectDataFromJson(root, element);
            String line = UtilFunction.getStringDataFromJsonObject(straightLine, Constant.LINE);
            String lineExpr = UtilFunction.getStringDataFromJsonObject(straightLine, Constant.EXPR);
// why？
//  lineExpr = line.equals(lineExpr) ? "" : lineExpr;
            straightLineMap.put(Constant.LINE, line.equals("%line%") ? "" : line);
            straightLineMap.put(Constant.EXPR, lineExpr);
        }
        return straightLineMap;
    }

    /**
     * 从json中找出圆二要素，名字，表达式
     */
    public static Map<String, String> getCircleMapFromJsonObj(JsonObject root, String element) {
        Map<String, String> circleMap = Maps.newHashMap();
        if (root.has(element)) {
            JsonObject circleObject = UtilFunction.getJsonObjectDataFromJson(root, element);
            String circle = UtilFunction.getStringDataFromJsonObject(circleObject, Constant.CIRCLE);
            String circleExpr = UtilFunction.getStringDataFromJsonObject(circleObject, Constant.EXPR);
// why？
//  lineExpr = line.equals(lineExpr) ? "" : lineExpr;
            circleMap.put(Constant.CIRCLE, circle.equals("%circle%") ? "" : circle);
            circleMap.put(Constant.EXPR, circleExpr);
        }
        return circleMap;
    }

    public static int transString(String temp) {
        if (temp.matches("(\\-|)\\d")) {
            return Integer.valueOf(temp);
        }
        if (temp.equals("infinite"))
            return Integer.MAX_VALUE;
        String[] a = {"零", "一", "二|两|两个", "三", "四", "五", "六", "七", "八", "九", "十"};
        int[] b = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int i = 0; i < a.length; i++) {
            if (a[i].contains(temp)) {
                return b[i];
            }
        }
        return 0;
    }





    public static String transChineseNum(String data) {
        String[] chineseNums = {"零", "一", "二两", "三", "四", "五", "六", "七", "八", "九", "十"};
        for (int i = 0; i < chineseNums.length; i++) {
            if (chineseNums[i].contains(data))
                return String.valueOf(i);
        }
        return data;
    }


    public static List<String> stringToListString(String str) {

        char[] strChars = str.toCharArray();
        List<String> stringList = new ArrayList<>();
        //转换为数组
        for (char ch : strChars) {
            stringList.add(String.valueOf(ch));
        }

        return stringList;
    }


    /**
     * 在直线AB上截取AP  -->	P
     */
    public static String getPoint(String lineString, String segmentString) {
        List<String> points = new ArrayList<>();
        Matcher matcher = Pattern.compile(RegularExpression.BIG_WORD_CONTAIN).matcher(lineString);
        while (matcher.find()) {
            points.add(matcher.group());
        }
        if (points.size() != 2)
            return null;

        String point1String = null;
        String point2String = null;
        matcher = Pattern.compile(RegularExpression.BIG_WORD_CONTAIN).matcher(segmentString);
        if (matcher.find()) {
            point1String = matcher.group();
        }
        if (matcher.find()) {
            point2String = matcher.group();
        }
        if (point1String != null && point2String != null) {
            if (points.contains(point1String) && !points.contains(point2String)) {
                return point2String;
            } else if (!points.contains(point1String) && points.contains(point2String)) {
                return point1String;
            }
        }
        return null;
    }

    /**
     * 判断expr是不是函数表达式
     *
     * @param expr 待判断的字符串
     * @return 是否是函数
     */
    public static boolean isFunction(String expr) {

        Matcher matcher = RegularExpressionTools.getRegexMatcher("(?<!lo|l)(f|g|h|F|G|H)(_(\\d+|[a-z]|\\([a-z](\\+|\\-|\\*|/)\\d+\\)))?('|\\^\\(\\-1\\))?\\(([\\+\\-/[a-z]\\d\\^\\(\\)]*?(x|t)[\\+\\-/[a-z][A-Z]\\d\\^\\(\\)]*?)\\)", expr);
        Matcher matcher2 = RegularExpressionTools.getRegexMatcher("^y=", expr);

        Matcher matcher1 = RegularExpressionTools.getRegexMatcher("^(f|g|h|F|G|H)\\(x\\)(?=(\\+|\\-|\\*|\\/))", expr);
        //修改BJ201218L
        if (expr.equals("f(x)+g(x)")) {
            return true;
        }
        if (expr.equals("f_(n+1)(x)=f_1[f_n(x)]")) {
            return false;
        }
        if (matcher1.find()) {
            return false;
        }
        //修改ln200813l
        boolean find = matcher.find() || (matcher2.find() && expr.contains("x") && StringUtils.countMatches(expr, "=") < 2);
        boolean contains = expr.contains(">") || expr.contains("<") || expr.contains("≤") || expr.contains("≥");
        return (find && expr.contains("=")) || (find && (!contains));
    }

    /**
     * 判断是否为分段函数
     */
    public static boolean isPiecewiseFunction(String expr) {
        return expr.contains("#");
    }

    /**
     * 判断是否为抽象函数
     * isAbsFunction("f(x)")=true
     * isAbsFunction("y=f(x)")=true
     * isAbsFunction("y=f(x)+g(x)")=true
     * isAbsFunction("y=f(x)+1")=true
     * isAbsFunction("f(x)+g(x)=1")=false
     * isAbsFunction("f(x)=f(x+1)")=false
     * isAbsFunction("f(x)=f'(x+1)+1")=false
     * isAbsFunction("f(x)=x+1")=false
     */
    public static boolean isAbsFunction(String expr) {
        return !expr.contains("=") || ((expr.contains("y") && RegularExpressionTools.isMatcher("((?<!lo|l)[fghFGH](_\\d+|))('|\\^\\(\\-1\\))?\\(x\\)", expr)));
    }

    public static boolean isComplexFunction(String expr) {
        return RegularExpressionTools.isMatcher("\\=(max|min)(\\{|)", expr);
    }


    /**
     * 打标签后对句子的切分
     *
     * @param nerString 待切分的句子
     * @return 拆分后的句子
     */
    public static List<String> splitString(String nerString) {
        if (StringUtils.isEmpty(nerString)) {
            return new ArrayList<>();
        }
        List<String> splitsentenceList;
        splitsentenceList = Lists.newArrayList(nerString.split("(?<!(&&(expr|set|point|circle|line|sequ|triangle|angle|plane|rotator|quadrangle|polyhedron|arc)))( , |。| \\. | ; | \\? )"));
        List<String> mergedsplit = new ArrayList<>();
        for (String singlesentence : splitsentenceList) {
            String Tag = checkmergeTag(singlesentence, splitsentenceList);
            if (!Tag.equals("NoNeedMerge")) {
                Pattern p1 = Pattern.compile("( [^\\s]+(&&line|&&point|&&expr|&&angle))");
                Matcher m1 = p1.matcher(singlesentence);
                if (m1.find()) {
                    int q = m1.start();
                    if (m1.start() == 0) {
                        mergedsplit.add(mergesplit(Tag, singlesentence, splitsentenceList, mergedsplit));
                    } else {
                        mergedsplit.add(singlesentence);
                    }
                } else {
                    mergedsplit.add(singlesentence);
                }
            } else {
                mergedsplit.add(singlesentence);
            }
        }
        return mergedsplit;
    }

    //对于带标志（如四点）的断句错误判断是否需要作切分之后的整合
    public static String checkmergeTag(String sentence, List<String> sentencelist) {
        String[] num = {"((?<!第)二|2)", "((?<!第)三|3)", "((?<!第)四|4)"};
        String[] afternum = {"((个|)(顶|)点)", "((条|)直线|线段|边(?!(形|长)))", "((个|)角(?!形))", "(个|边长)"};
        String[] mergeTag = new String[(num.length) * (afternum.length)];
        int p = 0;
        //笛卡尔拼接出标致数组
        for (String aNum : num) {
            for (String anAfternum : afternum) {
                mergeTag[p++] = aNum + anAfternum;
            }
        }
        for (String mergeTag1 : mergeTag) {
            Pattern pattern = Pattern.compile(mergeTag1);
            Matcher matcher = pattern.matcher(sentence);
            if (matcher.find()) {
                return matcher.group(0);
            }
        }
        return "NoNeedMerge";
    }

    //对于断句错误出现在标志（如四点）前进行整合
    public static String mergesplit(String tag, String sentence, List<String> sentencelist, List<String> sentencelist1) {
        String[] num = {"((?<!第)二|2)", "((?<!第)三|3)", "((?<!第)四|4)"};
        String[] afternum = {"(个|边长)", "((条|)直线|线段|边(?!(形|长)))", "((个|)角(?!形))", "((个|)(顶|)点)"};
        String[] mergetypes = {"&&expr", "&&line", "&&angle", "&&point"};
        int mergenum = 0;
        String mergetype = "&&xepr";
        for (int i = 0; i < num.length; i++) {
            Pattern pattern = Pattern.compile(num[i]);
            Matcher matcher = pattern.matcher(tag);
            while (matcher.find()) {
                mergenum = i + 2;
            }
        }
        for (int j = 0; j < afternum.length; j++) {
            Pattern pattern = Pattern.compile(afternum[j]);
            Matcher matcher = pattern.matcher(tag);
            while (matcher.find()) {
                mergetype = mergetypes[j];
            }
        }
        //sentencelist1.add(sentence);
        int pos = sentencelist1.indexOf(sentence);

        if (mergenum <= (pos + 1) && pos >= 2) {
            String mergesentence = sentence;
            Boolean tag_1 = true;
            for (int m = 0; m < mergenum - 1; m++) {
                if (!sentencelist.get(pos - m - 1).contains(mergetype)) {
                    tag_1 = false;
                    break;
                }
            }
            if (tag_1) {
                for (int i = 0; i < mergenum - 1; i++) {
                    mergesentence = sentencelist1.get(pos - i - 1) + " , " + mergesentence;
                }
                sentence = mergesentence;
                for (int i = 0; i < mergenum; i++) {
                    sentencelist1.remove(sentencelist1.size() - 1);
                }
            }
        }
        return sentence;
    }

    public static void addProperty(JsonObject jsonObject, String name, String value) {
        String[] names = name.split("|");
        for (int i = 0; i < names.length - 1; i++) {
            jsonObject = jsonObject.getAsJsonObject(names[i]);
            if (null == jsonObject) return;
        }
        jsonObject.addProperty(name, value);
    }

    /**
     * JSON中的参数是否为空
     *
     * @param para JSON中拿出的参数
     * @return 若为空, 则成功
     */
    public static boolean paraEmpty(String para) {
        return StringUtils.isEmpty(para) || para.equals("NULL");
    }

    /**
     * JSON中的参数是否不为空
     *
     * @param para JSON中拿出的参数
     * @return 若不为空, 则成功
     */
    public static boolean paraNotEmpty(String para) {
        return !paraEmpty(para);
    }



    //将表达式中混入的单位取出
    public static String getUnit(StringBuffer buffer) {
        String strictRelationUnitRegexForm1 = "(?<=[0-9xy\\)])"
                + "(L/h|km/h|m/s|KM/h|M/s|cm\\^\\(2\\)|cm\\^2|dm\\^2|dm\\^\\(2\\)|mm\\^2|mm\\^\\(2\\)|mm\\^3|mm\\^\\(3\\)|cm\\^3|cm\\^\\(3\\)|dm\\^3|dm\\^\\(3\\)|mm|cm|km|dm|kg|\\(\\(\\(cm\\)\\^2\\)\\))$";
        // 严格的形式
        String strictRelationUnitRegexForm2 = "(?<=[0-9a-z\\)])\\("
                + "(L/h|km/h|m/s|KM/h|M/s|cm\\^\\(2\\)|cm\\^2|dm\\^2|dm\\^\\(2\\)|mm\\^2|mm\\^\\(2\\)|mm\\^3|mm\\^\\(3\\)|cm\\^3|cm\\^\\(3\\)|dm\\^3|dm\\^\\(3\\)|mm|cm|km|dm|kg|m\\^2|m\\^3|(?<!(abs\\())m|g|h|s)\\)$";
        String strictRelationUnitRegex = "("
                + strictRelationUnitRegexForm1 + "|" + strictRelationUnitRegexForm2
                + ")";
        Matcher matcher = Pattern.compile(strictRelationUnitRegex).matcher(buffer.toString());
        if (matcher.find()) {
            buffer.delete(matcher.start(), matcher.end());
            return matcher.group();
        }
        return "";
    }



    /**
     * @param nerStringList 待处理的句子
     * @return 错误切分句子的合并
     * 处理：若 λ=1/k0(k0&&expr ∈ N+&&set  , k0≥2)&&expr  情况
     * 处理为：λ=1/k0(k0∈N+,k0≥2)&&expr
     */
    public static List<String> mergeSentenceForSet(List<String> nerStringList) {
        String temp = null;
        String newReplace = null;
        String nerString = nerStringList.toString();
        //用于提取括号里面的内容
        Pattern pattern = Pattern.compile("(\\([^\\)]+\\))");
        Matcher matcher = pattern.matcher(nerString);
        //遍历取到的括号内容
        while (matcher.find()) {
            temp = matcher.group();
            Matcher matcher1 = Pattern.compile("(&&expr|&&set)").matcher(temp);
            // 若括号内出现 &&expr 或者 &&set  则替换为""
            if (matcher1.find()) {
                newReplace = temp.replaceAll("(&&expr |&&set | )", "");
                nerString = nerString.replace(temp, newReplace);
            }
        }
        if (nerString.startsWith("[") && nerString.endsWith("]")) {
            nerString = nerString.substring(1, nerString.length() - 1);
        }
        //再由List转为String时，List里每个元素最后的空格会被去掉，这里再重新添加空格，
        //如果去掉这步，再调用splitString 方法时候，句子不会被拆分
        nerString = nerString.replace(",", " ,");
        String tempnerString = nerString;
        Matcher matcher1 = pattern.matcher(nerString);
        //这里的while是因为，上一步将“，”替换为“ ,”时，将括号里面的“，”也一起替换了
        //此处在将括号里面的“ ，”替换回来，为“，” 若无此步，转关系会失败
        List<String> bracketList = ExactMaxBracket(nerString);
        // List<String> bracketExtendList= ExactMaxBracketExtend(nerString);
        for (int i = 0; i < bracketList.size(); i++) {
            String oldString = bracketList.get(i);
            String newString = oldString.replace(" ,", ",").replace(" ", "");
            nerString = nerString.replace(oldString, newString);
        }
       /* for (int i = 0 ; i < bracketExtendList.size() ; i ++){
            String oldString = bracketExtendList.get(i);
            String newString = oldString.replace(" ,",",").replace(" ","");
            nerString = nerString.replace(oldString,newString);
        }*/
        //将处理过的句子重新调用splitString方法进行分句
        nerStringList = UtilFunction.splitString(nerString);
        return nerStringList;
    }

    /**
     * @param nerString 待处理的句子
     * @return 处理最外层多余的括号
     * 处理：若 $((AB)^(2)+(BC)^(2)=(EF)^(2))$  情况
     * 处理为：$(AB)^(2)+(BC)^(2)=(EF)^(2)$
     */
    public static String mergeBrackets(String nerString) {
        if (nerString.matches(".*[\\u4E00-\\u9FA5].*")) {
            return nerString;
        }
        if ((!UtilFunction.isInEquality(nerString)) && (!nerString.contains("="))) {
            return nerString;
        }
        if (nerString.contains("[") || nerString.contains("]") || nerString.contains(Constant.VEC_SYMBOL)) {
            return nerString;
        }
        if (nerString.matches(".+=(N(\\*|\\+|)|Ø|∅|Z|Q|R|C)$")) {
            return nerString;
        }

        if (nerString.contains("#")) {
            String[] checkingS = nerString.split("#");
            boolean isT = true;
            for (String checking : checkingS) {
                if (StringUtils.countMatches(checking, "(") != StringUtils.countMatches(checking, ")")) {
                    isT = false;
                    break;
                }
            }
            if (isT) {
                List<String> tempStringList = new ArrayList<>();
                for (String checking : checkingS) {
                    String tempString = mergeSingleExpr(checking);
                    tempStringList.add(tempString);
                }
                return StringUtils.join(tempStringList, "#");
            }
        }
        return mergeSingleExpr(nerString);
    }

    private static String mergeSingleExpr(String nerString) {
        if (nerString.matches(".*[\\u4E00-\\u9FA5].*")) {
            return nerString;
        }
        if ((!UtilFunction.isInEquality(nerString)) && (!nerString.contains("="))) {
            return nerString;
        }
        while (nerString.startsWith("(") && nerString.endsWith(")")) {
            String[] checkingS = nerString.split("=|≥|>|＜|≤|<|≠");
            int num = checkingS.length;
            for (String checking : checkingS) {
                if (StringUtils.countMatches(checking, "(") == StringUtils.countMatches(checking, ")")) {
                    num--;
                }
            }
            if (num > 0) {
                nerString = nerString.substring(1, nerString.length() - 1);
            } else {
                break;
            }
        }
        return nerString;
    }


    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }


    /**
     * 用于提取括号里面的内容（包括括号本身），提取最大括号长度
     *
     * @param message 待处理字符串
     */
    public static List<String> ExactMaxBracket(String message) {

        //String message = "则 m&&expr 的取值范围是 (0 ,1]∪[9 ,+∞)&&set , 已知集合 A={1 ,2}&&set , B={a ,(a^2)+3}&&set ";
        List<String> tempList = new ArrayList<>();
        Stack<Character> bracketStack = new Stack<Character>();
        char[] charmessage = message.toCharArray();
        char temp;
        int bractStart = charmessage.length; //记录最左边“（”的位置
        int bractEnd = 0; //记录最右边“）”的位置
        for (int i = 0; i < charmessage.length; i++) {
            if (i == charmessage.length) {
                break;
            }
            switch (charmessage[i]) {
                case '(':
                    bracketStack.push(charmessage[i]);
                    if (bractStart > i) {
                        bractStart = i;
                    }
                    break;
                case '[':
                    bracketStack.push(charmessage[i]);
                    if (bractStart > i) {
                        bractStart = i;
                    }
                    break;
                case '{':
                    bracketStack.push(charmessage[i]);
                    if (bractStart > i) {
                        bractStart = i;
                    }
                    break;
                case ')':
                    temp = bracketStack.peek();
                    if ((temp == '(') || (temp == '[')) {
                        bracketStack.pop();
                        if (bracketStack.size() == 0) {
                            bractEnd = i;
                            String b = message.substring(bractStart, bractEnd + 1);
                            tempList.add(b);
                            bractStart = charmessage.length;
                            bractEnd = 0;
                            break;
                        }
                    }
                    break;
                case ']':
                    temp = bracketStack.peek();
                    if ((temp == '(') || (temp == '[')) {
                        bracketStack.pop();
                        if (bracketStack.size() == 0) {
                            bractEnd = i;
                            String b = message.substring(bractStart, bractEnd + 1);
                            tempList.add(b);
                            bractStart = charmessage.length;
                            bractEnd = 0;
                            break;
                        }
                    }
                    break;
                case '}':
                    temp = bracketStack.peek();
                    if (temp == '{') {
                        bracketStack.pop();
                        if (bracketStack.size() == 0) {
                            bractEnd = i;
                            String b = message.substring(bractStart, bractEnd + 1);
                            tempList.add(b);
                            bractStart = charmessage.length;
                            bractEnd = 0;
                            break;
                        }
                    }
                    break;
            }
        }
        return tempList;
    }

    /**
     * 提取括号（开闭区间中括号不一致的情形）
     */
    public static List<String> ExactMaxBracketExtend(String message) {


        //String test = "则 m&&expr 的取值范围是 (0 ,1]∪[9 ,+∞)&&set";
        List<String> tempList = new ArrayList<>();
        Stack<Character> bracketStack = new Stack<Character>();
        char[] charmessage = message.toCharArray();
        char temp;
        int bractStart = charmessage.length; //记录最左边“（”的位置
        int bractEnd = 0; //记录最右边“）”的位置
        for (int i = 0; i < charmessage.length; i++) {
            if (i == charmessage.length) {
                break;
            }
            switch (charmessage[i]) {
                case '(':
                    bracketStack.push(charmessage[i]);
                    if (bractStart > i) {
                        bractStart = i;
                    }
                    break;
                case '[':
                    bracketStack.push(charmessage[i]);
                    if (bractStart > i) {
                        bractStart = i;
                    }
                    break;
                case ')':
                    temp = bracketStack.peek();
                    if ((temp == '(') || (temp == '[')) {
                        bracketStack.pop();
                        if (bracketStack.size() == 0) {
                            bractEnd = i;
                            String b = message.substring(bractStart, bractEnd + 1);
                            tempList.add(b);
                            bractStart = charmessage.length;
                            bractEnd = 0;
                            break;
                        }
                    }
                    break;
                case ']':
                    temp = bracketStack.peek();
                    if ((temp == '(') || (temp == '[')) {
                        bracketStack.pop();
                        if (bracketStack.size() == 0) {
                            bractEnd = i;
                            String b = message.substring(bractStart, bractEnd + 1);
                            tempList.add(b);
                            bractStart = charmessage.length;
                            bractEnd = 0;
                            break;
                        }
                    }
                    break;
            }
        }
        return tempList;
    }

//    public static void main(String[] args) {
//        System.out.println(mergeBrackets("(((6b)/(4a))=(b/a))"));
//        System.out.println(deleteBracket("(4/3(5))"));
//        System.out.println(mergeBrackets("(ab=S)#(((6b)/(4a))=(b/a))"));
//    }
}
