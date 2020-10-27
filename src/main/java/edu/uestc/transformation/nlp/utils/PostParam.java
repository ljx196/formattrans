package edu.uestc.transformation.nlp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cyq7on
 * @description 请求nlp参数
 * @create 2019/12/2
 **/
public class PostParam {
    private String chinese_type;
    private Content text_json;


    public PostParam(Content text_json) {
        this("1", text_json);
    }

    public PostParam(String chinese_type, Content text_json) {
        this.chinese_type = chinese_type;
        this.text_json = text_json;
    }

    public String getChinese_type() {
        return chinese_type;
    }

    public void setChinese_type(String chinese_type) {
        this.chinese_type = chinese_type;
    }

    public Content getText_json() {
        return text_json;
    }

    public static class Content {
        private String type;
        private String stem;
        private List<String> subStems;
        private List<String> options;
        private List<String> solutions;
        private int instantiatedLevel;
        private String instantiatedCategory;
        private String instantiatedDescription;
        private String questionID;


        public Content(String type, String stem, List<String> subStems, List<String> options, List<String> solutions) {
            this(type, stem, subStems, options);
            if (solutions == null) {
                solutions = new ArrayList<>(0);
            }
            this.solutions = solutions;

        }

        public Content(String type, String stem, List<String> subStems, List<String> options, List<String> solutions,
                        int instantiatedLevel, String instantiatedCategory, String instantiatedDescription) {
            this(type, stem, subStems, options, solutions);
            this.instantiatedLevel = instantiatedLevel;
            this.instantiatedCategory = instantiatedCategory;
            this.instantiatedDescription = instantiatedDescription;

        }
        public Content(String type, String stem, List<String> subStems, List<String> options, List<String> solutions,
                       int instantiatedLevel, String instantiatedCategory, String instantiatedDescription,String questionID) {
            this(type, stem, subStems, options, solutions);
            this.instantiatedLevel = instantiatedLevel;
            this.instantiatedCategory = instantiatedCategory;
            this.instantiatedDescription = instantiatedDescription;
            this.questionID = questionID;

        }

        public Content(String type, String stem, List<String> subStems, List<String> options) {
            this.type = type;
            this.stem = stem;
            if (subStems == null) {
                subStems = new ArrayList<>(0);
            }
            if (options == null) {
                options = new ArrayList<>(0);
            }
            this.subStems = subStems;
            this.options = options;
        }
        public Content(String type, String stem, List<String> subStems) {
            this.type = type;
            this.stem = stem;
            if (subStems == null) {
                subStems = new ArrayList<>(0);
            }
            this.subStems = subStems;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "type='" + type + '\'' +
                    ", stem='" + stem + '\'' +
                    ", subStems=" + subStems +
                    '}';
        }
    }
}
