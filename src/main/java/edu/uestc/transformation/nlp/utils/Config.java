package edu.uestc.transformation.nlp.utils;

/**
 * @author: yyWang
 * @create: 2019/10/15
 * @description: 常用的配置信息
 */

public class Config {

    static String hostname = "aifuture.uestc.cn";

    //数据写入端口
    static int port = 1309;

    static String username = "wyy";

    static String password = "111111";


    static String destinationPath = "pycharm/bert_match/data/";

    static String recordPath = "pycharm/bert_match/record/";

    static String[] trainFileName = {"train.txt","standardAnswer.txt"};

    static String[] additionalFileName = {"append.txt","appendAnswer.txt"};
//    static String additionalFileName = "java_test.txt";


}
