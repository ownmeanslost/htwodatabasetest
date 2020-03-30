package com.example.htwodatabasetest.excel.util;

import com.alibaba.fastjson.JSON;
import com.example.htwodatabasetest.excel.model.B1DataBO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.unidal.lookup.util.ReflectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @ClassName: B1Excel
 * @Author: shaofan.li
 * @Description:
 * @Date: 2019/11/23 11:19
 */
@Service
public class B1ExcelUtil extends AbstractExcelToObject {

    //未完成
    private static final  ThreadLocal<List<B1DataBO>> notFinished = new ThreadLocal<>();
    public void setNotFinished(List<B1DataBO> notFinishedP){
        notFinished.set(notFinishedP);
    }

    public List<B1DataBO> getNotFinished(){
        return notFinished.get();
    }

    //已经完成
    private static final  ThreadLocal<List<B1DataBO>> finished = new ThreadLocal<>();
    public void setFinished(List<B1DataBO> finishedP){
        finished.set(finishedP);
    }

    public List<B1DataBO> getFinished(){
        return finished.get();
    }

    @Override
    protected Map<String, String> getFieldNameMap() {
        Map<String, String> map = new HashMap<>();
        map.put("序号","id");
        map.put("单号","code");
        map.put("采购方","customerName");
        map.put("状态","status");
        map.put("供应商","supplierName");
        map.put("下单时间","addTime");
        map.put("交易员","tradeName");
        map.put("数量","qty");
        map.put("实发数量","sfQty");
        map.put("大区","areaName");
        map.put("实提部门","stDepartmentName");
        map.put("发票","finance");
        map.put("重量","weight");
        map.put("实发重量","sfWeight");
        map.put("B2结算状态","B2SettlementStatus");
        map.put("联系人电话","contacterTel");
        map.put("联系人","contacterName");
        map.put("备注","remark");
        map.put("实提应收","shouldReceiptedAmt");
        map.put("来源","source");
        map.put("类型","type");
        map.put("预收金额","preReceiptedAmt");
        map.put("部门","department");
        map.put("事业部","syb");
        map.put("是否使用白条","canUseBT");
        map.put("凭证使用金额","creAmt");
        map.put("货主","owner");
        map.put("最后更新","lastModifyTime");
        map.put("分公司","partCompany");
        map.put("补退时间","returnPayTime");
        map.put("下单IP","ip");
        map.put("外部合同号","otherContractCode");
        map.put("B1结算状态","b1SettlementTime");
        map.put("供应支款","GYPayAmt");
        map.put("实提应付","STYF");
        map.put("货源","HY");
        map.put("客服人员","KFRY");
        return map;
    }

    @Override
    protected List<String> getNameColumns() {
        return null;
    }

    @Override
    protected List<String> getTotalColumns() {
        return Arrays.asList("qty","sfQty","weight","sfWeight","shouldReceiptedAmt","preReceiptedAmt","creAmt","GYPayAmt",
                "STYF");
    }

    @Override
    protected List<String> getAllColumns() {
        return Arrays.asList(
                "id","code","customerName","status","supplierName"
                ,"addTime","tradeName","qty","sfQty","areaName"
                ,"stDepartmentName","finance","weight","sfWeight","B2SettlementStatus"
                ,"contacterTel","contacterName","remark","shouldReceiptedAmt","source"
                ,"type","preReceiptedAmt","department","syb","canUseBT"
                ,"creAmt","owner","lastModifyTime","partCompany","returnPayTime"
                ,"ip","otherContractCode","b1SettlementTime","GYPayAmt","STYF"
                ,"HY","KFRY");
    }

    public List<B1DataBO> transData(InputStream inputStream, boolean isExcel2003) throws IOException {
        List<Map<String,String>>  mapList = transToObject(inputStream, isExcel2003);

        List<B1DataBO> list = Lists.newArrayList();


        //json转对象
        for (Map<String,String> map : mapList){
            //map 转Json
            String toString = JSON.toJSONString(map).toString();
            B1DataBO b1Data = JSON.parseObject(toString, B1DataBO.class);
            list.add(b1Data);
        }
        //按照下单时间排序
        Collections.sort(list);
        return  list;
    }

