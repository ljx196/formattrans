package edu.uestc.transformation.nlp.utils;


import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author: yyWang
 * @create: 2019/10/15
 * @description:
 */

public class LinuxConnection {

    // log4j 记录日志
    private static final Logger log = LogManager.getLogger(LinuxConnection.class);

    // 私有的对象
    private static LinuxConnection linuxConnection;

    private Session session;

    // 是否登录
    private boolean logined = false;

    /**
     * 线程安全，适合多线程
     *
     * @return 返回 LinuxConnection 对象
     */
    public static synchronized LinuxConnection getInstance() {
        if (linuxConnection == null) {
            linuxConnection = new LinuxConnection();
        }
        return linuxConnection;
    }

    /**
     * 私有的构造方法
     */
    private LinuxConnection() {
    }

    /**
     * 远程登录服务器
     */
    public void sshRemoteLogin() {
        if (logined) {
            return;
        }

        // 创建对象
        JSch jsch = new JSch();
        try {
            // 获取到jSch的session, 根据用户名、主机ip、端口号获取一个Session对象
            session = jsch.getSession(Config.username, Config.hostname, Config.port);
            // 设置密码
            session.setPassword(Config.password);

            Properties props = new Properties();
            props.put("StrictHostKeyChecking", "no");
            // 为Session对象设置properties
            session.setConfig(props);

            //设置超时,设置5分钟无操作就关闭连接
            session.setTimeout(300000);

            // 通过 session 建立连接
            session.connect();

            // 设置登录状态
            logined = true;

        } catch (JSchException e) {
            // 设置登录状态
            logined = false;
            log.info(String.format("主机登录失败：%s,userName:%s,异常为：%s", Config.hostname, Config.username, e.getMessage()));
        }
    }


    /**
     * 关闭连接
     */
    public void closeSession() {
        // 调用 session 的关闭连接的方法
        if (session != null) {
            // 如果session不为空，调用session 的关闭连接的方法
            logined = false;
            session.disconnect();
            session = null;

        }
    }


    /**
     * 对将要执行的linux的命令进行遍历
     *
     * @param in   输入命令后返回的结果。
     * @param list 如果不为空，就将数据保存到list 集合中
     * @return 返回命令执行后的结果
     * @throws Exception 由于发生不可知的意外，数据读取失败
     */
    private String processDataStream(InputStream in, List<String> list) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String result;
            while ((result = br.readLine()) != null) {
                sb.append(result).append(",");
                if (list != null) {
                    list.add(result);
                }
            }

        } catch (Exception e) {
            throw new Exception("获取数据流失败: " + e);
        }
        return sb.toString();
    }


    /**
     * 需要执行的 shell 命令，如查看服务器文件命令，以及插入数据相关的操作，
     *
     * @param cmd  需要执行的命令
     * @param list 可以保存查看服务器文本数据
     * @return 返回处理的结果
     */

    private String execCommand(String cmd, List<String> list) {
        if (session == null) {
            sshRemoteLogin();
        }
        // 定义通道
        Channel channel = null;
        InputStream stdout = null;
        try {
            // 命令不为空
            if (cmd != null) {

                // 打开 channel
                // 说明 exec 用于执行命令，sftp 用于文件处理
                channel = session.openChannel("exec");
                // 设置 command
                ((ChannelExec) channel).setCommand(cmd);
                // 进行连接
                channel.connect();

                // 获取到输入流
                stdout = channel.getInputStream();
                String result = processDataStream(stdout, list);
                // 输出返回的结果
                if (result.equals("")) {
                    return "0";
                }
                return result;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stdout != null) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channel != null) {
                channel.disconnect();
            }
            closeSession();
        }

        return "1";
    }

    /**
     * 以追加的方式，向目标服务器中的文件添加内容
     *
     * @param message  需要添加的内容
     * @param isAnswer 区分数据写入文件的位置
     */
    public static int addMessage(String message, boolean isAnswer) {

        // 远程连接
        LinuxConnection.getInstance().sshRemoteLogin();
        // 使用 echo message >>filename 向文件末尾追加内容
        String cmd = "echo -e " + message + " >> " + Config.destinationPath;

        // 判断数据写入的位置
        if (isAnswer) {
            cmd += Config.additionalFileName[1];
        } else {
            cmd += Config.additionalFileName[0];
        }
        //查看服务器中文件临时文本
        String chatResult = chatMessage(message, isAnswer);
        if (chatResult.equals("0")) {
            String result = LinuxConnection.getInstance().execCommand(cmd, null);
            if (result.equals("0")) {
                log.info(message + " 数据添加成功！");
                // 记录每个人标注的服务器文件
                String src = Config.recordPath + System.getProperty("user.name") + ".txt";
                String desc = "./mathematics-nlp/" + System.getProperty("user.name") + ".txt";

                //可以方便地修改日期格式
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String m = "echo -e " + message + " —— " + dateFormat.format(new Date()) + " >> " + src;
                if (LinuxConnection.getInstance().execCommand(m, null).equals("0")) {
                    // 下载文件到本地
                    LinuxConnection.getInstance().fileDownload(src, desc);
                }
                return 0;
            } else {
                log.info(" 数据添加失败请检查");

            }
        }
        // 关闭 session
        LinuxConnection.getInstance().closeSession();
        return 1;
    }

    /**
     * 查看要添加的数据是不是已经存在，如果存在就禁止添加数据到文件中
     *
     * @param message  要检查的数据
     * @param isAnswer
     * @return 返回是否已经存在
     */
    private static String chatMessage(String message, boolean isAnswer) {
//        使用命令查看服务器临时文件内容
        String cmd1 = "cat " + Config.destinationPath;
        // 查看总数据
        String cmd2 = "cat " + Config.destinationPath;
        if (isAnswer) {
            cmd1 += Config.additionalFileName[1];
            cmd2 += Config.trainFileName[1];
        } else {
            cmd1 += Config.additionalFileName[0];
            cmd2 += Config.trainFileName[0];
        }

        // 查询文件的内容
        List<String> descList = new ArrayList<>();
        LinuxConnection.getInstance().execCommand(cmd1, descList);
        LinuxConnection.getInstance().execCommand(cmd2, descList);
        if (!descList.contains(message)) {
            // 说明要添加的数据不存在，可以向服务器文件添加数据
            return "0";
        } else {
            log.info("要添加的数据\n" + message + "！！！ 已存在，请重新检查要添加的数据");
            return "-1";
        }

    }

    /**
     * 文件下载
     *
     * @param src  服务器目标文件
     * @param dest 需要保存到本地的文件
     */
    private void fileDownload(String src, String dest) {
        if (session == null) {
            sshRemoteLogin();
        }
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");

            // 远程连接
            channelSftp.connect();

            // 下载文件，多个重载方法
            channelSftp.get(src, dest);

        } catch (JSchException | SftpException e) {
            log.info(e);
        } finally {
            // 释放资源
            closeSession();
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
        }
    }

}


