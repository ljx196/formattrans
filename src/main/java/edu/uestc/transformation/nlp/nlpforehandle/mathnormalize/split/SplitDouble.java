package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZZH on 2016/9/18.
 */
public class SplitDouble {

    public static String pointAnalysisType1(String conjTemp) {
        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(?<=(和|关于|与|到))[^和到与关于]+?的(顶点|焦点|圆心|点)(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        System.out.println("*******conj start********");
        if (matcher.find()) {
            String tempLine = "点 P_" + pos +"&&point ";
            int i = matcher.start();
            int j = matcher.end();
            stringBuilder.append(conjTemp.substring(pos, i));
            stringBuilder.append(tempLine);
            stringBuilder.append(conjTemp.substring(j, conjTemp.length()));
            String startString = matcher.group() + "为" + tempLine + ", ";
            result = startString + stringBuilder.toString();
            System.out.println(result);
            return result;
        }
        System.out.println("*******conj end********");
        return conjTemp;
    }



    //点Q到抛物线C的准线的距离为34
    //(和|与|到)。。。的(切线|直线|准线|线段)的。。。
    public static String lineAnalysisType1(String conjTemp) {
        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(?<=(和|与|到))[^和|到|与]+?的(切线|直线|准线|线段)(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        System.out.println("*******conj start********");
        if (matcher.find()) {
            String tempLine = " l_" + pos +"&&line ";
            int i = matcher.start();
            int j = matcher.end();
            stringBuilder.append(conjTemp.substring(pos, i));
            stringBuilder.append(tempLine);
            stringBuilder.append(conjTemp.substring(j, conjTemp.length()));
            String startString = matcher.group() + "为" + tempLine + ", ";
            result = startString + stringBuilder.toString();
            System.out.println(result);
            return result;
        }
        System.out.println("*******conj end********");
        return conjTemp;
    }



    public static String lineAnalysisType2(String temp) {
        StringBuilder sorSentence = new StringBuilder();
        String result = "";
        int count = 0;
        int pos = 0;
        Pattern pattern = Pattern.compile("(经过|过)[^过交在]+?的(切线|直线|线段|准线)(?=的)");
        Matcher m = pattern.matcher(temp);
        System.out.println("********start********");
        if (m.find()) {
            String tempLine = " l_" + count+"&&line " ;
            int i = m.start();
            int j = m.end();
            sorSentence.append(temp.substring(pos, i));
            sorSentence.append(tempLine);
            sorSentence.append(temp.substring(j, temp.length()));
            String startString = m.group() +  "为" + tempLine + " ,";
            result = startString + sorSentence.toString();
            System.out.println(result);
            return result;
        }
//        System.out.println(sorSentence);
        System.out.println("********end*********");
        return temp;
    }

    // (在|以)。。。的(切线|直线|准线|线段)的。。。
    // (在|以)点A的(切线|直线|准线|线段)的斜率。。。
    public static String lineAnalysisType3(String conjTemp) {
        StringBuilder stringBuilder = new StringBuilder();
        String result ;
        int pos = 0;
        Pattern conjPattern = Pattern.compile("(?<=(在|以))[^以在]+?的(切线|直线|准线|线段)(?=的)");
        Matcher matcher = conjPattern.matcher(conjTemp);
        System.out.println("*******perp start********");
        while (matcher.find()) {
            int end = matcher.end();
            stringBuilder.append(conjTemp.substring(pos, end));
            String addString = "为 l_"+pos+"&&line , "+" l_"+pos+"&&line ";
            stringBuilder.append(addString);
            pos = end;
        }
        stringBuilder.append(conjTemp.substring(pos, conjTemp.length()));
        result =  stringBuilder.toString();

//        System.out.println(result);

        System.out.println("*******perp end********");
        return result;
    }

}
