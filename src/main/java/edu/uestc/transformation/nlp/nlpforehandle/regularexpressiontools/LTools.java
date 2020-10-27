package edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools;


import edu.uestc.transformation.nlp.nlpforehandle.util.CharUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LTools {

    /**
     * StringBuffer实现replaceAll的方法 ,返回是否做了修改
     *
     * @param originRegex 待替换正则表达式
     * @param target      替换成的字符串
     */
    public static boolean stringBufferReplaceAll(StringBuffer sb, String originRegex, String target) {
        return stringBufferReplaceAll(sb, originRegex, target, true);
    }
    public static boolean stringBufferReplaceAll(StringBuffer sb, String originRegex, String target, boolean changePos) {
        if (sb == null) {
            return false;
        }
        boolean flag = false;
        Pattern p = Pattern.compile(originRegex);
        Matcher matcher = p.matcher(sb);
        List<Integer> posBegin = new ArrayList<>();
        List<Integer> posEnd = new ArrayList<>();

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            posBegin.add(start);
            posEnd.add(end);
            flag = true;
        }
        for (int i = posBegin.size() - 1; i >= 0; i--) {
            int start = posBegin.get(i);
            int end = posEnd.get(i);
            sb.replace(start, end, target);
            if (changePos) {
                //TODO
//                globalInformation.getLocationlocal().get().replaceString(start, end, target, globalInformation);
            }
        }
        return flag;
    }

    /**
     * 判断是不是坐标形式
     */
    public static boolean isCoordForm(String str) {
        if (str == null) {
            return false;
        }
        int level = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (CharUtils.isFrontBracket(c)) {
                level++;
            } else if (CharUtils.isBackbracket(c)) {
                level--;
            } else if (c == ',' && level == 0) {
                return false;
            }
        }
        return level == 0;
    }

    /**
     * 在StringBuffer 中 根据insertList 插入flag
     */
    public static void insertStringInStringBuffer(StringBuffer stringBuffer, List<Integer> insertList, String flag) {
        if (insertList == null || insertList.size() == 0) {
            return;
        }

        //升序排序
        Collections.sort(insertList);
        for (int i = insertList.size() - 1; i >= 0; i--) {
            stringBuffer.insert(insertList.get(i), flag);
        }
    }

    /**
     * oldString without $
     */
    public static String filter(String oldString) {

        if (oldString != null && oldString.length() > 0 &&
                (oldString.charAt(0) == '=' || oldString.charAt(0) == ':'))
            return oldString.substring(1);
        return oldString;
    }

    /**
     * 获取该点的name
     */
    public static String getName(String foot) {
        if (foot == null || foot.equals(""))
            return null;
        Matcher matcher = Pattern.compile(RegularExpression.BIG_WORD_CONTAIN).matcher(foot);
        if (matcher.find())
            return matcher.group();
        else if (foot.contains("原点")) {
            return "O";
        }
        return null;
    }

    /**
     * 找第一个点的名字
     */
    public static String getName(List<String> points) {
        if (points == null || points.size() == 0)
            return null;
        for (String point : points) {
            String name = getName(point);
            if (name != null)
                return name;
        }
        return null;
    }


    /**
     * 根据数组中的单个的regex，生成用 | 连接的latex
     */
    public static String createRegexOrByStringList(String[] regexs) {
        StringBuilder stringBuilder = new StringBuilder("(");
        for (String s : regexs) {
            stringBuilder.append("(");
            stringBuilder.append(s);
            stringBuilder.append(")");
            stringBuilder.append("|");
        }
        int len = stringBuilder.length();
        //把最后一个“|”转换为“）”
        stringBuilder.replace(len - 1, len, ")");
        return stringBuilder.toString();
    }

}
