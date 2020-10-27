package edu.uestc.transformation.nlp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * @author cyq7on
 * @description 重构了接口协议
 * @create 2019/11/29
 * @see <a href="http://121.48.165.44:4000/mathnlp/transmission-format.html?tdsourcetag=s_pctim_aiomsg"/>
 **/

public class NlpString2 {


    /**
     * @param postParam 请求参数
     * @return nlp结果
     * @see <a href="http://121.48.165.44:4000/mathnlp/transmission-format.html?tdsourcetag=s_pctim_aiomsg"/>
     */
    public String getNlpJson(PostParam postParam) {

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = null;
        String url = "http://aifuture.uestc.cn:7776/nlp_api";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            List<NameValuePair> paramList = new ArrayList<>();

            String json = new GsonBuilder().disableHtmlEscaping().create().toJson(postParam.getText_json());

            // 将题目转为 LaTeX 形式。
          LatexConvert latex = new LatexConvert();
            // 转化为LaTeX的字符串。
           json = latex.jsonToLatex(json);
            paramList.add(new BasicNameValuePair("text_json", json));
            paramList.add(new BasicNameValuePair("chinese_type", postParam.getChinese_type()));
            paramList.add(new BasicNameValuePair("need_relation", "0"));
            // 模拟表单
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 将json 字符串格式进行美化，便于控制台查看
     *
     * @param json 待格式化的json 字符串
     * @return 返回格式化后的json 格式字符串
     */
    public String toPrettyFormat(String json) {
        JSONObject object = JSONObject.parseObject(json);
        return JSON.toJSONString(object,
                SerializerFeature.PrettyFormat,
                SerializerFeature.MapSortField);
    }

    /**
     * 将 nlp 返回的 json 字符串写入到结果中。
     *
     * @param filePath 结果保存的文件路径
     * @param json     需要提取的文本
     * @param isAppend 文件写入方式，是否追加
     */
    public void soleResult(String filePath, String json, boolean isAppend) {
        TextOperation textOperation = new TextOperation();
        // 第一个参数是将文本提取的结果写入到文件的路径，第二个参数是否以追加的方式写入文本
        textOperation.writePath(filePath, isAppend);
        textOperation.writeLines(json);
        out.println("结果保存到文件成功");
    }

    public static void main(String[] args) {
//
      // String path = "F:\\DevelopTools\\mathematics\\mathematics-reasoning\\src\\main\\resources\\rule\\analyticalGeometry\\直线截抛物线.json";
        // 题干
        String stem = "集合{（x，y）|xy≤0，x，y∈R}中还包括实数轴上的点．";
       // String stem = "双曲线 $\\frac{{{x}^{2}}}{{{a}^{2}}}-\\frac{{{y}^{2}}}{{{b}^{2}}}=1\\left( a>0,b>0 \\right)$的右焦点为$F(c,0)$";
        List<String> subStems = Lists.newArrayList("");
        // 选择题的选项
        List<String> options = Lists.newArrayList("");

        // 小问

        // 题目的解题过程也就是答案
        List<String> solutions = Lists.newArrayList("");

        // 数据传输的格式，具体要求看文档,
        // type:0,实例化；*对于实例化时，将条件写在题干里，结论写在小问里*
        // 1，选择题；
        // 2，填空题；
        // 3，大题等题型
        // 在测实例化的时候,进行赋值，表示实例化的优先级，数字越大优先级越高，只有type=0返回的json才有下面3个字段，
        // 其它值就没有

        //以下三个字段只针对实例化，即type为0时使用
        // 实例化必填，实例化优先级，0-1000，0最高
        int instantiatedLevel = 1;

        // 实例化必填，类别数，公用0，函数1，向量2，数列3，解析几何4，复数5，平面几何6，立体几何7
        String instantiatedCategory = "4";

        // 实例化必填，简单描述该实例化实现的功能
        String instantiatedDescription = "直线截圆弦长";

        //手动输入questionID
        String questionID ="";

       //PostParam postParam = new PostParam(new PostParam.Content("0", stem, subStems, options, solutions,
              //instantiatedLevel, instantiatedCategory, instantiatedDescription,questionID));
       PostParam postParam = new PostParam(new PostParam.Content("2", stem, subStems,options));
        NlpString2 nlpString = new NlpString2();
        String nlpJson = nlpString.getNlpJson(postParam);
        out.println(nlpJson);

        // 这里将关系提取的结果进行格式化输出，便于控制台查看结果，如果直接打印结果，会出现json 没有换行
      //  out.println(nlpString.toPrettyFormat(nlpJson));

        // 如果要将结果写入到文件文本中，取消下面的代码注释，
    //nlpString.soleResult(path,nlpString.toPrettyFormat(nlpJson),false);


    }


}
