package edu.uestc.transformation.nlp.nlpforehandle.util;

import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.ResourceFinder;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.BufferedReader;
import java.io.IOException;


public class NLUConfiguration {
    private NLUConfiguration() {
    }

    private static final Logger LOG = LogFactory.getLogger(NLUConfiguration.class);

    public static final String NLU_CONFIG_FILENAME = "autosolve_nlu.properties";
    public static final String XML = "xml";

    public static final String THRIFT_SERVER_IP_KEY = "nlp.thrift.ip";
    public static final String THRIFT_SERVER_PORT_KEY = "nlp.thrift.port";
    public static final String THRIFT_SERVER_TIMEOUT_KEY = "nlp.thrift.timeout";
    public static final String DEFAULT_THRIFT_SERVER_IP = "121.199.62.173";
    public static final int DEFAULT_THRIFT_SERVER_PORT = 9097;
    public static final int DEFAULT_THRIFT_SERVER_TIMEOUT = 3000;

   // private static Properties property;

//    static {
//        property = ResourceFinder.buildProperties(NLU_CONFIG_FILENAME);
//    }

  //  public static String getProperty(String key) {
   //     return property.getProperty(key);
   // }

//    public static int getProperty(String key, int defaultValue) {
//        String valueCandidate = getProperty(key);
//        try {
//            return Integer.parseInt(valueCandidate);
//        } catch (Exception e) {
//            LOG.warn("try to get int property from key {} in {}. but failed to convert {} to int. so return default {}"
//                    , key, NLU_CONFIG_FILENAME, valueCandidate, defaultValue,e);
//            return defaultValue;
//        }
//    }

//    public static String getProperty(String key, String defaultValue) {
//        String valueCandidate = getProperty(key);
//        return StringUtils.isEmpty(valueCandidate) ? defaultValue : valueCandidate;
//    }

//    public static String getThriftServerIp() {
//        return getProperty(THRIFT_SERVER_IP_KEY, DEFAULT_THRIFT_SERVER_IP);
//    }
//
//    public static int getThriftServerPort() {
//        return getProperty(THRIFT_SERVER_PORT_KEY, DEFAULT_THRIFT_SERVER_PORT);
//    }
//
//    public static int getThriftServerTimeout() {
//        return getProperty(THRIFT_SERVER_TIMEOUT_KEY, DEFAULT_THRIFT_SERVER_TIMEOUT);
//    }

    public static BufferedReader readResourceBufferedReader(String fileName) {
        return ResourceFinder.readResourceBufferedReader(XML + "/" + fileName);
    }

    public static Document readXml(String fileName) throws JDOMException, IOException {
        return ResourceFinder.readXml(XML + "/" + fileName);
    }
}