    //导出数据
    public HSSFWorkbook getWorkbook(InputStream inputStream, boolean isExcel2003) throws IOException, ParseException {
        List<B1DataBO> b1DataList =  transData(inputStream, isExcel2003);

        HSSFWorkbook workbook = new HSSFWorkbook();
        if(CollectionUtils.isNotEmpty(b1DataList)){
            //原始页（排序后）
            HSSFSheet excelSheet0 = workbook.createSheet("list");
            getOrigDataSheet(excelSheet0,workbook, b1DataList);
            //少提数据Sheet
            HSSFSheet excelSheet1 = workbook.createSheet("少提数据");
            getLestDataSheet(excelSheet1,workbook, b1DataList);

            //未完成表
            HSSFSheet excelSheet2 = workbook.createSheet("未完成");
            getNotFinishSheet(excelSheet2, workbook, b1DataList);
            //已完成表
            HSSFSheet excelSheet3 = workbook.createSheet("已完成");
            getFinishSheet(excelSheet3, workbook, b1DataList);
            //汇总表
            HSSFSheet excelSheet4 = workbook.createSheet("汇总表");
            getTotalSheet(excelSheet4, workbook, b1DataList);
        }
        return workbook;
    }

    private void getOrigDataSheet(HSSFSheet excelSheet, HSSFWorkbook workbook, List<B1DataBO> b1DataList) {
        List<String> list = getHead();
        //设置第一列
        setExcelHeader(workbook, excelSheet, list);
        List<String> allFields = getAllColumns();
        Class<B1DataBO> rowClass = B1DataBO.class;
        if (CollectionUtils.isNotEmpty(b1DataList)){
            //填充表格行数据
            setExcelRows(excelSheet,b1DataList,allFields,null,rowClass);
        }

    }


