package edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools;



//import java.util.ArrayList;
//import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式相关工具类
 * Created by zzh on 2016/2/25
 */
public class RegularExpressionTools {

    /**
     * 获得正则表达式的match
     *
     * @param regex      正则
     * @param dataString 源字符串
     * @return
     */
    public static Matcher getRegexMatcher(String regex, String dataString) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(dataString);
    }


    public static Matcher getRegexMatcher(String regex, StringBuilder dataString) {
        return getRegexMatcher(regex, dataString.toString());
    }

    public static Matcher getRegexMatcher(String regex, StringBuffer dataString) {
        return getRegexMatcher(regex, dataString.toString());
    }

    /**
     * 字符串是否匹配到正则表达式
     * @param regex 正则
     * @param s 待测字符串
     * @return 匹配成功,则sucess
     */
    public static boolean isMatcher(String regex, String s){
        return Pattern.compile(regex).matcher(s).find();
    }

    /**
     * 判断是否是代数表达式
     * 首先判断表达式中含有∠、△、⊥、∥、▽等几何元素时，则为几何表达式
     * 如果不含上述的几何表达式时，判断表达式中是否含有两个即两个以上连续大写字母的形式，如果有，则为几何表达式
     * 否则，为代数表达式
     */
    public static boolean isAlgebraExpression(String expressString) {
        String geometrysymbol = "(∠|△|⊥|∥|▽)";
        Matcher match = Pattern.compile(geometrysymbol).matcher(expressString);
        if (match.find()) {
            return false;
        } else {
            Matcher matcher = Pattern.compile("([A-Z]('+|_[0-9]+|)){2}").matcher(expressString);
            return !matcher.find();
        }
    }

}
