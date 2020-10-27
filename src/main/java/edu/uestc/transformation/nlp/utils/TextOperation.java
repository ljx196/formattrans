package edu.uestc.transformation.nlp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/8/6
 * @description: txt文本常见操作，比如读取txt 文本，向txt文本写内容等操作
 */

public class TextOperation {
    // log4j 记录日志
    private static final Logger log = LogManager.getLogger(LinuxConnection.class);

    // 文件读取流
    private BufferedReader reader;

    private FileReader r;
    // 文件写入流
    private Writer writer;

    /**
     * 根据文件路径创建写入句柄，并指定文件写入的方式是否是追加，如果文件不存在就会创建
     *
     * @param filepath 文件路径
     * @param isAppend 是否追加
     */
    public void writePath(String filepath, boolean isAppend) {

        String dir = filepath.substring(0, filepath.lastIndexOf("\\"));
        File newFile = new File(dir);

        try {
            if (!newFile.exists()) {
                // 如果文件不存在，就创建
                newFile.mkdirs();
            }
            writer = new FileWriter(filepath, isAppend);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 根据文件的路径读取文本里的内容到 list 集合中
     *
     * @param path 需要读取文件的路径
     * @return 返回读取文件内容的集合
     */
    public static List<String> ReadLines(String path) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            log.error("读取的文件不存在：" + e);
        }

        List<String> res = new ArrayList<String>();
        try {
            String line;
            while (reader != null && (line = reader.readLine()) != null) {
                res.add(line);
            }
        } catch (IOException e) {
            log.error("读取数据失败：" + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("BufferedReader 关闭失败！" + e);
                }
            }
        }
        return res;
    }


    /**
     * 将数据内容以换行的方式写入文件中
     *
     * @param path    文件写入路径
     * @param content 要写入的字符串
     * @param dates   要写入的字符串集合
     * @param append  数据写入是否以追加的方式写入
     */
    public static void writeIntoFile(String path, String content, List<String> dates, boolean append) {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    log.debug("创建文件成功:" + file);
                }
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8));
            if (content != null) {
                writer.write(content);
                log.error("[" + content + "]" + "写入成功");
                writer.newLine();
                // 将内容及时写入文件中
                writer.flush();
            }
            // 将集合里的数据写入文件中
            if (dates != null) {
                for (String date : dates) {
                    writer.write(date);
//                    log.debug("[" + date + "]" + "写入成功");
                    writer.newLine();
                }
                // 将内容及时写入文件中
                writer.flush();
            }

        } catch (FileNotFoundException e) {
            log.error("找不到文件" + e);
        } catch (UnsupportedEncodingException e) {
            log.error("不支持的编码类型" + e);
        } catch (IOException e) {
            log.error("写入文件失败！" + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("BufferedWriter关闭失败！" + e);
                }
            }
        }
        return;
    }


    /**
     * 向文件写入内容，但是没有换行，所有内容写入在一行
     *
     * @param content 待写入的内容
     */
    public void write(String content) {
        try {
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向文件内写入内容，并且换行
     *
     * @param content 待写入的内容
     */
    public void writeLines(String content) {
        try {
            writer.write(content + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭读取文件流
     */
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (r != null) {
                r.close();
            }
            if (writer != null) {
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
