package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

//import com.tsinghuabigdata.regex.GrammarCheckFunc;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.GrammarCheckFunc;
/**
 * 分析除号后面的表达式，确定分母的范围（即添加括号，把分母的范围包含在内）
 *
 * @author B1-101
 * @Modified by lizo
 */
public class AdJoinParse {
    private static final int prefix = 1;


    /**
     * 分析除号后面接的分母的范围
     * (后面的项只能用乘号连接则可以认为在分母上)
     *
     * @param expr
     * @param startIndex
     * @return
     */
    public int analyseDenominator(String expr, int startIndex) {
        int endIndex;
        int bracketLevel = 0;
        for (endIndex = startIndex + 1; endIndex < expr.length(); endIndex++) {
            char character = expr.charAt(endIndex);
            if (GrammarCheckFunc.isFrontBracket(character)) {
                bracketLevel++;
                continue;
            }
            //终止条件1：遇见），深度为0
            if (GrammarCheckFunc.isBackbracket(character)) {
                if (bracketLevel == 0) {
                    return endIndex;
                } else {
                    bracketLevel--;
                    continue;
                }
            }
            //终止条件2：遇到+,-，并且括号的深度为0

            if (bracketLevel == 0 &&
                    (character == ',' || character == '+' || character == '-' || character == '*' || character == '/' || character == ')')) {
                return endIndex;
            }

            //终止条件3：遇到=，深度为0
            if (character == '=' || character == '$' && bracketLevel == 0) {
                return endIndex;
            }

//			if(isLegalDeanominator(character))
//			{
//				if(GrammarCheckFunc.isFrontBracket(character))
//				{
//					bracket = true;
//				}else if(GrammarCheckFunc.isBackbracket(character))
//				{
//					bracket = false;
//				}
//			}else if(GrammarCheckFunc.isExpressionOperator(character)
//					&& true == bracket)
//			{
//			}else{
//				break;
//			}
        }
        return endIndex;
    }

    /**
     * @param start 开始匹配的位置
     * @return
     */
    public String parse(String str, int start, StringBuffer tagStr) {
        int endIndex = this.analyseDenominator(str, start);
        if (endIndex - start - prefix >= 2) {
            String tempString = str.substring(start, endIndex);
            String newString = "/(" + tempString.substring(1) + ")";
            str = str.replace(tempString, newString);
            tagStr.append("a");    //标记用的
        }
        return str;
    }
}
