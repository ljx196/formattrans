package edu.uestc.transformation.nlp.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.MathematicsNormalizeUtil;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author ：yyWang
 * @date ：Created in 2019/7/5
 * @description： 将文本中 英文字母用LaTeX 的 $ 进行包裹
 * 思路：
 * 1. 先用正则进行中文匹配，然后使用中文进行分割
 * 2. 处理一些无用的字符，一些标点符号。
 * 3. 处理需要LaTeX化的实体，并将替换的结果保存到list 集合中。
 * 4. 对应文本根据list保存的内容进行替换，但是每个实体LaTeX化后都需要用其他字符代替。以免后面的结果覆盖当前内容
 * 5. 将用其他字符替换后文本恢复LaTeX化实体，处理完成，返回结果
 */
public class LatexConvert {

    private static final Logger log = LogManager.getLogger(LatexConvert.class);

    // 保存替换后的字符串,可以保存重复的键值对
    private List<Map<String, String>> keymap = new ArrayList<>();
    // 将转LaTeX化的实体进行替换成 replaceLatex 集合中的字符
    private Map<String, String> replaceMap = new LinkedHashMap<>();
    // 从替换的元素中任选一个进行替换
    private static Random random = new Random();

    //    匹配中文字符
    private static final String ChineseStr = "[\\u4e00-\\u9fa5\"（），、？：“”∽⊥≌∥∵∴]+";

    // 坐标、定义域、值域正则
    private static final String coordinate_regularity = "[_{}A-z0-9∈]*[(\\[][-A-z0-9∞≠≤≥≈/*()^+\\{}]+,.*[)\\]]";

    // 正向先行断言，匹配坐标左侧非中文字符
    private static final String left_bracket = "[^\\u4e00-\\u9fa5]+,(?=\\$.*\\$)";
    private static final String left_replace = "([^\\u4e00-\\u9fa5∠]+)(,\\$.*\\$)";

    // 实例化正则判断
    private static final String theoremRege = "^#[a-zA-Z0-9]+#";

    // 正向后行断言，匹配坐标右侧非中文字符
    private static final String right_bracket = "(?<=\\$.{1,50}\\$),[^\\u4e00-\\u9fa5]+";
    private static final String right_replace = "(\\$.*?\\$,)([^\\u4e00-\\u9fa5∠]+)";
    //    需要删除的符号
    private List<String> deleteStr = new ArrayList<>(Arrays.asList(
            "", "(", ",", "“", "”", ")", "()", "（", "），", "，",
            "\"", "。", "、", "：", " ", "“", "”", "∽", "≌", "⊥", "∥", ".", "．", ":", "&"
    ));


    /**
     * 将json 字符串中的某些字段LaTeX化
     *
     * @param json json字符串
     * @return 返回 LaTeX化的json 字符串
     */
    public String jsonToLatex(String json) {
        JSONObject o = JSONArray.parseObject(json);

        // 获得当前处理文本类型
        String type = o.getString("type");

        // 对题干 LaTeX 化
//        String stem = getMapleString(o.getString("stem"), type);
        String origin = o.getString("stem");
        if (origin != null) {
            String stem = getMapleString(origin, null);
            o.put("stem", stem);
            if (!origin.equals("")) {
                log.info(origin + " 添加$后的结果为：\n" + stem);
            }

        }


        // 对小问LaTeX 化
        JSONArray subStems = o.getJSONArray("subStems");
        if (subStems != null) {
            o.put("subStems", jsonArrayLatex(subStems, "subStems", type));
        }


        // 对选项 LaTeX 化
        JSONArray options = o.getJSONArray("options");
        if (options != null) {
            o.put("options", jsonArrayLatex(options, "options", type));
        }


        // 对标准答案 LaTeX化
        JSONArray solutions = o.getJSONArray("solutions");
        if (solutions != null) {
            o.put("solutions", jsonArrayLatex(solutions, "solutions", type));
        }


        return o.toJSONString();
    }


