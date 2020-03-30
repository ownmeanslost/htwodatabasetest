package com.example.htwodatabasetest.excel.util;

import com.google.common.base.Charsets;
import fr.opensagres.poi.xwpf.converter.core.BasicURIResolver;
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: POIUtils
 * @Author: shaofan.li
 * @Description:
 * @Date: 2020/1/13 17:02
 */
public class POIUtils {
    private static final Logger log = LoggerFactory.getLogger(POIUtils.class.getName());

    /**
     * docx to html
     *
     * @param in 输入流
     * @return
     * @throws IOException
     */
    public static InputStream docxToHtml(InputStream in, String filePath) {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(in);
            XHTMLOptions options = XHTMLOptions.create();
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            filePath = filePath+"image"+ File.separator;
            options.setExtractor(new FileImageExtractor(new File(filePath)));
            options.URIResolver(new BasicURIResolver("image"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, out, options);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return in;
    }

    /**
     * doc to html
     *
     * @param in
     * @return
     * @throws Exception
     */
/*    public static InputStream docToHtml(InputStream in) {
        try {
            HWPFDocument wordDocument = new HWPFDocument(in);
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            outStream.close();
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return in;
    }*/

  /*  public static InputStream docToHtml(Map<String, Object> request, InputStream in) {
        String fileName = (String) request.get("name");
        if (StringUtils.isEmpty(fileName)) {
            return in;
        }
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String prevName = fileName.substring(0, fileName.lastIndexOf("."));
        if ("html".equalsIgnoreCase(extensionName)) {
            return in;
        }
        if (extensionName.equalsIgnoreCase("doc")) {
            request.put("name", prevName + ".html");
            return docToHtml(in);
        }
        if (extensionName.equalsIgnoreCase("docx")) {
            request.put("name", prevName + ".html");
            return docxToHtml(in, (String)request.get("filePath"));
        }
        return in;
    }*/

    public static void inputStreamToFile(InputStream inputStream, String newPath) {
//		InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            /**
             String userDir = System.getProperty("user.dir");
             String path = userDir + "/bin/file.xml";
             inputStream = new FileInputStream(path);
             */

//			String newPath = userDir + "/bin/file-new.xml";
            File file = new File(newPath);
            outputStream = new FileOutputStream(file);

            int bytesWritten = 0;
            int byteCount = 0;

            byte[] bytes = new byte[1024];

            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, bytesWritten, byteCount);
//				bytesWritten += byteCount;
            }

            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }



    public static String htmlToWord(String content) {
        String path = "D:/wordFile";
        Map<String, Object> param = new HashMap<String, Object>();

        if (!"".equals(path)) {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String pdfHtml = "";
            try {
                byte[] bytes = OfficeUtil.inputStream2ByteArray(new FileInputStream("C:\\Users\\shaofan.li\\Desktop\\附件上传\\￥%#￥……&.png"), true);
                String replaceString ="<img  src=\""+ Arrays.toString(bytes) +"\" class=\"sealImg\"></img>";
                 pdfHtml = content.replace("<img  src=\"\" class=\"sealImg\"></img>", replaceString);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
            }
            content = HtmlUtils.htmlUnescape(pdfHtml);
          /*  List<HashMap<String, String>> imgs = getImgStr(content);
            int count = 0;
            for (HashMap<String, String> img : imgs) {
                count++;
                //处理替换以“/>”结尾的img标签
                content = content.replace(img.get("img"), "${imgReplace" + count + "}");
                //处理替换以“>”结尾的img标签
                content = content.replace(img.get("img1"), "${imgReplace" + count + "}");
                Map<String, Object> header = new HashMap<String, Object>();

                try {
                    //如果没有宽高属性，默认设置为400*300
                    if(img.get("width") == null || img.get("height") == null) {
                        header.put("width", 400);
                        header.put("height", 300);
                    }else {
                        header.put("width", (int) (Double.parseDouble(img.get("width"))));
                        header.put("height", (int) (Double.parseDouble(img.get("height"))));
                    }
                    header.put("type", "jpg");
                    header.put("content", OfficeUtil.inputStream2ByteArray(new FileInputStream("C:\\Users\\shaofan.li\\Desktop\\附件上传\\￥%#￥……&.png"), true));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                param.put("${imgReplace" + count + "}", header);
            }*/
            try {
                // 生成doc格式的word文档，需要手动改为docx
                byte by[] = content.getBytes("UTF-8");
                ByteArrayInputStream bais = new ByteArrayInputStream(by);
                POIFSFileSystem poifs = new POIFSFileSystem();
                DirectoryEntry directory = poifs.getRoot();
                directory.createDocument("WordDocument", bais);
                FileOutputStream ostream = new FileOutputStream("D:\\wordFile\\temp.doc");
                poifs.writeFilesystem(ostream);
                bais.close();
                ostream.flush();
                ostream.close();


            /*  // 临时文件（手动改好的docx文件）
                CustomXWPFDocument doc = OfficeUtil.generateWord(param, "D:\\wordFile\\temp1.doc");
                //最终生成的带图片的word文件
                FileOutputStream fopts = new FileOutputStream("D:\\wordFile\\final.doc");
                doc.write(fopts);
                fopts.close();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "D:/wordFile/final.docx";
    }

    //获取html中的图片元素信息
    public static List<HashMap<String, String>> getImgStr(String htmlStr) {
        List<HashMap<String, String>> pics = new ArrayList<HashMap<String, String>>();

        Document doc = Jsoup.parse(htmlStr);
        Elements imgs = doc.select("img");
        for (Element img : imgs) {
            HashMap<String, String> map = new HashMap<String, String>();
            if(!"".equals(img.attr("width"))) {
                map.put("width", img.attr("width").substring(0, img.attr("width").length() - 2));
            }
            if(!"".equals(img.attr("height"))) {
                map.put("height", img.attr("height").substring(0, img.attr("height").length() - 2));
            }
            map.put("img", img.toString().substring(0, img.toString().length() - 1) + "/>");
            map.put("img1", img.toString());
            map.put("src", img.attr("src"));
            pics.add(map);
        }
        return pics;
    }

    public static void main(String[] args) throws IOException {
        String content = "";
            //获取html
        File file = new File("C:\\Users\\shaofan.li\\Desktop\\新建文本文档.html");
        if(!file.exists()){
            System.out.println("文件不存在");
        }
        InputStream reader = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int length =0;
        while ((length = reader.read(bytes)) != -1){
            content += new String(bytes,0,length , Charsets.UTF_8);
        }

        htmlToWord(content);
    }


}
