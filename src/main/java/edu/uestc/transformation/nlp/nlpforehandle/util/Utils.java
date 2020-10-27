package edu.uestc.transformation.nlp.nlpforehandle.util;

import com.google.common.collect.Lists;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**工具类，主要包括文件的读写和集合的运算
 * Created by Duansy on 2017/7/27 0027.
 */
public class Utils {
    private static final Logger LOG = LogFactory.getLogger(Utils.class);
    private static Utils instance;
    public static synchronized Utils getInstance() {
        if (instance == null) {
            try {
                instance = new Utils();
            } catch (Exception e) {
                LOG.error("Operate file fail {}", e);
            }
        }
        return instance;
    }

       /**
     * 读文件操作
     * @param filePath
     * @return
     */
    public  List<String> readFile(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null) {
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            LOG.error("读取文件内容出错{}", e);
        }
        return list;
    }
    /**
     * 写文件操作
     * @param filePath
     */
    public void writeFile(String filePath, List<String> list) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            List<String> list1 = list;
            for(String str: list1) {
                out.write(str.toString());
                out.newLine();  //  '\n'不一定都能产生换行的效果
            }
            out.close();
        } catch (IOException e) {
            LOG.error("写文件失败{}", e);
        }
    }
    /**
     * 写文件操作(知识图谱中写点集)
     * @param filePath
     */
    public void writeNodesFile(String filePath, List<String> list) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            List<String> list1 = list;
            out.write("id,label");
            out.newLine();
            for(String str: list1) {
                out.write(str.toString());
                out.newLine();  //  '\n'不一定都能产生换行的效果
            }
            out.close();
        } catch (IOException e) {
            LOG.error("写文件失败{}", e);
        }
    }
    /**
     * 写文件操作(知识图谱中写边集)
     * @param filePath
     */
    public void writeEdgesFile(String filePath, List<String> list) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            List<String> list1 = list;
            out.write("source,target,label");
            out.newLine();
            for(String str: list1) {
                out.write(str.toString());
                out.newLine();  //  '\n'不一定都能产生换行的效果
            }
            out.close();
        } catch (IOException e) {
            LOG.error("写文件失败{}", e);
        }
    }
    /**
     * 求两个集合的并集
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> unionSet(List<String> list1, List<String> list2) {
        list2.removeAll(list1);
        list1.addAll(list2);
        return list1;
    }
    /**
     * 求两个集合的差集(list1-list2)
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> diffSet(List<String> list1, List<String> list2) {
        list1.removeAll(list2);
        return list1;
    }
    /**
     * 求两个集合的交集
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> intersection(List<String> list1, List<String> list2) {
        list1.retainAll(list2);
        return list1;
    }
    public static List<String> intersection1(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<>();
        for(String string1 : list1) {
            for(String string2 : list2) {
                if(string1.equals(string2) || string1 == string2) {
                    result.add(string1);
                }
            }
        }
        return result;
    }
    /**
     * 去除重复的ID
     * @param InputFilePath
     * @param OutputFilePath
     */
    public void deleteMultipleId(String InputFilePath, String OutputFilePath) {
        List<String> list = readFile(InputFilePath);
        Set<String> stringSet = new HashSet<String>(list); //去重
        List<String> stringList =  new ArrayList<String>(stringSet);
        writeFile(OutputFilePath, stringList);
    }
    public void getQuestionId() {
        List<String> stringList = readFile("F:/使用文档/test/quanliang.txt");
        List<String> stringList1 = new ArrayList<String>();
        Pattern pattern = Pattern.compile("(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Z]{32}");
        for(String string : stringList) {
            Matcher matcher = pattern.matcher(string);
            if(matcher.find()) {
                stringList1.add(matcher.group());
            }
        }
        writeFile("F:/使用文档/test/已经跑全量.txt", stringList1);
    }

    /**
     * 生成Sql语句的组合
     * @param list
     */
    public void getSqlFromId(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        for(String string : list) {
            stringBuffer.append("'").append(string).append("'").append(",");
        }
        System.out.println(stringBuffer);
    }

    /**
     * 返回一个对象的所有属性（包括父类的）
     * @param o
     * @return
     */
    public List<Field> getFileds(Object o) {
        List<Field> fields = Lists.newArrayList();
        Class clazz = o.getClass();
       /* while (clazz != null && !clazz.equals(AbstractRelation.class)) {
            Field[] field = clazz.getDeclaredFields();  //得到类的属性
            for (int i = 0; i < field.length; i++) {
                fields.add(field[i]);
            }
            clazz = clazz.getSuperclass();
        }
*/
        return fields;
    }

    /**
     * 返回一个实例对象的现有属性
     * @param
     * @return
     */

    public static void answer() {
        String str1 = "E:\\corpus\\DuReader_v2.0_test1\\test1set\\raw\\test.json";
        String out = "E:\\corpus\\DuReader_v2.0_test1\\test1set\\raw\\result.json";
        List<String> list1 = Utils.getInstance().readFile(str1);
        List<String> result = new ArrayList<>();
        String regex = "\"question_id\": [0-9]+";
        String regex1 = "\"question_type\": \"[A-Z]+\"";
        String regex2 = "\"[\\u4E00-\\u9FA5]+\"";

        Pattern pattern = Pattern.compile(regex);
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);

        String id = null;
        String type = null;
        String answer = null;

        for (String string : list1) {
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                id = matcher.group();
            }
            Matcher matcher1 = pattern1.matcher(string);
            while (matcher1.find()) {
                type = matcher1.group();
            }
            Matcher matcher2 = pattern2.matcher(string);
            List<String> ans = new ArrayList<>();
            while (matcher2.find()) {
                answer = matcher2.group();
                ans.add(answer);
            }
            StringBuffer stringBuffer = new StringBuffer("{");
            stringBuffer.append(id).append(",").append(type).append(",").append("\"answers\": [").append(answer).append("],").append("\"yesno_answers\": []").append("}");
            String temp = new String(stringBuffer);
            System.out.println(temp);
            result.add(temp);
        }
        Utils.getInstance().writeFile(out, result);
    }

}