    /**
     * 对 JSONArray 里的元素LaTeX 化
     *
     * @param subStems JSONArray 对象
     * @param stems    当前处理的 json 字段用于区别当前处理是不是选择题
     * @param type     当前标注文本的类型，0 是实例化，其他不关心
     * @return 返回已经LaTeX化的 JSONArray 对象
     */
    private JSONArray jsonArrayLatex(JSONArray subStems, String stems, String type) {

        JSONArray j = new JSONArray();
        for (Object subStem : subStems) {
            String t = standardInput((String) subStem, stems);
//            String temp = getMapleString(t, type);
//            ArrayList split = splitLatexText(t, type);

            String temp = getMapleString(t, null);

//            if (stems.equals("solutions")) {
//                // 对解答过程中的 LaTeX进行 转maple 处理
//                temp = getMapleString(t, null);
//            }
            if (t.length() != 0) {
                log.info(t + " 添加$后的结果为：\n" + temp);
            }

            // 将转换后的字符串添加到 JSONArray 对象中
            j.add(temp);
        }
        return j;
    }


    /**
     * 对小问中的特殊符号进行标准化输入，比如 填空题的下划线，选择题的括号，以及选项中的A,B选项
     *
     * @param temp  待处理的字符串
     * @param stems 当前处理的 json 字段用于区别当前处理是不是选择题
     * @return 返回标准后的字符串
     */
    public String standardInput(String temp, String stems) {
        String[] regs = {
                "^(\\([\\dⅠⅡⅢ]\\)|（[\\dⅠⅡⅢ]）)[、.]?",
                "([(（ ]+[)）]|[_;]*)$"
        };
        StringBuilder reg = new StringBuilder();
        for (String r : regs) {
            reg.append("(");
            reg.append(r);
            reg.append(")");
            reg.append("|");
        }

        // 去掉最后一个 |
        int len = reg.length();
        reg.replace(len - 1, len, "");


        if (stems.equals("options")) {
            reg.append("|").append("^[A-F、.]*");
        } else {
            reg.append("|").append("^(\\(|\\[|（)[\\S]*?(\\)|\\]|）)");
            reg.append("|").append("^\\S?[\\d]+(,|\\.)");
        }
        return temp.replaceAll(reg.toString(), "").trim();
    }


    /**
     * @param text 将 LaTeX 文本转换为 maple 文本
     * @param type 当前标注的数据类型
     * @return 返回转换成 maple文本
     */
    public String getMapleString(String text, String type) {

        // 传过来的文本没有数据，不用处理
        if (text.length() == 0 && text.equals("")) {
            return text;
        }
        // 去除题目信息中开头的年份，地区
       // text = dryness(text);

        // 由于idea 的原因会出现 成对的转移符,如果去掉转义符会出现方程组转maple 失败
//        text = text.replace("\\\\", "\\");

        //在处理一期题目的时候会遇到. 这种符号
        text = text.replaceAll("\\.(?![0-9↑({])", "，");
        text = text.replaceAll("\\\\\\(","(").replaceAll("\\\\\\（","(");
        text = text.replaceAll("\\\\\\)",")").replaceAll("\\\\\\）",")");
        text = text.replaceAll("（","(").replaceAll("）",")");
        text = text.replaceAll("(?<=[a-zA-Z0-9])，(?=[a-zA-Z0-9])",",");
        //去除#%#
        text = text.replaceAll("#%#","，");

        //去掉一些html标签
        String[] regs1 = {
                "\r",
                "<br>",
                "<div>",
                "</div>",
                "style(.*?)(?=>)",
                "</sub>",
                "\\&[a-zA-Z]{1,10};",
                "</sup>",
                "<img(.*)>"
        };
        text = text.replaceAll(LTools.createRegexOrByStringList(regs1),"");
        text = text.replaceAll("<sup>","^");
        text = text.replaceAll("<sup >","^");
        text = text.replaceAll("<sub>","_");
        // 防止中文逗号转为英文逗号
        text = text.replace("，", "艮");

        text = MathematicsNormalizeUtil.normalize(text);


        // 将部分英文逗号转为中文逗号
        String[] regs = {
                "(?<=[\\u4e00-\\u826d\\u826f-\\u9fa5][-A-Za-z_0-9=()/*+∠]{0,13}),(?=[A-Za-z_0-9]*[\\u4e00-\\u9fa5])",
                ",(?=[\\u4e00-\\u9fa5]|[∵∴])",
                "(?<=[\\u4e00-\\u9fa5]),",
                //",$",
                "#%#"
        };
        text = text.replaceAll(LTools.createRegexOrByStringList(regs),
                "，");

        // 将中文逗号有条件的转为英文逗号
        text = text.replaceAll("(?<=[\\u4e00-\\u9fa5][A-Z]{1,2})，(?=[A-Z]{1,2}[\\u4e00-\\u9fa5])", ",");

        // 替换成题目文本最开始的中文逗号
        text = text.replace("艮", "，");
        // 对处理过后的文本添加 $ 符号,因为通过maple转换后会对坐标添加$ 符号，但影响LaTeX处理，就暂时取消$
//        if (patternNum(text, "#[a-zA-Z0-9]+#") && text.contains("$")) {
//            text = text.replace("$", "");
//        }
        String temp = getPattern(text);

        // 将LaTeX 字符串转为 Maple 字符串,对实例化结论再次转maple 可能会有问题，就取消再次转换
        //if (!patternNum(text, "#[a-zA-Z0-9]+#")) {
        temp = MathematicsNormalizeUtil.normalize(temp);
        // }


        if (temp.length() != 0 && temp.charAt(temp.length() - 1) == '，') {
            temp = temp.substring(0, temp.length() - 1);
        }

//
//        if (type.equals("0")) {
//            temp = temp + splitText;
//        }

        return temp;
    }

