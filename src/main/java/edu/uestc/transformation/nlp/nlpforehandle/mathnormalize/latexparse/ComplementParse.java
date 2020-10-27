package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;

import java.util.Stack;
import java.util.regex.Matcher;

/**
 * 补集latex转换
 * Created by lirui on 2016/7/8.
 */
public class ComplementParse {

    /**
     *解析latex形式的补集
     * 得到新格式的补集,形如U-A
     * @param replace 带有latex形式补集的串
     * @return 新格式的补集串
     */
    public String Parse(String origin, int start, String replace){
//        //针对标准形式的新方法,但要求补集中的全集必须在"{}"中,
//        String newRegex = "(\\\\complement|C)_?\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}(?=\\{|\\(|[A-Z])";
//        Matcher matcher = Pattern.compile(newRegex).matcher(replace);
//        if(matcher.find()) {
//            return matcher.replaceFirst(matcher.group("globalset") + "-");
//        }
//        return replace;
        final String[]  regexSet = {
                //形如\complement_{R}{B}变为(R-B)
                "(\\\\complement|C)_?\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}\\{?(?<subset>[A-Za-z∪∩∈+-]+?)\\}?",
                //形如{{\complement }_{U}}B变为(U-B) 或者 形如{{\complement }_{U}}(A∪B)变为(U-(A∪B))
                "\\{\\{(\\\\complement|C)\\}_?\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}\\}(?<subset>[A-Za-z]|\\([A-Za-z∪∩∈+-]+?\\))",
                //形如\complement _{U}(A∪B)变为(U-(A∪B)) 或者 形如\complement _{U}(A)变为(U-A)
                "(\\\\complement|C)_?\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}([A-Za-z]|\\((?<subset>[A-Za-z∪∩∈+-]+?)\\))",
                //形如{{\complement }_{U}}{B}变为(U-B)
                "\\{\\{(\\\\complement|C)\\}_?\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}\\}\\{(?<subset>[A-Za-z∪∩∈+-]+?)\\}",
                //形如{\complement}_{U}B或者{\complement}_{U}(A∪B)变为(U-(A∪B))
                "\\{(\\\\complement|C)\\}_?\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}(?<subset>[A-Za-z]|\\([A-Za-z∪∩∈+-]+?\\))",
                //形如{\complement_U}B或者{\complement_U}(A∪B)变为(U-B)
                "\\{(\\\\complement|C)_?(?<globalset>[A-Za-z∪∩∈+-]+?)\\}(?<subset>[A-Za-z]|\\(([A-Za-z∪∩∈+-]+?)\\))",
                //形如 {\complement}_{UB}}变为 U-B
                "\\{(\\\\complement|C)\\}_?\\{(?<globalset>[A-Za-z])(?<subset>[A-Za-z])\\}",
                //形如{C_U}{{M}}变为U-M
                "\\{(\\\\complement|C)_?(?<globalset>[A-Za-z∪∩∈+-]+?)\\}\\{\\{(?<subset>[A-Za-z]|\\(([A-Za-z∪∩∈+-]+?)\\))\\}\\}",
                //形如{C_U}B变为U-B
                "\\{(\\\\complement|C)_?(?<globalset>[A-Za-z∪∩∈+-]+?)\\}(?<subset>[A-Za-z]|\\(([A-Za-z∪∩∈+-]+?)\\))",
                //刑如\complement {{\,}_{U}}(A∪B)变为 U-(A∪B)
                "\\\\complement\\{\\{\\\\,\\}_\\{(?<globalset>[A-Za-z∪∩∈+-]+?)\\}\\}(?<subset>[A-Za-z]|\\(([A-Za-z∪∩∈+-]+?)\\))",
                //形如C_UP变为U-P
                "C_(?<globalset>[A-Za-z∪∩∈+-]+?)(?<subset>[A-Za-z]|\\(([A-Za-z∪∩∈+-]+?)\\))",
        };

        for (String regex : regexSet){
            replace = getAndRelpaceMatchComplement(regex, replace);
        }
        return replace;
    }

    /**
     * 根据传入的正则表达式,找到并按要求替换原串的补集部分
     * @param regex 正则表达式
     * @param origin 原始串
     */
    private String getAndRelpaceMatchComplement(String regex, String origin){
        Matcher matcher = RegularExpressionTools.getRegexMatcher(regex, origin);
        return matcher.find() ? matcher.replaceFirst(formNewComplement(matcher.group("globalset"), matcher.group("subset"))) : origin;
    }

    /**
     * 根据或许到的全集字符串和待求补集的集合,组成同一的新的补集格式
     * 刑如"U-A"
     * @param global 全集串
     * @param sub 待求补集的集合串
     * @return  新的补集格式串
     */
    private String formNewComplement(String global, String sub){
        String result = cheekAndAddBracket(global) + "-" + cheekAndAddBracket(sub);
        return cheekAndAddBracket(result);
    }

    /**
     * 检查该表达式是否需要增加括号,若需要,则增加括号
     * @param data 待检查表达式串
     */
    private String cheekAndAddBracket(String data){
        return data.length() == 1 || isOuterBracketExist(data)? data :  "("+data+")" ;
    }

    /**
     * 判断表达式最外层是否存在括号
     * @param express 表达式
     * @return 若存在,则成功
     */
    private boolean isOuterBracketExist(String express){
        if(!(express.startsWith("(") && express.endsWith(")"))){
            return false;
        }
        Stack<Character> stack = new Stack<>();
        char[] array = express.toCharArray();
        stack.push(array[0]);
        for(int i = 1; i < array.length; i++){
            if(array[i] == ')'){
                stack.pop();
            }else if(array[i] == '('){
                stack.push(array[i]);
            }
            if(stack.empty() && i != array.length-1){
                return false;
            }
        }
        return true;
    }

}

