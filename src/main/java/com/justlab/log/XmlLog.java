package com.justlab.log;

import android.util.Log;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by justi on 2017/7/5.
 */
public class XmlLog {
    public XmlLog() {
    }

    public static void printXml(String tag, String xml, String headString) {
        if(xml != null) {
            xml = formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + "Log with null object";
        }

        Util.printLine(tag, true);
        String[] lines = xml.split(JLog.LINE_SEPARATOR);
        String[] arr$ = lines;
        int len$ = lines.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String line = arr$[i$];
            if(!Util.isEmpty(line)) {
                Log.d(tag, "║ " + line);
            }
        }

        Util.printLine(tag, false);
    }

    public static String formatXML(String inputXML) {
        try {
            StreamSource e = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(e, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception var4) {
            var4.printStackTrace();
            return inputXML;
        }
    }
}