    /**
     * 切分三元组
     *
     * @param text
     * @param type
     * @return
     */
    private ArrayList splitLatexText(String text, String type) {
        ArrayList<String> splitText = new ArrayList<>();
        if (text.length() == 0 && text.equals("")) {
            return splitText;
        }

        if (type.equals("0")) {
            String temp = text;
            text = text.split("#.*$")[0];
            splitText.add(text);
            splitText.add(temp.substring(text.length()));
        }

        return splitText;
    }

    /**
     * 去除输入题目信息中开头的年份.地区
     *
     * @param text 题目信息
     * @return 去燥后的题目信息
     */
//    private String dryness(String text) {
//        if (StringUtils.isEmpty(text)) {
//            return StringUtils.EMPTY;
//        }
//        final String regex1 = "^(\\(|\\[|（|【)";
//        final String regex2 = "[\\u4e00-\\u9fa5]";
//        Matcher matcher = RegexConstant.getMatcher(regex1, text);
//        if (matcher.find()) {
//            int startIndex = 1;
//            char startChar = matcher.group().charAt(0);
//            char endChar;
//            switch (startChar) {
//                case '(':
//                    endChar = ')';
//                    break;
//                case '[':
//                    endChar = ']';
//                    break;
//                case '（':
//                    endChar = '）';
//                    break;
//                default:
//                    endChar = '】';
//                    break;
//            }
//            int endIndex = ExprTools.matchingParentheses(text, startIndex, startChar, endChar);
//            if (endIndex > startIndex) {
//                String temp = text.substring(startIndex, endIndex);
//                if (RegexConstant.hasPattern(regex2, temp)) {
//                    text = text.substring(endIndex + 1);
//                }
//            }
//        }
//        return text;
//    }

