package finley.gmair.util;

import finley.gmair.model.air.MojiRecord;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MojiRecordUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/10/10 1:31 AM
 */
public class MojiRecordUtil {
    private static Logger logger = LoggerFactory.getLogger(MojiRecordUtil.class);

    public static List<MojiRecord> list(File file) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(file);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Element root = document.getRootElement();
        return interpret(root);
    }

    private static List<MojiRecord> interpret(Element root) {
        List<MojiRecord> list = new ArrayList<>();
        List<Element> children = root.elements("RECORD");
        for (Element item : children) {
            try {
                int fid = Integer.parseInt(item.element("Fid").getTextTrim());
                String name = item.element("name").getTextTrim();
                String enName = item.element("name_en").getTextTrim();
                String pyName = item.element("name_py").getTextTrim();
                String province = item.element("Fprovince_cn").getTextTrim();
                MojiRecord record = new MojiRecord(fid, name, enName, pyName, province);
                list.add(record);
            } catch (Exception e) {
                continue;
            }
        }
        return list;
    }
}
