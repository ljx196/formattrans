package edu.uestc.transformation.nlp.nlpforehandle.Latex;

import com.google.common.base.StandardSystemProperty;
import com.google.common.io.Resources;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author: tjc
 * @create: 2020/9/23
 * @description:
 */

public class ResourceFinder {
    private static final Logger LOG = LogFactory.getLogger(ResourceFinder.class);
    public ResourceFinder() {
    }
    public static Document readXml(String fileName) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        InputStream in = findResources(fileName);
        if (in != null) {
            Document doc = builder.build(in);
            in.close();
            return doc;
        } else {
            throw new IOException("can't find " + fileName + " in the classpath");
        }
    }
    public static BufferedReader readResourceBufferedReader(String fileName) {
        InputStream in = null;

        try {
            URL url = Resources.getResource(fileName);
            in = url.openStream();
            return new BufferedReader(new InputStreamReader(in));
        } catch (IOException var3) {
            LOG.error(var3.getMessage(), var3);
            return null;
        }
    }
    public static InputStream findResources(String fileName) throws IOException {
        URL url;
        try {
            url = Resources.getResource(StandardSystemProperty.FILE_SEPARATOR.value() + fileName);
            LOG.debug("find " + fileName + " in the root of classpath. use it to override the build-in settings");
        } catch (IllegalArgumentException var5) {
            try {
                url = Resources.getResource(fileName);
                LOG.debug("find " + fileName + " in " + url.getPath());
            } catch (IllegalArgumentException var4) {
                LOG.warn("can't find " + fileName + " in the classpath");
                throw new IOException("can't find " + fileName + " in the classpath");
            }
        }

        if (url != null) {
            return url.openStream();
        } else {
            throw new IOException("something is wrong when loading the file " + fileName);
        }
    }
}