    /**
     * 文本中的实体 LaTeX 化，暴露接口给外界访问的入口
     *
     * @param str 待处理的文本
     * @return 返回处理后的文本
     */
    public String getPattern(String str) {

        keymap.clear();
        replaceMap.clear();
        //  步骤1. 按照中文进行切分
        Pattern pattern = Pattern.compile(ChineseStr);
        String[] stirs = pattern.split(str.trim());
        List<String> strSet = new ArrayList<>(Arrays.asList(stirs));

        //Pattern pattern_tang = Pattern.compile("；\\([0-9]\\)");

        // 步骤2.删除一些标点符号，统一格式
        strSet.removeAll(deleteStr);

        // 步骤3.处理按照中文切分后的字符串，并按照格式再次切分，切割成只有单个字符或者实体
        for (String string : strSet) {
           if(string.equals(":(1)")){
               Map<String,String> temp = new HashMap<String,String>();
               temp.put(":(1)","，(1)");
               keymap.add(temp);
               continue;
           };
         if(Pattern.matches(".*；\\([0-9]\\).*",string)){
            // String oldStr = string;
             //String newStr = string.replaceAll("；","");
            // Map<String,String> tmp = new HashMap<>();
            // tmp.put(oldStr,newStr);
          //   keymap.add(tmp);
             continue;
         }
            // 因为解答过程中实体是用中括号包裹的需要去掉中括号
            // 这里是解答过程的处理
            if (patternNum(string, "^(?:\\\\)?\\[(.*)(?:\\\\)?\\]$")) {
                String befor = string;
                string = string.replaceAll("^(?:\\\\)\\[(.*)(?:\\\\)\\]$", "$1");
                str = str.replace(befor, string);
            }
            dealStr(string);
        }

        // LaTeX 用下面的词进行替换，最后再转换
        List<String> stringList = new ArrayList<>(Arrays.asList(
                "魑魅", "魍魉", "狰讙", "驳鯥", "蠃鱼", "孰湖", "穷奇", "伏羲琴", "冉遗鱼", "鵸鵌", "东皇钟", "昊天塔", "异兽狡", "玃如", "毕方",
                "豪彘", "鹿蜀", "狌狌", "羬羊", "虎蛟", "瞿如", "猾褢", "蛊雕", "狸力", "赤鱬", "灌灌", "猼訑", "盘古斧", "旋龟", "鸾鸟",
                "溪边", "文鳐", "白雉", "梼杌", "天之痕", "轩辕剑", "神农鼎", "炼妖壶", "昆仑镜", "崆峒", "乾坤鼎", "西王母", "洪荒", "盘古幡", "诛仙剑阵",
                "貝筆", "罷備", "畢邊", "參倉", "産長", "芻從", "達帶", "動斷", "樂離", "劉龍", "婁盧", "馬買", "門黽", "難鳥", "聶寜",
                "齊豈", "氣遷", "僉喬", "親窮", "嗇殺", "審聖", "師時", "夀屬", "雙肅", "嵗孫", "萬為", "韋烏", "獻鄉", "寫尋", "亞嚴",
                "厭堯", "業頁", "義兿", "陰隱", "猶魚", "與雲", "鄭執", "質專", "標錶", "彆蔔", "擔膽", "導燈", "鄧敵", "糴遞", "點澱",
                "塵襯", "稱懲", "遲衝", "懺償", "廠徹", "攙讒", "蠶燦", "礎處", "觸辭", "聰叢", "鬥獨", "噸奪", "電鼕", "礬範", "飛墳",
                "鳳膚", "婦復", "蓋乾", "趕個", "鞏溝", "構購", "榖顧", "颳關", "觀櫃", "漢號", "閤轟", "後鬍", "壺滬", "護劃", "懷壞"


        ));

        // 步骤4.按照文本中的实体，替换首次匹配到的实体
        for (Map<String, String> entity : keymap) {
            for (Map.Entry<String, String> edit : entity.entrySet()) {
                str = str.replaceFirst(Pattern.quote(edit.getKey()), Matcher.quoteReplacement(edit.getValue()));
                // 虽然按照首次匹配的正则进行替换，但是还会出现后面覆盖前面实体LaTeX化的内容，这时可以将已经 LaTeX化的实体利用其它字符进行替换。
                str = replaceStr(str, stringList);
            }

        }


        // 替换成原字符串
        str = replaceEndStr(str);

        // 特殊情况处理
        str = specialHandle(str);
        return str;
    }

    /**
     * 去除不需要打 $ 符合的字段
     *
     * @param str 已标注后的字符串
     * @return
     */
    private String specialHandle(String str) {
        str = str.replaceAll("([第前])\\$(n.*?)\\$(项和?)", "$1$2$3");
//        str = str.replace("前$n$项的和", "前n项的和");
        str = str.replaceAll("(?<=[\\u4e00-\\u9fa5])\\$([)_])\\$", "$1");
        return str;
    }

