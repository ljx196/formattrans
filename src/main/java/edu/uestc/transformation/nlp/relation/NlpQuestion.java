package edu.uestc.transformation.nlp.relation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/10/17
 * @description: 将短句送到nlp前端进行识别
 */

public class NlpQuestion {

    /**
     * nlp 短句
     */
    private String commonText;

    /**
     * 题目分割成短句
     */
    private List<String> phrases;

    /**
     * 题干包含的关系
     */
    private List<RelationLtp> stemRelations;

    /**
     * 从文件中标注短句，使用的字段，
     * 为替换前的原句
     * 如 求二面角$D-AB_1-E$的余弦值rsb
     */
    private String originSentence;


    /**
     * 从文件中标注短句，使用的字段，
     * shortSentence: 短句使用26个字母替换实体后的短句
     * 如 求二面角N的余弦值rsb
     */
    private String shortSentence;

    // 小问包含的关系
    private List<SubStemRelation> subStemRelations;

    private List<List<RelationLtp>> solutions;

    /**
     * 前端替换后的题目文本，已经实体引入后的实体。
     */
    private String fakeText;

    /**
     * 短句中实体替换后的结果
     */
    private String shortSentenceReplaced;

    /**
     * 短句包含的实体集合
     */
    private ArrayList<Entity> entities;

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    /**
     * 已提取到的关系
     */
    private List<RelationLtp> relations;

    public List<RelationLtp> getStemRelations() {
        return stemRelations;
    }

    public void setStemRelations(List<RelationLtp> stemRelations) {
        this.stemRelations = stemRelations;
    }

    public List<SubStemRelation> getSubStemRelations() {
        return subStemRelations;
    }

    public String getOriginSentence() {
        return originSentence;
    }

    public void setOriginSentence(String originSentence) {
        this.originSentence = originSentence;
    }

    public String getShortSentence() {
        return shortSentence;
    }

    public void setShortSentence(String shortSentence) {
        this.shortSentence = shortSentence;
    }

    public void setSubStemRelations(List<SubStemRelation> subStemRelations) {
        this.subStemRelations = subStemRelations;
    }

    public List<List<RelationLtp>> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<List<RelationLtp>> solutions) {
        this.solutions = solutions;
    }

    public String getCommonText() {
        if (commonText == null) {
            commonText = originSentence + " \n[ " + shortSentence + " ]";
        }
        return commonText;

    }

    public String getShortSentenceReplaced() {
        return shortSentenceReplaced;
    }

    public void setShortSentenceReplaced(String shortSentenceReplaced) {
        this.shortSentenceReplaced = shortSentenceReplaced;
    }

    public String getFakeText() {
        return fakeText;
    }

    public List<String> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }

    public void setFakeText(String fakeText) {
        this.fakeText = fakeText;
    }

    public void setCommonText(String commonText) {
        this.commonText = commonText;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public List<RelationLtp> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationLtp> relations) {
        this.relations = relations;
    }
}
