package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;

import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import edu.uestc.transformation.nlp.nlpforehandle.jsontorelationtools.UtilFunction;
import edu.uestc.transformation.nlp.nlpforehandle.util.NLUConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 同义词库的处理
 *
 * @author B1-101
 */
public class SimilarWord {
    private static final Logger LOG = LogFactory.getLogger(SimilarWord.class);
    private static List<SimilarWordData> similarWords = new ArrayList<>();
    private static Date modifytime = null;//修改时间
    private final static String path = "similarword_local.xml";
    private static List<String> allSimilarWords = new ArrayList<>();
    private static Map<String, List<String>> allSimilarWordMap = new HashMap<>();

    private static SimilarWord instance;  //Singleton instance

    public static SimilarWord getInstance() {
        if (instance == null) {
            synchronized (SimilarWord.class) {
                if (instance == null) {
                    instance = new SimilarWord();
                }
            }
        }
        return instance;
    }

    static {
        try {
            initFromFile(path);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 从文件初始化数据
     *
     * @param fileName 文件名
     * @throws Exception
     */
    private static void initFromFile(String fileName) throws Exception {
        Document doc = NLUConfiguration.readXml(fileName);
        Element root = doc.getRootElement();
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date datebasetime = format.parse(root.getAttributeValue("modifytime"));
        if (modifytime != null && datebasetime.equals(modifytime)) {
            return;
        }

        similarWords.clear();
        List rules = root.getChildren("similar");
        for (Object rule1 : rules) {
            SimilarWordData swdata = new SimilarWord().new SimilarWordData();
            Element rule = (Element) rule1;
            String replaceWord = rule.getAttributeValue("tag").replace("＜", "<");
            swdata.setReplace(replaceWord);
            allSimilarWords.add(replaceWord);//add for sxx
            List similarList = rule.getChildren("value");
            List<String> originWords = new ArrayList<>();
            for (Object obj : similarList) {
                Element e_obj = (Element) obj;
                String originWord = e_obj.getText().replace("《", "<");
                swdata.addSimilar(originWord);
                originWords.add(originWord);
                allSimilarWords.add(originWord);
            }
            allSimilarWordMap.put(replaceWord, originWords);
            similarWords.add(swdata);
        }
        modifytime = datebasetime;
    }

    public static List<String> getAllSimilarWords() {
        return allSimilarWords;
    }

    public static void setAllSimilarWords(List<String> allSimilarWords) {
        SimilarWord.allSimilarWords = allSimilarWords;
    }

    public static Map<String, List<String>> getAllSimilarWordMap() {
        return allSimilarWordMap;
    }

    public static void setAllSimilarWordMap(Map<String, List<String>> allSimilarWordMap) {
        SimilarWord.allSimilarWordMap = allSimilarWordMap;
    }

    /**
     * 处理同义词的对外接口函数
     */
    public String similarWord(String orignString) {
        if (StringUtils.isEmpty(orignString)) {
            return orignString;
        }
        String tempStr = orignString;
        for (SimilarWordData word : similarWords) {
            List<String> similarList = word.getSimilar();
            for (String similar : similarList) {
                if (similar != null && word.getReplace() != null) {
                    tempStr = tempStr.replaceAll(similar, word.getReplace());
                }
            }
        }
//        tempStr = tempStr.replaceAll("\\.(\\s|)\\.(\\s|)\\.(\\s|)", "###");

        /**
         * todo: 这个替换暂时去掉
         * tempStr = tempStr.replaceAll("((?<![0-9])\\.|\\.(?![0-9]))", ",").replaceAll("###", "...");
         */

        tempStr = tempStr.replaceAll("(?<=(,|^))满足(?![\\u4E00-\\u9FFF])", "");
//        tempStr = tempStr.replaceAll("(?<!([\\u4E00-\\u9FFF]|,))满足((不|)等式|方程|)(?![\\u4E00-\\u9FFF])", ",");
//        tempStr = tempStr.replaceAll("(?<=,)(并|)且满足", "");
//        tempStr = tempStr.replaceAll("(?<!,)(并|)且满足", ",");
        tempStr = tempStr.replaceAll("n(\\s|)=(\\s|)1,2(,(3(,(4(,|)|)|)|)|)(\\s|)\\.\\.\\.", "n为正整数.");
        tempStr = tempStr.replaceAll("n(\\s|)=(\\s|)2(,(3(,(4(,|)|)|)|)|)(\\s|)\\.\\.\\.", "n≥2");
//        tempStr = tempStr.replaceAll("也(是|为)", "是");
        tempStr = tempStr.replaceAll("\\\\(\\(|)\\{(?=\\\\begin\\{cases\\})", "\\$ ");
        tempStr = tempStr.replaceAll("(?<=\\\\end\\{cases\\})\\}(,|)\\\\(\\)|)", " \\$");
        tempStr = tempStr.replaceAll("\\\\\\((?=\\\\begin\\{cases\\})", "\\$ ");
        tempStr = tempStr.replaceAll("(?<=\\\\end\\{cases\\})(,|)\\\\(\\)|)", " \\$");
        tempStr = tempStr.replace("的前项和", "的前n项和");
        tempStr = tempStr.replace("最大值,最小值", "最大值和最小值");
        tempStr = tempStr.replace("最小值,最大值", "最小值和最大值");
        tempStr = tempStr.replace("极大值,极小值", "极大值和极小值");
        tempStr = tempStr.replace("极小值,极大值", "极小值和极大值");
        tempStr = tempStr.replace("项之和", "项和");
        tempStr = tempStr.replace("分解因式", "因式分解");
        tempStr = tempStr.replace("⇒", "得");
        tempStr = tempStr.replace("x,y轴", "x轴,y轴");
        tempStr = tempStr.replace("_${cm}^3$", "_").replace("_$cm^{3}$", "_");
//        tempStr = tempStr.replace("</br>", "#%#");
        tempStr = tempStr.replace("说出", "写出");
        tempStr = tempStr.replace("n=1,2,***", "n为正整数");
     //   tempStr = tempStr.replace("零", "0");
        tempStr = tempStr.replace("公差不为零", "公差d不为0");

        tempStr = tempStr.replaceAll("(?<!\\{)▱", "平行四边形");
        tempStr = tempStr.replaceAll("(等差|等比)?数列[$](.*)[$]的各项均为正数", " $2为正项$1数列");
        //(等差|等比)?数列[$](.*)[$]的各项均为正数  $2为正项$1数列
        //特殊的latex处理
        tempStr = dealWith(tempStr);
//        tempStr = makeMacket(tempStr);
        tempStr = tempStr.replaceAll("离心率(\\$|)e=(\\$|)\\(\\)", "离心率e为()");//选择题后面的情况
        tempStr = UtilFunction.dealOrNum(tempStr);
        return tempStr;
    }

//    /**
//     * 去除多余的外层括号
//     *
//     * @author tsb
//     */
//    private String makeMacket(String tempStr) {
//        Pattern pattern = Pattern.compile("(\\$\\{)(?=\\\\begin)(.*^\\$)(?<=end\\{cases\\})(\\}\\$)");
//        Matcher matcher = pattern.matcher(tempStr);
//        StringBuilder stringBuilder = new StringBuilder();
//        int pos = 0;
//        if (matcher.find()) {
//            int start = matcher.start();
//            int end = matcher.end();
//            String str = matcher.group();
//            stringBuilder.append(tempStr.substring(pos, start));
//            stringBuilder.append("$").append(str.substring(2, str.length() - 2)).append("$");
//            pos = end;
//        }
//        stringBuilder.append(tempStr.substring(pos, tempStr.length()));
//        return stringBuilder.toString();
//    }

    /**
     * 同义词结构数据
     *
     * @author wangyong
     */
    public class SimilarWordData {
        private String replace;
        private List<String> similar = new ArrayList<>();

        public String getReplace() {
            return replace;
        }

        public void setReplace(String replace) {
            this.replace = replace;
        }

        public List<String> getSimilar() {
            return similar;
        }

        public void addSimilar(String similarword) {
            this.similar.add(similarword);
        }

        @Override
        public String toString() {
            return "SimilarWordData{" +
                    "replace='" + replace + '\'' +
                    ", similar=" + similar +
                    '}';
        }
    }

    //特殊的latex处理
    public String dealWith(String stem) {
        //对log的处理
        stem = stem.replace("{log}", "log");
        Pattern pattern = Pattern.compile("(\\{log_\\{(?<key0>[0-9a-z\\+\\-\\*/\\.]+)\\}\\})|(lo\\{\\{g\\}_\\{(?<key1>[0-9a-z\\+\\-\\*/\\.]+)\\}\\})|(l[ng](?<key2>[0-9a-z]+))|(\\{log_(?<key3>[0-9a-z]+)\\})");
        Matcher matcher = pattern.matcher(stem);
        while (matcher.find()) {
            String key = "", flag;
            int i = 0;
            while (key == null || key.equals("")) {
                flag = "key" + i++;
                key = matcher.group(flag);
            }
            stem = stem.replaceAll("(\\{log_\\{" + key + "\\}\\})|(lo\\{\\{g\\}_\\{[0-9a-z\\+\\-\\*/\\.]\\}\\})|(\\{log_" + key + "\\})", "log_{" + key + "}");
            stem = stem.replaceAll("(l[n,g])" + key, "$1(" + key + ")");
        }
        //f'(x),f^2(x)的处理
        stem = stem.replace("{{f}^{'}}", "f'").replace("{{f}^{2}}{x}", "(f(x))^2").replace("{{f}^{2}}(x)", "(f(x))^2");
        //对求和符号的处理,     暂时这么处理
        stem = stem.replace("\\sum_{k=1}^{2n}", "∑#k=1#n#");

        return stem;
    }
}
