package edu.uestc.transformation.nlp.nlpforehandle.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 获取根目录下java类的继承关系和接口实现关系
 * Create by kjj on 2016/7
 */
public class selectrelation {

    private List<List<String>> list;
    private int index;

    public List<List<String>> getList() {
        return list;
    }


    public void setList(List<List<String>> list) {
        this.list = list;
    }


    public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
    }



    public void readFileByLines(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            BufferedReader reader = null;
            try {
                //System.out.println("以行为单位读取文件内容，一次读一整行：");
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    String relation = "",extend = "",implement = "";
                    if (tempString.contains("class") && tempString.contains("public")) {
                        index++;
                        //System.out.println(tempString);
                        String regex1 = "implements(.*)\\{";
                        String regex2 = "extends(.*)implements";
                        String regex6 = "extends(.*)\\{";
                        String regex3 = "class(.*)extends";
                        String regex4 = "class(.*)implements";
                        String regex5 = "class(.*)\\{";
                        Pattern pattern1 = Pattern.compile(regex1);
                        Pattern pattern2 = Pattern.compile(regex2);
                        Pattern pattern3 = Pattern.compile(regex3);
                        Pattern pattern4 = Pattern.compile(regex4);
                        Pattern pattern5 = Pattern.compile(regex5);
                        Pattern pattern6 = Pattern.compile(regex6);
                        Matcher matcher1 = pattern1.matcher(tempString);
                        Matcher matcher2 = pattern2.matcher(tempString);
                        Matcher matcher3 = pattern3.matcher(tempString);
                        Matcher matcher4 = pattern4.matcher(tempString);
                        Matcher matcher5 = pattern5.matcher(tempString);
                        Matcher matcher6 = pattern6.matcher(tempString);
                        if (tempString.contains("extends") && tempString.contains("implements")) {
                            while (matcher1.find()) {
                                implement = matcher1.group(1);
                            }
                            while (matcher2.find()) {
                                extend = matcher2.group(1);
                            }
                            while (matcher3.find()) {
                                relation = matcher3.group(1);
                            }
                        }
                        else if(tempString.contains("extends") && !tempString.contains("implements")){
                            while (matcher6.find()) {
                                extend = matcher6.group(1);
                            }
                            while (matcher3.find()) {
                                relation = matcher3.group(1);
                            }
                        }
                        else if (!tempString.contains("extends") && tempString.contains("implements")) {
                            while (matcher1.find()) {
                                implement = matcher1.group(1);
                            }
                            while (matcher4.find()) {
                                relation = matcher4.group(1);
                            }
                        }
                        else {
                            while (matcher5.find()) {
                                relation = matcher5.group(1);
                            }
                        }
                        List<String> temp = new ArrayList<String>();
                        temp.add(relation);
                        temp.add(extend);
                        temp.add(implement);
                        list.add(temp);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        else {
            String[] list = file.list();
            for(int i =0;i<list.length;i++){
                readFileByLines(fileName+"\\"+list[i]);
            }
        }
    }

}
