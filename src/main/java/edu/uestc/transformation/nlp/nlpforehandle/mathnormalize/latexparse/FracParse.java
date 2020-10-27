package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.GrammarCheckFunc;

public class FracParse {
    private static final Logger LOG = LogManager.getLogger(FracParse.class);

    /**
     * latex 分式解析函数，每次只能替换一次，其余部分进行下一次的循环
     *
     * @param str 匹配分式的串,
     * @return 替换后的字符串
     */
    public String fracParse(String str, int start, StringBuffer replacestr) {
        String newString = getPrasedSubStr(str, start);

        try {
            if (newString != null) {
                int[] indexs = getNeededReplaceIndex(replacestr);
                replacestr.replace(indexs[0], indexs[1], newString);            //只针对指定位置的\\frac{}{}形式的串进行替换
            }
        } catch (Exception e) {
            LOG.error("something is wrong when parsing latex fraction["
                    + str + "," + start + "," + replacestr.toString() + "] : " + e.getMessage());
        }
        return replacestr.toString();
    }


    /**
     * 对原始串进行解析，找出\\frac{}{}形式（含带分数）
     */
    private String getPrasedSubStr(String str, int start) {

        int improperIndex = this.improperFrac(str, start - 1);        //获取分式开始位置（实际可能含带分数的首部）
        String improperNumber = null;
        if (improperIndex != start) {
            improperNumber = str.substring(improperIndex, start);    //获取带分数的首部
        }

        int front;
        int end;
        int[] indexs = getParts(str, start);
        front = indexs[0];
        end = indexs[1];

        if (start != -1 && end != -1 && start < end + 1) {
            String molecular = str.substring(start + 6, front);
            String denominator = str.substring(front + 2, end);
            if (improperNumber != null) {    //带分式直接将分子计算出来，不能转换为数字则不处理
                try {
                    int impNum = Integer.parseInt(improperNumber.replace(
                            "{", "").replace("}", ""));
                    int denNum = Integer.parseInt(denominator.replace(
                            "{", "").replace("}", ""));
                    int molNum = Integer.parseInt(molecular.replace(
                            "{", "").replace("}", ""));
                    molecular = molNum + (denNum * impNum) + "";
                } catch (NumberFormatException e) {
                    return "(" + improperNumber + "*" + "(" + molecular + ")/(" + denominator + "))";
                }
            }
            return "((" + molecular + ")/(" + denominator + "))";
        }
        return null;
    }


    /**
     * 同步对replaceString 进行解析，对需要提换的部分进行定位
     */
    private int[] getNeededReplaceIndex(StringBuffer replaceString) {

        int[] result = new int[2];
        int start = replaceString.indexOf("\\frac");
        String replaceStr = replaceString.toString();
        int improperIndex = this.improperFrac(replaceStr, start - 1);        //获取分式开始位置（实际可能含带分数的首部）
        int[] indexs = getParts(replaceStr, start);
        result[0] = improperIndex;
        result[1] = indexs[1] + 1;
        return result;
    }


    /**
     * 对{}{}进行定位
     */
    private int[] getParts(String str, int start) {

        int indexs[] = new int[2];
        boolean frontpart = true;
        int loop = 1;
        /*获取分式的分子和分母*/
        for (int i = start + 6; i < str.length(); i++) {
            if (str.charAt(i) == '}') {
                loop--;
            }
            if (str.charAt(i) == '{') {
                loop++;
            }
            if (loop == 0 && frontpart) {
                indexs[0] = i;                    //分子部分的结束位置
                loop = 1;
                i++;
                frontpart = false;
            }
            if (loop == 0) {
                indexs[1] = i;                    //分母部分的结束位置
                break;
            }
        }
        return indexs;
    }

    /**
     * 处理分式前面的表示假分式
     */
    private int improperFrac(String str, int start) {
        while (start >= 0) {
            char c = str.charAt(start);
            if (!GrammarCheckFunc.isNumber(c)) {
                break;
            }
            start--;
        }
        return start + 1;
    }


}