    /**
     * 步骤5 将用其他字符替换的 LaTeX化的实体，进行还原，成原字符串
     *
     * @param str 需要替换的字符串
     * @return 返回替换后的字符串
     */
    private String replaceEndStr(String str) {
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            str = str.replace(entry.getKey(), entry.getValue());
        }
        return str;
    }

    /**
     * 步骤4.将已经转成 LaTeX 的实体进行其他字符替代以免被覆盖
     *
     * @param str        需要处理的字符串
     * @param stringList 保留关键字的集合
     * @return 返回替换后的字符串
     */
    private String replaceStr(String str, List<String> stringList) {
        String reg = "(\\$.*?\\$)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            // 从list 集合中取出一个字符进行替换。关键字
            String key = stringList.get(random.nextInt(stringList.size()));
            // 替换一个
            str = str.replace(matcher.group(1), key);
            stringList.remove(key);
            replaceMap.put(key, matcher.group(1));
        }
        return str;
    }


    /**
     * 根据正则去判断字符串是否包含指定内容
     * 如果不包含就返回匹配的结果，
     *
     * @param str        待匹配的字符串
     * @param is_default 如果为空就使用默认正则，否则使用传入的正则
     * @return 返回匹配的结果
     */
    private boolean patternNum(String str, String is_default) {
        String regex = "[A-Za-z]+_[0-9]+|[0-9]+|[^-0-9_()]+";
        if (is_default != null) {
            regex = is_default;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 步骤3
     * 对list集合里的字符串进行处理，并分割成单个字符。进行实体 LaTeX 化
     * 处理后的结果保存在keymap 全局集合中返回处理的 list 集合，就是处理过的字符串，关键字是原字符串，对应的值是替换后的
     * LaTeX实体，再将这个map 放进keymap list 集合中，因为list集合允许元素重复。当一个题目中有多个重复的实体，后面进行
     * 处理时就会被替换。文本中有多少个需要转化的实体，list就有多少个元素，但是用hashmap 进行存储时元素个数就会小于这个值。
     *
     * @param string 需要处理的字符串
     */
    private void dealStr(String string) {
        // 如果是坐标的话就按照坐标进行切分，否则就按照逗号或者顿号进行切割
        // ，|[A-z]?\([A-z0-9∞]+.*?\)|、|,

        String[] old = string.split("[，、。；：]");

        if (patternNum(string, "^\\\\\\{\\\\begin\\{array}")) {
            // 对方程组进行特殊切分，保证
            string = string.replaceAll("(^\\\\\\{\\\\begin\\{array}.*\\\\end\\{array})(.*$)", "$1\\$$2");
            old = string.split("\\$");
        } else if ((countString(string, ":") == 1 && patternNum(string, "^[A-Za-z]:"))) {
            // 是为了处理解析几何中字母后面跟一个解析式的情况
            old = string.split("[:]");
        } else if ((countString(string, "[><=＞＜≥≤]") > 1 && !patternNum(string, theoremRege)) ||
                (countString(string, "[),]") >= 2) && isCoordinate(string)) {
            // |(?<=[>=＞＜≥≤][-A-Za-z_0-9]{1,4}),
            old = string.split("((?<=[>$}A-Za-q)]),|,(?=[A-Z]))");
        }
        if (old.length > 1) {
            for (String str : old) {
                // 去掉字符串的前后空格的影响
                str = str.trim();
                // 递归调用进行 实体LaTeX化
                dealStr(str);
            }
        } else if (old.length != 0 && !"".equals(old[0]) && old[0].length() != 0) {
            String replaceStr = newStrReplace(old[0].trim());
            Map<String, String> keyValue = new HashMap<>();
            keyValue.put(old[0].trim(), replaceStr);
            // 将原实体添加到 map集合中，并保存对应的 键值对，key是原字符串，value是 LaTeX化的实体。然后将这个map 添加到list 集合中。
            keymap.add(keyValue);
        }

    }

    /**
     * 用来分析字符串中包含的英文逗号是否是坐标里的逗号，如果不是就按照英文逗号分隔字符串，否则就不分隔
     *
     * @param string 包含英文逗号和括号的字符串
     * @return 返回是否需要按照逗号分隔字符串
     */
    private boolean isCoordinate(String string) {
        int left = 0;
        boolean flag = true;
        if (!string.contains(",")) {
            return false;
        }
        for (char c : string.toCharArray()) {
            if (c == '(') {
                left++;
            } else if (c == ')') {
                left--;
            } else if (c == ',') {
                if (left != 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }


    /**
     * 统计string 字符串出现 s 的个数
     *
     * @param string 原字符串
     * @param s      子字符串
     * @return 返回出现的个数
     */
    private int countString(String string, String s) {
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(string);
        int count = 0;
        while (m.find()) {
            count++;
        }

        return count;
    }

    /**
     * 单个字符串处理，是否添加 $,也就是添加匹配规则。
     *
     * @param str 需要处理的字符串
     * @return 返回添加 $ 后的字符串，也就是返回 LaTeX 化的实体。
     */
    private String newStrReplace(String str) {
        if (!str.contains("$") && !str.equals("")) {

            // 处理等式，方程组，及实例化的情况
            if (countString(str, "[＝=]") >= 1 ||
                    patternNum(str, theoremRege) ||
                    patternNum(str, "^\\\\\\{\\\\begin\\{array}.*?\\\\end\\{array}$")) {
                str = "$" + str + "$";
            } else if (patternNum(str, coordinate_regularity)) {
                // 处理坐标
                str = str.replaceAll("(" + coordinate_regularity + ")", "\\$$1\\$");

                // 处理两边都是坐标的情况
                String t = str.replaceAll("\\$.*\\$", "");
                if (!t.equals(",") && str.endsWith("$")) {
                    // 处理坐标左边有这样的 12,$(1,2)$
                    if (!patternNum(t, coordinate_regularity)) {
                        String ref = str.substring(t.length());
                        t = t.replaceAll("([-A-Z'0-9_a-z()+\\\\{}≤/*]+)", "\\$$1\\$");
                        str = t + ref;
                    } else {
                        str = str.replaceAll(left_replace, "\\$$1\\$$2");
                    }

                } else if (!t.equals(",") && str.startsWith("$")) {
                    //处理坐标右边有这样的情况$(1,2)$，12
                    if (!patternNum(t, coordinate_regularity)) {
                        String ref = str.substring(0, str.length() - t.length());
                        t = t.replaceAll("([-A-Z'0-9_a-z()+\\\\{}≤/*]+)", "\\$$1\\$");
                        str = ref + t;
                    } else {
                        str = str.replaceAll(right_replace, "\\$$1\\$$2");
                    }
                }
            } else if (str.contains("∠") || str.contains("△") || str.contains("Δ")) {
                // 因为字符串可能同时包含 cos∠ABC 而这种情况需要整体打$
                if (str.contains("cos") || str.contains("tan") || str.contains("sin")) {
                    str = "$" + str + "$";
                } else {
                    // 处理需要将特殊符号打在外面的情况
                    str = str.replaceAll("([-A-Z'0-9_a-z()+\\\\{}]+)", "\\$$1\\$");
                    // 处理 $Rt$△这个情况
                    if (str.contains("$Rt$")) {
                        str = str.replace("$Rt$", "Rt");
                    }
                }
            } else if (str.contains(",")) {
                if (patternNum(str, "[∈]")) {
                    str = "$" + str + "$";
                } else {
                    // 处理 P,Q 这种情况,使用的是英文逗号分隔的
                    str = str.replaceAll("([-A-Z'0-9_a-z()+↑\\\\{}]+)", "\\$$1\\$");
                }
            } else if (patternNum(str, null)) {
//                str = "$" + str + "$";
                // 对 \[f'(x) < 0\] 这种形式做处理去掉中括号
               str = str.replaceAll("([-+*A-Za-z0-9_^λαθ∂ρβγη.δωφϕμ()/<>＞＜≥≠≤↑':!{}ￃ\\[\\]\\\\]+)", "\\$$1\\$");
      //          str = str.replaceAll("([-+*A-Za-z0-9_^λαθ∂ρβγη.δωφϕμ()/<>＞＜≥≠≤↑'!{}ￃ\\[\\]\\\\]+)", "\\$$1\\$");
            }
        }
//        if (str.contains("$")) {
//            // 对 $ 标注的 实体进一步处理
//            String[] temp = str.split("\\$.*?\\$");
//            for (String t : temp) {
//                dealStr(t);
//            }
//        }
        return str;
    }

//    /**
//     * 将题目规范化，处理题目符号错误的情况，将中文逗号合理地换成英文逗号
//     *
//     * @param question 待处理的文本
//     * @return 返回处理后的句子
//     */
//    public String questionPretreated(String question) {
//        // 现将题目按照中文的逗号进行分割，然后用正则去判断，是否需要将中文逗号修改
//        // 成英文逗号
//        String[] temp = question.split("，|．");
//        StringBuffer afterQuestion = new StringBuffer(temp[0]);
//        for (int i = 1; i < temp.length; i++) {
//            questionRegex(afterQuestion, temp[i]);
//        }
//        return afterQuestion.toString();
//    }
//
//    /**
//     * 处理题目符号的规则
//     *
//     * @param afterQuestion 已处理好的短句
//     * @param origin        待处理的短句
//     */
//    private void questionRegex(StringBuffer afterQuestion, String origin) {
//        //判断上一个短句的正则
//        String[] beforeRules = {
//                "[A-Za-z0-9)]+$",
//        };
//        // 判断当前短句正则，是否需要修改符号
//        String[] afterRules = {
//                "^[_A-Za-z0-9{}(]+",
//                "^[与]+",
//        };
//        if (patternNum(afterQuestion.toString(), beforeRules[0]) && patternNum(origin, afterRules[0])) {
//            // 处理中文逗号转成中文顿号
//            afterQuestion.append("、").append(origin);
//        } else if (patternNum(origin, afterRules[1])) {
//            // 处理中文逗号转成因为逗号
//            afterQuestion.append(",").append(origin);
//        } else {
//            afterQuestion.append("，").append(origin);
//        }
//
//    }
}

