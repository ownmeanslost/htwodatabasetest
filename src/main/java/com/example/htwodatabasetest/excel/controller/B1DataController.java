package com.example.htwodatabasetest.excel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.htwodatabasetest.excel.model.BaseAjaxVO;
import com.example.htwodatabasetest.excel.util.AbstractExcelToObject;
import com.example.htwodatabasetest.excel.util.B1ExcelUtil;
import com.example.htwodatabasetest.excel.util.POIReadExcelToHtml;
import com.example.htwodatabasetest.excel.util.SHA256Util;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @ClassName: B1DataContrator
 * @Author: shaofan.li
 * @Description:
 * @Date: 2019/11/21 19:51
 */
@Controller
public class B1DataController {

    @Autowired
    private B1ExcelUtil b1Excel;

    @RequestMapping("/index")
    public ModelAndView initFtl(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("b1Data");
        return modelAndView;
    }

    /**
     * 通知单文件上传

     * @return
     */
/*    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public BaseAjaxVO uploadFile(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        List<MultipartFile> fileList = request.getFiles("files");
        BaseAjaxVO baseAjaxVO = new BaseAjaxVO();
        baseAjaxVO.setCode(-1);
        baseAjaxVO.setText("附件上传失败！");
        if(CollectionUtils.isEmpty(fileList)){
            baseAjaxVO.setText("上传文件不能为空！");
            return baseAjaxVO;
        }
        MultipartFile multipartFile = fileList.get(0);
        boolean isExcel2003 =  multipartFile.getOriginalFilename().toLowerCase().endsWith(".xls");
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = "";
        int index = originalFilename.lastIndexOf(".");
        if(StringUtils.isNotBlank(originalFilename) && index != -1){
            String str = originalFilename.substring(0,index);

            fileName = str + "_result.xls";

        }
        //创建文件夹
        File file = new File("d://B1Data");
        if(!file.exists()){
            file.mkdirs();
        }

        File fileExcel = new File("d://B1Data",fileName);
        if(!fileExcel.exists()){
            fileExcel.createNewFile();
        }
        //获取文件
        HSSFWorkbook hssfWorkbook = null;
        try (InputStream inp = multipartFile.getInputStream();  OutputStream outputStream = new FileOutputStream(fileExcel);){
            hssfWorkbook = b1Excel.getWorkbook(inp, isExcel2003);
            hssfWorkbook.write(outputStream);
            baseAjaxVO.setCode(0);
            baseAjaxVO.setText( "导出成功!导出文件位于D://B1Data/"+ fileName);
        } catch (Exception e) {
            baseAjaxVO.setCode(-1);
            baseAjaxVO.setText(e.getMessage());
            return baseAjaxVO;
        }
        return baseAjaxVO;
    }*/

    /**
     * 通知单文件上传

     * @return
     */

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public BaseAjaxVO uploadFile(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        List<MultipartFile> fileList = request.getFiles("files");
        BaseAjaxVO baseAjaxVO = new BaseAjaxVO();
        baseAjaxVO.setCode(-1);
        baseAjaxVO.setText("附件上传失败！");
        if(CollectionUtils.isEmpty(fileList)){
            baseAjaxVO.setText("上传文件不能为空！");
            return baseAjaxVO;
        }
        MultipartFile multipartFile = fileList.get(0);

        Workbook wb =  new HSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        List<List<String>> listsFrom2 = Lists.newArrayList();
        //取数据
        for(int rowIndex =1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if(row == null) {
                continue;
            }
            List<String> stringList = Lists.newArrayList();
            //列
            for(int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                Cell cell = row.getCell(cellIndex);
                if(cell == null) {
                    stringList.add("");
                    continue;
                }
                String value = POIReadExcelToHtml.getCellValue(cell);
                stringList.add(value);
            }
            listsFrom2.add(stringList);
        }
        List<Info> list = new LinkedList<>();
        listsFrom2.forEach(e ->{
            Info info = new Info();
           info.name= e.get(0);
           info.zh = e.get(1);
           info.pass= e.get(2);
           info.softvpn = e.get(3);
            info.phone = e.get(4);
           list.add(info);
        });

        list.forEach(e->{
            System.out.print(e.name+","+e.phone);
            //send(e.phone, e.zh, e.pass, e.softvpn);
        });

        return baseAjaxVO;
    }



    public static Deque<Info> list1 = new ConcurrentLinkedDeque<Info>();
    public static Deque<Info> listError = new ConcurrentLinkedDeque<Info>();
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream(new File("C:\\Users\\shaofan.li\\Desktop\\提货函获取车号情况分析20200327.xls"));
        Workbook wb =  new HSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheetAt(1);
        List<String> listsFrom2 = Lists.newArrayList();
        Set<String> stringList = Sets.newHashSet();
        //取数据
        for(int rowIndex =1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if(row == null) {
                continue;
            }
            //列
            Cell cell = row.getCell(2);
            if(cell == null) {
                listsFrom2.add("");
                continue;
            }
            String value = POIReadExcelToHtml.getCellValue(cell);
            if(StringUtils.isNotBlank(value)){
                listsFrom2.add(value);
                stringList.add(value);
            }
        }


