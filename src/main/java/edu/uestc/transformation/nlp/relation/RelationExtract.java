package edu.uestc.transformation.nlp.relation;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import edu.uestc.transformation.nlp.utils.LinuxConnection;
import edu.uestc.transformation.nlp.utils.NlpString2;
import edu.uestc.transformation.nlp.utils.PostParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author: tang
 * @create: 2020/9/22
 * @description:
 */

public class RelationExtract {
    private final static Logger log = LogManager.getLogger(RelationExtract.class);
    private QuestionLabel questionLabel = new QuestionLabel();
    private String json;
    private List<String>triples = new ArrayList<>();//保存题目的所有三元组

    public String getJson() {
        return json;
    }
    private String getTriples(){
        if (triples.isEmpty()){
            return "##";
        }
        String text="";
        for(int i=0;i<triples.size()-1; ++i){
            text = text+triples.get(i);
            text = text+"#";
        }
        text = text+triples.get(triples.size()-1);
        return text;
    }

    //返回题目的所有关系三元组
    public String triples(String stem,List<String>subStems){
        List<String> options = Lists.newArrayList("");
        PostParam postParam = new PostParam(new PostParam.Content("0", stem, subStems,options));
        handleQuestion(postParam);
        return getTriples();
    }
    public String triples(String stem){
        List<String> subStems = Lists.newArrayList("");
        List<String> options = Lists.newArrayList("");
        PostParam postParam = new PostParam(new PostParam.Content("0", stem, subStems,options));
        handleQuestion(postParam);
        return getTriples();

    }


    private void handleQuestion(PostParam postParam) {
        //this.isAnswer = isAnswer;

        // 表明是标注题目中的关系
        postParam.setChinese_type("1");

        // 保存传输的相关数据
        questionLabel.setPostParam(postParam);

        // 通过前端返回的json 转成NlpQuestion 对象
       // NlpQuestion nlpQuestion = null;
        try {
            phraseRelations(questionLabel);//将前端返回的json解析到NlpQuestion对象中并返回
        } catch (Exception e) {
            log.info(e.getMessage());
        }finally {
            // 关闭相关的服务
            closeService();
        }
    }
    private void closeService() {
        // 关闭服务器连接
        LinuxConnection.getInstance().closeSession();
    }

    private void phraseRelations(QuestionLabel questionLabel) throws Exception {
        // 开启前端，先走一遍正常的关系抽取，如果关系没有抽取出来。再进行关系标注
        NlpString2 s = new NlpString2();

        // value 为1代表返回的json 中实体类型为中文名称
        /**
         * getPostParam返回PostParam
         * getNlpJson返回题目的所有实体和关系组成的json
         * */
        String result = s.getNlpJson(questionLabel.getPostParam());

        // 判断返回的结果是不是 json 格式，否则就返回异常
        if (!isJson(result)) {
            throw new Exception("nlp 前端返回的结果有误，请检查\n" + result);
        }
      //  System.out.println(s.toPrettyFormat(result));
        json = result;
        // 将前端返回的 json 字符串转换成对象
        //parseObkect会根据json里面的关键字和对象中的属性名做一一对应，对对象进行初始化。
        //NlpQuestion nq = JSONObject.parseObject(result,NlpQuestion.class);
        //return nq;
        NlpQuestion nlpQuestion = JSONObject.parseObject(result, NlpQuestion.class);
        List<RelationLtp> relations = obtainRelationsByNlp(nlpQuestion);
        if(relations.size()>0) {
            for (RelationLtp relation : relations) {
                triples.add(relation.showTriple());
            }
        }
    }

    private List<RelationLtp> obtainRelationsByNlp(NlpQuestion question) {

        // 题干中提取到的关系
        List<RelationLtp> relations = question.getStemRelations();
        // 将小问提取到的关系和 题干关系进行合并
        for (SubStemRelation s : question.getSubStemRelations()) {
            // 将每个小问中提取的关系，整合到一起
            if (s.getRelations() != null) {
                relations.addAll(s.getRelations());
            }
        }


        if (relations.size() > 0) {
            for (RelationLtp relationLtp : relations) {
                // 因为前端返回的是json字段，不是 relationLtp 对象，所以需要手动根据相关字段创建实体对象
                relationLtp.generateEntity();
            }
        }

        List<RelationLtp> newLists = new ArrayList<>();
        HashSet<RelationLtp> set = new HashSet<>();
        for (RelationLtp r : relations) {
            if (set.add(r)) {
                newLists.add(r);
            }

        }

        // 将关系保存
        question.setRelations(newLists);
        return newLists;
    }





    /**
     * 检查返回的结果是不是json 字符串
     *
     * @param result 带检查的字符串
     * @return 返回检测的结果，如果返回 true 说明前端返回的json 字符串是正确的
     */
    private boolean isJson(String result) {
        try {
            JSONObject.parseObject(result);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(result);
            } catch (JSONException ex1) {
                log.info("nlp 前端返回的结果有误，请检查");
                return false;
            }
        }
        return true;
    }


}
