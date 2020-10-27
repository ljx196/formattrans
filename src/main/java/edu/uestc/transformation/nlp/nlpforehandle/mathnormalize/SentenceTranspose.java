package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceTranspose {

    //Q_20207252198470这个题目处理逻辑存在bug
    private static List<String> splitSubStem(String subStem) {
        List<String> conlusions = new ArrayList<>();
        Pattern pattern = Pattern.compile("^.*?何值时,.*?(?<!互)为(?!顶点)(?<condition>.*?)$");
        Matcher matcher = pattern.matcher(subStem);
        if (!matcher.find()) {
            conlusions.add(subStem);
            return conlusions;
        }
        String condition = matcher.group("condition");
        String basic = subStem.substring(0, subStem.length() - condition.length());
        int condIndex = basic.lastIndexOf(',');
        String conclusion = basic.substring(0, condIndex);
        conclusion = conclusion.replaceAll("^当", "求").replaceAll("时$", "").replaceAll("分别", "");
        String condBasic = basic.substring(condIndex + 1);
        condBasic.replaceAll("使得", "");
        String[] conds = condition.split(",|、");
        for (String cond : conds) {
            conlusions.add(condBasic + cond + "," + conclusion);
        }
        return conlusions;
    }

    // 证明：若，则情况
    // 证明：对，都有
    // 移花接木
   /* private static String foreHandleString(String answerString, GlobalInformation globalInformation) {

        //取出括号内内容和离散属性的提取
        answerString = SplitComplex.processChain(answerString);

        if ((answerString.contains("#%#证明:若") || answerString.contains("证明:若")) && (answerString.contains(",则"))) {
            answerString = answerString.replaceAll("证明:", "");
            answerString = answerString.replaceAll(",则", ",证明:");
        } else if ((answerString.contains("#%#证明:对") || answerString.contains("证明:对")) && (answerString.contains(",都有")) && (!answerString.contains(",使得"))) {
            answerString = answerString.replaceAll("证明:对(于|)", "");
            answerString = answerString.replaceAll(",都有", ",证明:");
//        }else if ((answerString.contains(",则满足"))&& answerString.contains("取值范围"))
        } else if ((answerString.contains("证明:对")) && (answerString.contains(",都有")) && (answerString.contains(",使得"))) {
            answerString = answerString.replaceAll("证明:对", "");
            answerString = answerString.replaceAll(",都有", ",");
            answerString = answerString.replaceAll(",使得", ",证明:");
        } else if ((answerString.contains("#%#证明:对") || answerString.contains("证明:对")) && (answerString.contains(",有"))) {
            answerString = answerString.replaceAll("证明:对", "");
            answerString = answerString.replaceAll(",有", ",证明:");
        } else if ((answerString.contains("#%#证明:对任意") || answerString.contains("证明:对任意")) && (answerString.contains(",恒有"))) {
            answerString = answerString.replaceAll("证明:对任意", "");
            answerString = answerString.replaceAll(",恒有", ",证明:");
        } else if ((answerString.contains("#%#证明:对于任意") || answerString.contains("证明:对于任意")) && (answerString.contains(",恒有"))) {
            answerString = answerString.replaceAll("证明:对于任意", "");
            answerString = answerString.replaceAll(",恒有", ",证明:");
        } else if ((answerString.contains("#%#证明:对于任意") || answerString.contains("证明:对于任意") || answerString.contains("证明:任意")) && answerString.matches(".*?恒成立(,|)")) {
            answerString = answerString.replaceAll("证明:对于任意|证明:任意", "");
            String test = NewAnalysisQuestionsStems.getNERChunkStrWithoutOAndBlank(MergeTag.Merge(answerString));
            int lastIndex = test.lastIndexOf(',');
            test = test.substring(0, lastIndex) + ",证明:" + test.substring(lastIndex + 1, test.length());
//            test = test.replaceFirst(" , ", ",证明:");
            answerString = UtilFunction.deleteTags(test);

        } else if ((answerString.contains(",则满足")) && answerString.matches(".*?的[a-z]取值范围(是|为).*?")) {
            answerString = answerString.replaceAll(",则满足", ",");
            answerString = answerString.replaceAll("的(?=([a-z](取值范围)(是|为)))", ",则");
        } else if ((answerString.contains("#%#证明:当") || answerString.contains("证明:当")) && (answerString.contains(",恒有"))) {
            answerString = answerString.replace("证明:当", "");
            answerString = answerString.replace(",恒有", ",证明:");
        } else if ((answerString.contains("#%#求证:当") || answerString.contains("求证:当")) && (answerString.contains("时"))) {
            answerString = answerString.replace("求证:当", "");
            answerString = answerString.replace("时,", ",证明:");
        } else if (answerString.matches(".*?判断.+?是否.+?")) {
            answerString = answerString.replace("判断", "证明:");
            answerString = answerString.replace("是否", "");
        }
//        else if (answerString.matches(".*?证明存在.+?使得.+?")){
//            answerString = answerString.replace("证明存在","");
//            answerString = answerString.replace("使得",",证明");
//        }
        else if (answerString.matches(".*?是否存在常数.*?,使得.*?")) {
            answerString = answerString.replace("是否存在", "");
            answerString = answerString.replace(",使得", ",");
        } else if (answerString.matches(".*?是否存在(?!点).*?,使得.*,求.*点?.*")) {
            answerString = answerString.replace("是否存在", "");
        } else if (answerString.matches(".*?是否(存在)(?!点)[^点]*?,使(得|)[^,].*?")) {
            answerString = answerString.replace("是否存在", "求");
            answerString = answerString.replace("使得", "则");
        } else if (answerString.matches(".*?是否(存在)(?!点)[^点]*?,使(得|).*?")) {
            answerString = answerString.replace("是否存在", "");
            answerString = answerString.replace(",使得", ",证明:");
        } else if (answerString.contains("证明:存在") && answerString.contains("使得")) {
            answerString = answerString.replace("证明:存在", "");
            answerString = answerString.replace(",使得", ",证明:").replace("使得", "证明:");
        } else if (answerString.matches(".*?证明:除(.)*之外,.*?")) {
            answerString = answerString.replaceAll("除(.)*之外,", "");
        } else if (answerString.matches("求使得.*?成立的.*?取值范围.*?")) {
            answerString = answerString.replace("求", "");
            answerString = answerString.replace("成立的", ",求");
        } else if (answerString.matches(".*?证明:(.)*,且.*?")) {
            answerString = answerString.replaceAll("(?<!有)且(?!仅有)", "证明");
        } else if (answerString.contains("求使得") && answerString.contains("成立n")) {
            answerString = answerString.replace("求使得", "");
            answerString = answerString.replace("成立", ",求");
        } else if (answerString.contains("将n用m表示") || answerString.contains("将n表示为m的函数")) {
            answerString = answerString.replace("请将n表示为m的函数", "m为常数,将n表示为m的函数");
        } else if (answerString.contains("前n项和公式")) {
            answerString = answerString.replace("前n项和公式", "前n项和");
        } else if (answerString.contains("前n项的和")) {
            answerString = answerString.replace("前n项的和", "前n项和");
        } else if (answerString.contains("前n项的和的公式")) {
            answerString = answerString.replace("前n项的和的公式", "前n项和");
        } else if (RegularExpressionTools.isMatcher("(证明|求(出|)|指出|确定|回答)(:|)当", answerString)) {
            answerString = dealProveWhen(answerString);
        }else if (answerString.contains("图象相切,则切点坐标为")){
            answerString = answerString.replace("图象相切,则切点坐标为","图象相切于点P,则P点坐标为");
        }

        //在三角形ABC中，a=3，把a替换成BC。11.29与张宏峰商量后取消
//        if (answerString.contains("△ABC") && answerString.contains("a,b,c") &&answerString.contains("A,B,C")) {
//            globalInformation.setIsTriangle(true);
//            answerString = answerString.replaceAll("(?<!t)a(?!(n|rc))", "BC").replaceAll("b", "AC").replaceAll("(?<!ar)c(?!o(s|t))", "AB");
//        } else if (globalInformation.isTriangle()) {
//            answerString = answerString.replaceAll("(?<!t)a(?!(n|rc))", "BC").replaceAll("b", "AC").replaceAll("(?<!ar)c(?!o(s|t))", "AB");
//        }
        return answerString;
    }*/

    /**
     * 将"求解集"改为"求变量的取值范围"
     */
//    private static String replaceSloutionSet2VarRank(String origin) {
//        String result = "";
//
//        String[] sentences = origin.split(",");
//        for (String sentence : sentences) {
//            if (!sentence.contains("方程") && sentence.contains("的解集") && sentence.contains("x")) {
//
//                String regex = "(<head>求|则|为|是|)(?<express>[^求|则|为|是]*?)(的解集)";
//
//                Matcher matcher = RegularExpressionTools.getRegexMatcher(regex, sentence);
//                if (matcher.find()) {
//                    sentence = matcher.group("express") + "," + sentence.replace(matcher.group("express"), "").replace("的解集", "x的取值范围");
//                }
//            }
//            result += sentence + ",";
//        }
//        return result.substring(0, result.length() - 1);
//    }

    /**
     * 将" 证明:当&&&&时,&&&"变为"当&&&&时,证明:&&&"
     */
    private static String dealProveWhen(String sentence) {
        Matcher matcher = RegularExpressionTools.getRegexMatcher("(?<prove>(证明|求(出|)|指出|确定|回答)(:|))当[^何时什么]+?时,", sentence);
        if (matcher.find()) {
            String proveHead = matcher.group("prove");
            StringBuilder stringBuilder = new StringBuilder(sentence.replaceAll(proveHead + "(?=当)", ""));
            stringBuilder = stringBuilder.insert(matcher.end() - proveHead.length(), proveHead);
            return stringBuilder.toString();
        }
        return sentence;
    }

}
