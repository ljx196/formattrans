package edu.uestc.transformation.nlp.relation;

import edu.uestc.transformation.nlp.utils.PostParam;

import java.util.ArrayList;

import java.util.List;


/**
 * @author: yyWang
 * @create: 2019/10/17
 * @description: 需要题目关系标注的实体类
 */

public class QuestionLabel {

    // 需要标注的题目
    private String questionText;

    // 将题目分割成短句分别进行处理
    private List<String> phrase;

    // 向 http 传输数据相关的参数
    private PostParam postParam;

    // 以及查询的关系
   // private Set<KnowledgeSearch> knowledgeSearch;

    // 短句中的关系对象
    private List<NlpQuestion> phraseRelations;

    public QuestionLabel() {
        phraseRelations = new ArrayList<>();
      //  knowledgeSearch = new HashSet<>();
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getPhrase() {
        return phrase;
    }

   // public Set<KnowledgeSearch> getKnowledgeSearch() {
   //     return knowledgeSearch;
   // }

//    public void setKnowledgeSearch(Set<KnowledgeSearch> knowledgeSearch) {
//        this.knowledgeSearch.addAll(knowledgeSearch);
//    }

    public void setPhrase(List<String> phrase) {
        this.phrase = phrase;
    }

    public List<NlpQuestion> getPhraseRelations() {
        return phraseRelations;
    }

    public PostParam getPostParam() {
        return postParam;
    }

    public void setPostParam(PostParam postParam) {
        this.postParam = postParam;
    }

    public void setPhraseRelations(NlpQuestion question) {
        this.phraseRelations.add(question);
    }
}
