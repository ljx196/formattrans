package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ZZH on 2016/10/25.
 * 前端随机变量名存储类，给后端提供
 */
public class RandomVarsSave {

    public static int count = 0;
    private static Set<String> saves = new HashSet<>();


    public static boolean addToSave(String addElement) {
        return saves.add(addElement);
    }

    public static boolean addAll(Collection<String> elements) {
        if (null == elements)
            return false;
        return saves.addAll(elements);
    }

    public static void clearSave() {
        saves.clear();
        count = 0;
    }

    public static Set<String> getSaves() {
        return saves;
    }

    public static boolean contains(String var) {
        return saves.contains(var);
    }

    //和drl关键字相同，易混
    public static boolean isContain(String var) {
        return saves.contains(var);
    }

    public static boolean containsRandomVar(String str) {
        for (String var : saves) {
            if (str.contains(var))
                return true;
        }
        return false;
    }

}
