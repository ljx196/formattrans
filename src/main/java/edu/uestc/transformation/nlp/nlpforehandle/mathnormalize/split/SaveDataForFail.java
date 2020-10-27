package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZH on 2017/7/3.
 * 此类用于进行返回匹配失败的句子的前面的句子.
 * 只能生成一个实例
 */
public class SaveDataForFail {

    private List<String> stem = new ArrayList<>();
    private List<List<String>> subStem = new ArrayList<>();

    private static SaveDataForFail instance;

    public static SaveDataForFail getInstance() {
        if (instance == null) {
            instance = new SaveDataForFail();
        }
        return instance;
    }

    private SaveDataForFail() {

    }

    public List<String> getStem() {
        return stem;
    }

    public void setStem(List<String> stem) {
        this.stem = stem;
    }

    public List<List<String>> getSubStem() {
        return subStem;
    }

    public void setSubStem(List<List<String>> subStem) {
        this.subStem = subStem;
    }

    /**
     *
     * @param checkedString 要检测的句子
     * @return 返回失败句子前面的句子
     */
    public static List<String> checkAndFindBefore(String checkedString) {
        List<String> resultString = new ArrayList<>();
//        checkedString = checkedString.replaceAll("(failStem：|failSubStem\\d：)", "").replaceAll(",( |)$", "").trim();
        checkedString = checkedString.substring(0, checkedString.length()-1);   //去处句子后面的空格
        int i;
        i = instance.getStem().indexOf(checkedString);
        if (i > 3) {
            resultString.add(instance.getStem().get(i - 4));
            resultString.add(instance.getStem().get(i - 3));
            resultString.add(instance.getStem().get(i - 2));
            resultString.add(instance.getStem().get(i - 1));
            return resultString;
        } else if (i > 2) {
            resultString.add(instance.getStem().get(i - 3));
            resultString.add(instance.getStem().get(i - 2));
            resultString.add(instance.getStem().get(i - 1));
            return resultString;
        } else if (i > 1) {
            resultString.add(instance.getStem().get(i - 2));
            resultString.add(instance.getStem().get(i - 1));
            return resultString;
        } else if (i == 1) {
            resultString.add(instance.getStem().get(0));
        }
        for (List<String> strings : instance.getSubStem()) {
            i = strings.indexOf(checkedString);
            if (i > 3) {
                resultString.add(strings.get(i - 4));
                resultString.add(strings.get(i - 3));
                resultString.add(strings.get(i - 2));
                resultString.add(strings.get(i - 1));
                return resultString;
            } else if (i > 2) {
                resultString.add(strings.get(i - 3));
                resultString.add(strings.get(i - 2));
                resultString.add(strings.get(i - 1));
                return resultString;
            } else if (i > 1) {
                resultString.add(strings.get(i - 2));
                resultString.add(strings.get(i - 1));
                return resultString;
            } else if (i == 1) {
                resultString.add(strings.get(0));
            }
        }
        return resultString;
    }
}
