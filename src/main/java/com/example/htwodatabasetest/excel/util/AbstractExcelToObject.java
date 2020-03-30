package com.example.htwodatabasetest.excel.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ExcelToObject
 * @Author: shaofan.li
 * @Description:
 * @Date: 2019/11/22 13:23
 */
public abstract class AbstractExcelToObject {

    //列头
    private static final  ThreadLocal<List<String>> headRow = new ThreadLocal<>();
    public void setHead(List<String> head){
        headRow.set(head);
    }

    public List<String> getHead(){
        return headRow.get();
    }
    // List<List<String>> 里面的List<String>表示某一行数据
    public   List<List<String>> excelToList(InputStream inputStream, boolean isExcel2003) throws IOException {
        Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheetAt(0);
        List<List<String>> listsFrom2 = Lists.newArrayList();
        //取数据
        for(int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
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
        return listsFrom2;
    }

    /*
    *
     *功能描述 将数据扎转换成  List<Map<String,String>>
     * @author shaofan.li
     * @date
     * @param
     * @return
     */
    List<Map<String,String>> transToObject(InputStream inputStream, boolean isExcel2003) throws IOException {
       List<List<String>>  lists = excelToList(inputStream, isExcel2003);
       if (CollectionUtils.isEmpty(lists)){
           return  Lists.newArrayList();
       }

       List<Map<String,String>> list = Lists.newArrayList();
       List<String> head = lists.get(0);
       this.setHead(head);
       //真实数据行数
       int rows = lists.size();
       for (int i = 1; i< rows ; i++){
           List<KeyValue> keyValues = getHeadKeyValue(head);
           List<String> row = lists.get(i);
           for (int j =0;j< row.size(); j++){
               String a = row.get(j);
               keyValues.get(j).setValue(row.get(j));
           }

           //keyValues转map
           Map<String, String> map = Maps.newHashMap();
           for (KeyValue keyValue : keyValues){
               map.put(keyValue.getKey(), keyValue.getValue());
           }
           list.add(map);
       }

        return list;

   }


    //根据第一行数据，弄出数据模板，例如：key=code ，value =''
   protected  List<KeyValue> getHeadKeyValue( List<String> head){
       Map<String, String> fieldNameMap= getFieldNameMap();
       //表示一行数据
       List<KeyValue> keyValues = Lists.newArrayList();
       for (String s : head){
           String field = fieldNameMap.get(s.trim());
           //保证顺序
           KeyValue keyValue = new KeyValue();
           keyValue.setKey(field);
           keyValues.add(keyValue);
       }
       return keyValues;
   }

   class KeyValue{
        private String Key;

        private String value;


       public String getKey() {
           return Key;
       }

       public void setKey(String key) {
           Key = key;
       }

       public String getValue() {
           return value;
       }

       public void setValue(String value) {
           this.value = value;
       }
   }

   /*
   *
    *功能描述 获取对象字段与名称映射
    * @author shaofan.li
    * @date
    * @param
    * @return
    */
    protected abstract Map<String, String> getFieldNameMap();

    //获取字符串类型字段
    protected abstract List<String> getNameColumns();

    //获取数字类型字段
    protected abstract List<String> getTotalColumns();

    //获取全部字段
    protected abstract List<String> getAllColumns();

}