        InputStream inputStream1 = new FileInputStream(new File("C:\\Users\\shaofan.li\\Desktop\\物流.xls"));
        Workbook wb1 =  new HSSFWorkbook(inputStream1);
        Sheet sheet1 = wb1.getSheetAt(0);
        Map<String,String> map = Maps.newHashMap();
        //取数据
        for(int rowIndex =1; rowIndex <= sheet1.getLastRowNum(); rowIndex++) {
            Row row = sheet1.getRow(rowIndex);

            if(row == null) {
                continue;
            }
            //列
            Cell cell = row.getCell(0);
            String value = POIReadExcelToHtml.getCellValue(cell);
            if(StringUtils.isNotBlank(value)){
                if(!map.containsKey(value)){
                    map.put(value, "");
                }
            }
            Cell cell2 = row.getCell(1);
            String value2 = POIReadExcelToHtml.getCellValue(cell2);
            String value3 = map.get(value);
            if(StringUtils.isNotBlank(value3)){
                value3 =  value3 +  ","+ value2;
            }else {
                value3 = value2;
            }

            map.put(value, value3);
        }


        List<String> str = Lists.newArrayList();
        for (String s : listsFrom2){
            String value5 = map.get(s);
            str.add(value5);
        }
        System.out.println(JSON.toJSON(str));



        HSSFWorkbook workbook = new HSSFWorkbook();
        //原始页（排序后）
        HSSFSheet excelSheet0 = workbook.createSheet("list");
        int record = 0;
        //设置第一列
        for (String b : str) {
            HSSFRow excelRow = excelSheet0.createRow(record);
            HSSFCell cell = excelRow.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(b));
            record++;
        }

        workbook.write(new FileOutputStream("C:\\Users\\shaofan.li\\Desktop\\物流时间结果.xls"));
        inputStream.close();
        inputStream1.close();


        /*Set<String> set1 = Sets.newHashSet();
        Set<String> set2 = Sets.newHashSet();
        Set<String> set3 = Sets.newHashSet();
        Set<String> set4 = Sets.newHashSet();
        Set<String> set5 = Sets.newHashSet();
        int count = 0;
        for(String s :  stringList){
            if(count < 500){
                set1.add(s);
            }else if(count < 1000){
                set2.add(s);
            }else if(count < 1500){
                set3.add(s);
            }else if(count < 2000){
                set4.add(s);
            }else {
                set5.add(s);
            }
            count ++;
        }
        System.out.println(JSON.toJSON(set2));
        System.out.println(JSON.toJSON(set3));
        System.out.println(JSON.toJSON(set4));
        System.out.println(JSON.toJSON(set5));*/
        /*List<Info> list = new LinkedList<>();
        listsFrom2.forEach(e ->{
            Info info = new Info();
            info.name= e.get(0).trim();
            info.zh = e.get(1).trim();
            info.pass= e.get(2).trim();
            info.softvpn = e.get(3).trim();
            info.phone = e.get(4).trim();
            list.add(info);
        });

        list.forEach(e->{
            System.out.print(e.name+","+e.phone);
            System.out.print("\n");
            //发短信
            send(e.name,e.phone, e.zh, e.pass, e.softvpn);
        });
     *//*   send("李少凡","19945027484", "shaofan.li", "Qa69y8lHitILoPmM", "softvpn1.zhaogangren.com");
        send("李少凡","19945027484", "shaofan.li", "Qa69y8lHitILoPmM", "softvpn1.zhaogangren.com");
        send("李少凡","19945027484", "shaofan.li", "Qa69y8lHitILoPmM", "softvpn1.zhaogangren.com");
        send("李少凡","19945027484", "shaofan.li", "Qa69y8lHitILoPmM", "softvpn1.zhaogangren.com");*//*
        //send("李少凡","", "Guess", "Who", "I am?");
        System.out.println(list.size());
        list1.forEach(e->{
            System.out.print(e.name+","+e.phone);
            System.out.print("\n");

        });
        listError.forEach(e->{
            System.out.print(e.name+","+e.phone);
            System.out.println(e.result);
            System.out.print("\n");

        });
        System.out.println(list1.size());*/
    }



    static class Info{
        public String name;
        public String phone;
        public String zh;
        public String pass;
        public String softvpn;
        public SmsSingleSenderResult result;
    }

    public static void send(String name,String phone, String zh,String pass, String softvpn) {
        // 短信应用 SDK AppID
        int appid = 1400095900; // SDK AppID 以1400开头
        // 短信应用 SDK AppKey
        String appkey = "a8864b7e2243ff44619c3c0bb71c2c37";
        // 需要发送短信的手机号码
        String phoneNumbers =phone;
        // 短信模板 ID，需要在短信应用中申请
        int templateId = 528590; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
        // 签名
        String smsSign = "找钢网";
        try {
            String[] params =  new String[]{zh, pass, softvpn};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    templateId, params, smsSign, "", "");
            Info info = new Info();
            info.name = name;
            info.phone = phone;
            info.result = result;
            if(result.result == 0){
                list1.offer(info);
                //System.out.println("success:name:" + name + "phone:" + phone);
            }else {
                listError.offer(info);
            }

        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        } catch (com.github.qcloudsms.httpclient.HTTPException e) {
            e.printStackTrace();
        }
    }

}