    private void getTotalSheet(HSSFSheet excelSheet, HSSFWorkbook workbook, List<B1DataBO> b1DataList) throws ParseException {
        //大区部门映射
        Map<String,Set<String>> mapMap = Maps.newHashMap();
        for (B1DataBO b1DataBO : b1DataList){
            Set<String> departList = mapMap.get(b1DataBO.getAreaName());
            if(CollectionUtils.isEmpty(departList)){
                departList = Sets.newLinkedHashSet();
                departList.add(b1DataBO.getStDepartmentName());
                mapMap.put(b1DataBO.getAreaName(), departList);
            }
            departList.add(b1DataBO.getStDepartmentName());
        }

        Set<Map.Entry<String, Set<String>>> entries = mapMap.entrySet();
        int rowNum =0;
        // 获取列头样式对
        HSSFCellStyle columnTopStyle = this.setHeaderStyle(workbook);
        for (Map.Entry<String, Set<String>> stringSet : entries){
            Set<String> departments = stringSet.getValue();
            if(CollectionUtils.isNotEmpty(departments)){
                //创建本区第一行第一列
                HSSFRow row1 = excelSheet.createRow(rowNum++);
                createCell(row1, 0, stringSet.getKey(), columnTopStyle);
                // 合并列(4个参数，分别为起始行，结束行，起始列，结束列)
                CellRangeAddress region1 = new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 12);
                excelSheet.addMergedRegion(region1);

                //创建第二行
                HSSFRow row2 = excelSheet.createRow(rowNum++);
                createCell(row2, 0, "", columnTopStyle);//第一列
                createCell(row2, 1, "T+1", columnTopStyle);//第二列
                // 合并列(4个参数，分别为起始行，结束行，起始列，结束列)
                CellRangeAddress region2 = new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 3);
                excelSheet.addMergedRegion(region2);
                createCell(row2, 4, "2天", columnTopStyle);//第三列
                // 合并列(4个参数，分别为起始行，结束行，起始列，结束列)
                CellRangeAddress region3 = new CellRangeAddress(rowNum - 1, rowNum - 1, 4, 6);
                excelSheet.addMergedRegion(region3);
                createCell(row2, 7, "3天", columnTopStyle);//第四列
                // 合并列(4个参数，分别为起始行，结束行，起始列，结束列)
                CellRangeAddress region4 = new CellRangeAddress(rowNum - 1, rowNum - 1, 7, 9);
                excelSheet.addMergedRegion(region4);
                createCell(row2, 10, "超期", columnTopStyle);//第四列
                // 合并列(4个参数，分别为起始行，结束行，起始列，结束列)
                CellRangeAddress region5 = new CellRangeAddress(rowNum - 1, rowNum - 1, 10, 12);
                excelSheet.addMergedRegion(region5);

                //创建第三行
                HSSFRow row3 = excelSheet.createRow(rowNum++);
                createCell(row3, 0, "部门", columnTopStyle);//第一列
                createCell(row3, 1, "未完成", columnTopStyle);//第二列
                createCell(row3, 2, "完成", columnTopStyle);//第三列
                createCell(row3, 3, "完成率", columnTopStyle);//第四列
                createCell(row3, 4, "未完成", columnTopStyle);//第5列
                createCell(row3, 5, "完成", columnTopStyle);//第6列
                createCell(row3, 6, "完成率", columnTopStyle);//第7列
                createCell(row3, 7, "未完成", columnTopStyle);//第8列
                createCell(row3, 8, "完成", columnTopStyle);//第9列
                createCell(row3, 9, "完成率", columnTopStyle);//第10列
                createCell(row3, 10, "未完成", columnTopStyle);//第11列
                createCell(row3, 11, "完成", columnTopStyle);//第12列
                createCell(row3, 12, "完成率", columnTopStyle);//第13列

                //获取数据
                //按照下单时间排序
                Set<String> sortSet = new TreeSet<String>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);//降序排列
                    }
                });
                sortSet.addAll(departments);
                String[][] data = getTotalData(sortSet, stringSet.getKey());
                int departIndex = 0;
                for (String departmentName: sortSet){
                    //创建下一行
                    HSSFRow row = excelSheet.createRow(rowNum++);
                    createCell(row, 0, departmentName, columnTopStyle);//第一列
                    createCell(row, 1, data[departIndex][0], columnTopStyle);//第二列
                    createCell(row, 2, data[departIndex][1], columnTopStyle);//第三列
                    createCell(row, 3, data[departIndex][2], columnTopStyle);//第四列
                    createCell(row, 4, data[departIndex][3], columnTopStyle);//第5列
                    createCell(row, 5, data[departIndex][4], columnTopStyle);//第6列
                    createCell(row, 6, data[departIndex][5], columnTopStyle);//第7列
                    createCell(row, 7, data[departIndex][6], columnTopStyle);//第8列
                    createCell(row, 8, data[departIndex][7], columnTopStyle);//第9列
                    createCell(row, 9, data[departIndex][8], columnTopStyle);//第10列
                    createCell(row, 10, data[departIndex][9], columnTopStyle);//第11列
                    createCell(row, 11, data[departIndex][10], columnTopStyle);//第12列
                    createCell(row, 12, data[departIndex][11], columnTopStyle);//第13列
                    departIndex ++;
                }
                //总计
                HSSFRow row = excelSheet.createRow(rowNum++);
                createCell(row, 0, "总计", columnTopStyle);//第一列
                createCell(row, 1, data[departIndex][0], columnTopStyle);//第二列
                createCell(row, 2, data[departIndex][1], columnTopStyle);//第三列
                createCell(row, 3, data[departIndex][2], columnTopStyle);//第四列
                createCell(row, 4, data[departIndex][3], columnTopStyle);//第5列
                createCell(row, 5, data[departIndex][4], columnTopStyle);//第6列
                createCell(row, 6, data[departIndex][5], columnTopStyle);//第7列
                createCell(row, 7, data[departIndex][6], columnTopStyle);//第8列
                createCell(row, 8, data[departIndex][7], columnTopStyle);//第9列
                createCell(row, 9, data[departIndex][8], columnTopStyle);//第10列
                createCell(row, 10, data[departIndex][9], columnTopStyle);//第11列
                createCell(row, 11, data[departIndex][10], columnTopStyle);//第12列
                createCell(row, 12, data[departIndex][11], columnTopStyle);//第13列
                rowNum++;
            }
        }
    }

    /*
    *
     *功能描述 获取汇总数据
     * @author shaofan.li
     * @date
     * @param
     * @return
     */
    private String[][] getTotalData(Set<String> departments, String area) throws ParseException {
        String[][] data = new String[departments.size() + 1][12];
        int departIndex = 0;
        for (String department : departments){

           //T+1未完成的数量
            Date date1NotFinish = setTotalValue(data, departIndex, 0, notFinished.get(), area, department, DateUtil.getLastDayFor24ReturnDate(new Date()));
            //T+1已经完成的数量
            Date date1Finish = setTotalValue(data, departIndex, 1, finished.get(), area, department,DateUtil.getLastDayFor24ReturnDate(new Date()));
            //T+1已经完成率
            Double cs =  Double.valueOf(data[departIndex][1]);
            int bcs = Integer.valueOf(data[departIndex][1]) + Integer.valueOf(data[departIndex][0]);
            if(0 != bcs){
                BigDecimal bigDecimal = BigDecimal.valueOf(cs/bcs*100).setScale(0,BigDecimal.ROUND_HALF_UP);
                data[departIndex][2] = bigDecimal.toString()+"%";
            }

            //T+2未完成的数量
            Date date2NotFinish = setTotalValue(data, departIndex, 3,  notFinished.get(), area, department, date1NotFinish);
            //T+2已经完成的数量
            Date date2Finish =  setTotalValue(data, departIndex, 4,  finished.get(), area, department, date1Finish);
            //T+2已经完成率
            Double cs1 =  Double.valueOf(data[departIndex][4]);
            int bcs1 = Integer.valueOf(data[departIndex][4]) + Integer.valueOf(data[departIndex][3]);
            if(0 != bcs1){
                BigDecimal bigDecimal = BigDecimal.valueOf(cs1/bcs1*100).setScale(0,BigDecimal.ROUND_HALF_UP);
                data[departIndex][5] = bigDecimal.toString()+"%";
            }

            //T+3未完成的数量
            Date date3NotFinish = setTotalValue(data, departIndex, 6,  notFinished.get(), area, department, date2NotFinish);
            //T+3已经完成的数量
            Date date3Finish = setTotalValue(data, departIndex, 7,  finished.get(), area, department, date2Finish);
            //T+3已经完成率
            Double cs2 =  Double.valueOf(data[departIndex][7]);
            int bcs2 = Integer.valueOf(data[departIndex][7]) + Integer.valueOf(data[departIndex][6]);
            if(0 != bcs2){
                BigDecimal bigDecimal = BigDecimal.valueOf(cs2/bcs2*100).setScale(0,BigDecimal.ROUND_HALF_UP);
                data[departIndex][8] = bigDecimal.toString()+"%";
            }


            //超期未完成的数量
            setTotalValueForCQ(data, departIndex, 9,  notFinished.get(), area, department, date3NotFinish);
            //超期已经完成的数量
            setTotalValueForCQ(data, departIndex, 10,  finished.get(), area, department, date3Finish);
            //超期已经完成率
            Double cs3 =  Double.valueOf(data[departIndex][10]);
            int bcs3 = Integer.valueOf(data[departIndex][10]) + Integer.valueOf(data[departIndex][9]);
            if(0 != bcs3){
                BigDecimal bigDecimal = BigDecimal.valueOf(cs3/bcs3*100).setScale(0,BigDecimal.ROUND_HALF_UP);
                data[departIndex][11] = bigDecimal.toString()+"%";
            }
            departIndex ++;
        }
        //合计
        for(int i=0; i < 12; i ++){
            if((i+1)%3 == 0){
                Double cs =  Double.valueOf(data[departIndex][i-1]);
                double bcs = Double.valueOf(data[departIndex][i-1]) + Double.valueOf(data[departIndex][i-2]);
                if(0 != bcs){
                    BigDecimal bigDecimal = BigDecimal.valueOf(cs/bcs*100).setScale(0,BigDecimal.ROUND_HALF_UP);
                    data[departIndex][i] = bigDecimal.toString()+"%";
                }
                continue;
            }
            data[departIndex][i] = "0";
            for(int j = 0; j < departIndex; j++){
                int a = 0;
                if(StringUtils.isNotBlank(data[j][i])){
                    a = Integer.valueOf(data[j][i]);
                }
                data[departIndex][i] = String.valueOf(Integer.valueOf(data[departIndex][i]) + a);

            }
        }

        return data;
    }

    /*
    *
     *功能描述
     * @author shaofan.li
     * @date
     * @param  data 存放计算结果
     * @param departIndex，cell 横坐标，纵坐标
     * @param day 回退天数
     * @param date 被回退时间
     * @b1DataBOS 数据源
     * @area 大区
     * @department 部门
     * @return Date 时间区域下限
     */
    private Date setTotalValue(String[][] data,int departIndex, int cell
            , List<B1DataBO> b1DataBOS, String area, String department
            , Date date) throws ParseException {
        String str1 = DateUtil.dateToString(date);
        B1DataBO b1DataBO = new B1DataBO();
        b1DataBO.setAddTime(str1);
        int day = 0;
        Date date2 = null;
        //前一天为周末则在取前一天
        do{
            day --;
            date2 =  DateUtil.getBeforeDayReturnDate(date, day);
        } while (checkIsWeekend(DateUtil.addMinute(date2, 1)));

        String str2 = DateUtil.dateToString(date2);
        B1DataBO b1DataBO2 = new B1DataBO();
        b1DataBO2.setAddTime(str2);
        long result = b1DataBOS
                .stream()
                .filter(e -> (e.getAreaName().equals(area)
                        && e.getStDepartmentName().equals(department)
                        && (e.compareTo(b1DataBO) > 0) && (e.compareTo(b1DataBO2) < 0)))
                .count();
        data[departIndex][cell] =String.valueOf(result);
        return date2;
    }


    /*
     *
     *功能描述
     * @author shaofan.li
     * @date
     * @param  data 存放超期计算结果
     * @param departIndex，cell 横坐标，纵坐标
     * @param day 回退天数
     * @param date 被回退时间
     * @b1DataBOS 数据源
     * @area 大区
     * @department 部门
     * @return Date 时间区域下限
     */
    private void setTotalValueForCQ(String[][] data,int departIndex, int cell
            , List<B1DataBO> b1DataBOS, String area, String department
            , Date date){
        String str1 = DateUtil.dateToString(date);
        B1DataBO b1DataBO = new B1DataBO();
        b1DataBO.setAddTime(str1);

        long result = b1DataBOS
                .stream()
                .filter(e -> (e.getAreaName().equals(area)
                        && e.getStDepartmentName().equals(department)
                        && (e.compareTo(b1DataBO) > 0)))
                .count();
        data[departIndex][cell] =String.valueOf(result);
    }
    /*
    *
     *功能描述判断时间是否为周末
     * @author shaofan.li
     * @date
     * @param
     * @return
     */
    private boolean checkIsWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
       return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

    }

    private void createCell(HSSFRow row, int index, String value, HSSFCellStyle columnTopStyle ){
        HSSFCell cell = row.createCell(index);
        cell.setCellStyle(columnTopStyle);
        cell.setCellValue(value);
    }

    private void getFinishSheet(HSSFSheet excelSheet, HSSFWorkbook workbook, List<B1DataBO> b1DataList) {
        List<String> list = getHead();
        //list深度拷贝
        List<String> newList = new ArrayList<>();
        CollectionUtils.addAll(newList, new Object[list.size()]);
        Collections.copy(newList, list);
        newList.add("差异数量");
        //设置第一列
        setExcelHeader(workbook, excelSheet, newList);

        List<B1DataBO> leastList = Lists.newArrayList();
        //提取少提数据(待采购提货、待开提货函状态的订单且实发数大于0 且实发数量-数量 <5)
        for (B1DataBO b1Data : b1DataList){
            Integer sfQty = StringUtils.isBlank(b1Data.getSfQty()) ? 0 : Integer.valueOf(b1Data.getSfQty());
            Integer qty = StringUtils.isBlank(b1Data.getQty()) ? 0 : Integer.valueOf(b1Data.getQty());
            if("待采购客户提货".equals(b1Data.getStatus().trim()) || "待开提货函".equals(b1Data.getStatus())){
                if(sfQty> 0 && qty - sfQty <5){
                    b1Data.setDiffQty(qty -sfQty);
                    leastList.add(b1Data);
                }
            }else if("提货完成".equals(b1Data.getStatus().trim())){
                leastList.add(b1Data);
            }
        }
        Class<B1DataBO> rowClass = B1DataBO.class;
        List<String> totalColums = getTotalColumns();
        List<String> allFields = getAllColumns();
        if (CollectionUtils.isNotEmpty(leastList)){
            //填充表格行数据
            setExcelRows(excelSheet,leastList,allFields,totalColums,rowClass);
        }
        //全局存放
        finished.set(leastList);
    }

    private void getNotFinishSheet(HSSFSheet excelSheet, HSSFWorkbook workbook, List<B1DataBO> b1DataList) {
        List<String> list = getHead();
        //设置第一列
        setExcelHeader(workbook, excelSheet, list);
        List<B1DataBO> list1 = Lists.newArrayList();
        //提取少提数据(待采购提货、待开提货函状态的订单且实发数大于0 且实发数量-数量 <5)
        for (B1DataBO b1Data : b1DataList){
            int sfQty = StringUtils.isBlank(b1Data.getSfQty()) ? 0 : Integer.valueOf(b1Data.getSfQty());
            if("待采购客户提货".equals(b1Data.getStatus().trim())){
                if(sfQty == 0){
                    list1.add(b1Data);
                }
            }
        }
        Class<B1DataBO> rowClass = B1DataBO.class;
        List<String> allFields = getAllColumns();
        if (CollectionUtils.isNotEmpty(list1)){
            //填充表格行数据
            setExcelRows(excelSheet,list1,allFields,null,rowClass);
        }
        //全局存放
        notFinished.set(list1);
    }

    /*
    *
     *功能描述 获取少提数据Sheet
     * @author shaofan.li
     * @date
     * @param
     * @return
     */
    private void getLestDataSheet(HSSFSheet excelSheet, HSSFWorkbook workbook, List<B1DataBO> b1DataList) {

        List<String> list = getHead();
        //list深度拷贝
        List<String> newList = new ArrayList<>();
        CollectionUtils.addAll(newList, new Object[list.size()]);
        Collections.copy(newList, list);
        newList.add("差异数量");
        //设置第一列
        setExcelHeader(workbook, excelSheet, newList);

        List<B1DataBO> leastList = Lists.newArrayList();
        //提取少提数据(待采购提货、待开提货函状态的订单且实发数大于0 且实发数量-数量 <5)
        for (B1DataBO b1Data : b1DataList){
            Integer sfQty = StringUtils.isBlank(b1Data.getSfQty()) ? 0 : Integer.valueOf(b1Data.getSfQty());
            Integer qty = StringUtils.isBlank(b1Data.getQty()) ? 0 : Integer.valueOf(b1Data.getQty());
            if("待采购客户提货".equals(b1Data.getStatus().trim()) || "待开提货函".equals(b1Data.getStatus())){
                if(sfQty> 0 && qty - sfQty <5){
                    b1Data.setDiffQty(qty -sfQty);
                    leastList.add(b1Data);
                }
            }
        }
        Class<B1DataBO> rowClass = B1DataBO.class;
        List<String> totalColums = getTotalColumns();
        List<String> allFields = getAllColumns();
        List<String> allFieldsList = new ArrayList<>();
        CollectionUtils.addAll(allFieldsList, new Object[allFields.size()]);
        Collections.copy(allFieldsList, allFields);
        allFieldsList.add("diffQty");
        if (CollectionUtils.isNotEmpty(leastList)){
            //填充表格行数据
            setExcelRows(excelSheet,leastList,allFieldsList,totalColums,rowClass);
        }
    }




    /**
     * 设置行数据
     *
     * @param excelSheet
     * @param allFields 所有字段
     * @param totalColumn 数字字段
     * @param rowClass 类
     */
    private <T> void setExcelRows(HSSFSheet excelSheet, List<T> b1DataList, List<String> allFields,
                                  List<String> totalColumn,  Class<T> rowClass) {
        //由于列设置未配置下面列的String值，需要做个转换
        int record = 1;
        String fieldName = "";
        for (T b : b1DataList) {
            HSSFRow excelRow = excelSheet.createRow(record);
            for (int j = 0; j < allFields.size(); j++) {
                String configField = allFields.get(j);
                String cellType = null;
                //数字类型
               /* if(totalColumn.contains(configField)){
                    cellType = String.valueOf(HSSFCell.CELL_TYPE_NUMERIC);
                    fieldName = configField;
                }else {*/
                    cellType = String.valueOf(HSSFCell.CELL_TYPE_STRING);
                    fieldName = configField;
                //}
                Field field = FieldUtils.getField(rowClass, fieldName, true);
                if (field != null) {
                    createCell(excelRow, j, ReflectUtils.invokeGetter(b, fieldName),cellType);
                }
            }
            record++;
        }
    }

    /**
     * 创建excel表格的列
     *
     * @param row
     * @param cellNum
     * @param value
     */
    private void createCell(HSSFRow row, int cellNum, Object value, String cellType) {
        if (row != null) {
            HSSFCell cell = row.createCell(cellNum);
            if("0".equals(cellType)){
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(getNotNullNumber(value));
            }else{
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(new HSSFRichTextString(getNotNullStr(value)));
            }
        }
    }
    private Double getNotNullNumber(Object value) {
        return (value != null) ? Double.valueOf(value.toString()) : 0;
    }

    /**
     * 对象转string
     * @param value
     * @return
     */
    private String getNotNullStr(Object value) {
        return (value != null) ? String.valueOf(value) : "";
    }


    /**
     * 设置列头样式
     *
     * @param workbook
     * @param excelSheet
     * @param configNames
     */
    private void setExcelHeader(HSSFWorkbook workbook, HSSFSheet excelSheet, List<String> configNames) {
        if (CollectionUtils.isNotEmpty(configNames)) {
            HSSFRow excelHeader = excelSheet.createRow(0);
            // 获取列头样式对
            HSSFCellStyle columnTopStyle = this.setHeaderStyle(workbook);
            int cellCnt = 0;
            for (String name : configNames) {
                HSSFCell cellTiltle = excelHeader.createCell(cellCnt);
                cellTiltle.setCellStyle(columnTopStyle);
                cellTiltle.setCellValue(name);
                cellCnt++;
            }
        }
    }


    /**
     * 列头单元格样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle setHeaderStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 9);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;
    }



}
