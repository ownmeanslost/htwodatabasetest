package com.example.htwodatabasetest.excel.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * 原生读取
 *
 * @author hua.huang
 */
public class POIReadExcelToHtml {

    /**
     * 系统支持的字体文件
     */
    private static final Map<String, String> FONT_SUPPORTED_MAP = new HashMap();
    
    static {
        FONT_SUPPORTED_MAP.put("SimSun", "SimSun");
        FONT_SUPPORTED_MAP.put("宋体", "SimSun");
        FONT_SUPPORTED_MAP.put("微软雅黑", "Microsoft YaHei");
        FONT_SUPPORTED_MAP.put("Microsoft YaHei", "Microsoft YaHei");
    }

    /**
     * 默认字体：宋体
     */
    private static final String DEFAULT_FONT = "SimSun";
    
    private static final Logger logger = LoggerFactory.getLogger(POIReadExcelToHtml.class);

    /**
     * Excel模板文件与数据转为html文件
     * @param is
     * @param isExcel2003
     * @param mainReplaceMap
     * @param dataRowReplaceMaps
     * @param maxCountPerPage
     * @param imgFlagText2Base64ReplaceMap
     * @return 
     * @throws Exception
     */
    public static String convert(InputStream is, boolean isExcel2003, Map<String, String> mainReplaceMap,
            List<Map<String, String>> dataRowReplaceMaps, int maxCountPerPage,
            Map<String, String> imgFlagText2Base64ReplaceMap) throws Exception{

        // <editor-fold desc="STEP1: 对dataRowReplaceMaps明细数据进行合计运算">
        Map<String, String> sumMap = new HashMap<>();
        for (Map<String, String> child : dataRowReplaceMaps) {
            // 遍历列
            for (Map.Entry<String, String> entry : child.entrySet()) {
                try {
                    String key = entry.getKey();
                    key = key.replace("{{", "").replace("}}", "");
                    String val = entry.getValue().trim();
                    String sumMapKey = "{{sum(" + key + ")}}";
                    if (!val.isEmpty()) {
                        if (sumMap.containsKey(sumMapKey)) {
                            String oldVal = sumMap.get(sumMapKey);
                            BigDecimal num1 = new BigDecimal(oldVal);
                            BigDecimal num2 = new BigDecimal(val);
                            BigDecimal result = num1.add(num2);
                            String newVal = result.toString();
                            sumMap.put(sumMapKey, newVal);
                        } else {
                            BigDecimal num2 = new BigDecimal(val);
                            sumMap.put(sumMapKey, num2.toString());
                        }
                    }
                } catch (Exception e) {
                    logger.error("质保书进行行数据合计期间发生异常！", e);
                }
            }
        }
        // </editor-fold>

        // <editor-fold desc="STEP2: 明细数据按照行号统一定义变量替换">
        Map<String, String> detailRowReplaceMap = new HashMap();
        int serialNumber = 1;
        for(Map<String, String> rowMap : dataRowReplaceMaps) {
            Set<String> fieldSet = rowMap.keySet();
            for(String field : fieldSet) {
                String fieldName = field.substring(2, field.indexOf("}}"));
                detailRowReplaceMap.put("{{[" + serialNumber + "]" + fieldName + "}}", rowMap.get(field));
            }
            serialNumber++;
        }
        // </editor-fold>

        // <editor-fold desc="STEP3: 遍历Excel模板首个Sheet的所有行(逆向)，确定数据行的位置，并确定最大有效行号rowIndex和ColumnIndex最大有效列号">
        Workbook wb = isExcel2003 ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
        Sheet sheet = wb.getSheetAt(0);
        int maxColIndex = 0;    // 记录
        int maxValidRowIndex = 0;
        for (int rIndex = sheet.getLastRowNum(); rIndex >= 0; rIndex--) {
            Row row = sheet.getRow(rIndex);
            if (row == null) {
                continue;
            }
            for (int cIndex = row.getFirstCellNum(); cIndex <= row.getLastCellNum(); cIndex++) {
                Cell cell = row.getCell(cIndex);
                if (cell == null) {
                    continue;
                }
                // 非空行，且找到最后列的Cell单元格
                if(rIndex > maxValidRowIndex) {
                    maxValidRowIndex = rIndex;
                }
                if(cIndex > maxColIndex) {
                    maxColIndex = cIndex;
                }
            }
        }
        // </editor-fold>

        // <editor-fold desc="STEP4: 计算列宽并拼接html中table的colgroup定义">
        StringBuilder sbCol = new StringBuilder();
        sbCol.append("<colgroup>");
        int tableWidth = 0;
        int tableWidthPT = 0;
        for(int colIndex = 0; colIndex <= maxColIndex; colIndex++) {
            int colWidth = Math.round(sheet.getColumnWidth(colIndex) * 8 / 256);
            int colWidthPT = Math.round(sheet.getColumnWidth(colIndex) * 6 / 256);
            sbCol.append("<col width=\"").append(colWidth).append("\" style=\"mso-width-source:userset;width:").append(colWidthPT).append("pt\"/>");
            tableWidth += colWidth;
            tableWidthPT += colWidthPT;
        }
        sbCol.append("</colgroup>");
        // </editor-fold>

        // <editor-fold desc="STEP5: 拼接table等">
        StringBuilder sbTable = new StringBuilder();
        // table标签：指定固定宽度（不被内容撑开），换行方式：允许单词内换行，溢出部分隐藏，总宽度100%
        sbTable.append("<table width=\"").append(tableWidth).append("\" style='")
                .append("border-collapse:collapse;")
                .append("table-layout:fixed;")
                .append("overflow:hidden;")
                .append("width:").append(tableWidthPT).append("pt;'>");
        sbTable.append(sbCol.toString());
        sbTable.append("<tbody>");
        // </editor-fold>

        // <editor-fold desc="STEP6: 遍历行进行html转换">
        Map<String, String> cssMap = new HashMap(); // 用于存放单元格的css，进行css公用定义
        Map<String, String> rowSpanColSpanMap[] = getRowSpanColSpanMap(sheet);  // 获取sheet页内所有的单元格合并信息
        for (int rowNum = sheet.getFirstRowNum(); rowNum <= maxValidRowIndex; rowNum++) {
            // 明细行处理，内含数据变量替换
            parserRow(wb, sheet, sheet.getRow(rowNum), sbTable, rowSpanColSpanMap, cssMap, mainReplaceMap, detailRowReplaceMap, sumMap);
        }
        // </editor-fold>



        sbTable.append("</tbody></table>");

        StringBuilder sbHtml = new StringBuilder();
        // html标签
        sbHtml.append("<html>");
        // head标签：指定打印页尺寸A4、指定文件编码UTF-8，指定Content-Type：html
        sbHtml.append("<head>")
                .append("<style>@page {size: 297mm 210mm; margin: 0;}</style>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        sbHtml.append("<style type=\"text/css\">");
        Set<String> cssSet = cssMap.keySet();
        for(String css : cssSet) {
            sbHtml.append("\r");
            sbHtml.append(".").append(cssMap.get(css)).append(" {").append(css).append("}");
        }
        sbHtml.append("</style>")
                .append("</head>")
                .append("\r");
        // body标签：指定默认字体：宋体
        sbHtml.append("<body style='font-family:")
                .append(DEFAULT_FONT)
                .append(";'>");
        sbHtml.append(sbTable.toString());
        // Excel模板中的图片
        sbHtml.append(getPictureImgTag(sheet));
        // Excel特定内容上动态签章图片
        sbHtml.append(getReplaceImgTag(sheet, imgFlagText2Base64ReplaceMap));
        sbHtml.append("</body></html>");
        return sbHtml.toString();
    }

    /**
     * 替换单元格中的变量值
     * 备注：单元格中可能存在多个变量，比如{{A}}/{{B}}
     * @param srcValue
     * @param mainReplaceMap
     * @param dataRowReplaceMap
     * @param sumMap
     * @return
     */
    private static String replaceCellValue(String srcValue, Map<String, String> mainReplaceMap, Map<String, String> dataRowReplaceMap, Map<String, String> sumMap) {
        if(srcValue != null && srcValue.contains("{{") && srcValue.contains("}}")) {
            List<String> repaceList = findAllReplaceVar(srcValue);
            for(String replaceStr : repaceList) {
                if(dataRowReplaceMap != null && dataRowReplaceMap.containsKey(replaceStr)) {
                    srcValue = srcValue.replace(replaceStr, dataRowReplaceMap.get(replaceStr));
                } else if(sumMap.containsKey(replaceStr)){
                    srcValue = srcValue.replace(replaceStr, sumMap.get(replaceStr));
                } else if(mainReplaceMap.containsKey(replaceStr)) {
                    srcValue = srcValue.replace(replaceStr, mainReplaceMap.get(replaceStr));
                } else {
                    srcValue = srcValue.replace(replaceStr,"");
                }
            }
        }
        return srcValue;
    }

    public static List<String> findAllReplaceVar(String srcValue) {
        String toSplitStr = srcValue;
        List<String> varList = new ArrayList();
        while(toSplitStr != null && toSplitStr.contains("{{") && toSplitStr.contains("}}")) {
            int firstStartIndex = toSplitStr.indexOf("{{");
            int firstEndIndex = toSplitStr.indexOf("}}");
            if(firstEndIndex < firstStartIndex) {
                // 遇到不合法的定义，终止解析
                break;
            }
            String replaceStr = toSplitStr.substring(firstStartIndex, firstEndIndex + 2);
            toSplitStr = toSplitStr.replace(replaceStr, "");
            varList.add(replaceStr);
        }
        return varList;
    }

    /**
     * 行解析转换
     *
     * @param row
     * @param sb
     */
    private static void parserRow(Workbook wb, Sheet sheet, Row row, StringBuilder sb, Map<String, String>[] rowSpanColSpanMap, Map<String, String> cssMap,
            Map<String, String> mainReplaceMap, Map<String, String> dataRowReplaceMap, Map<String, String> sumReplaceMap) {
        // 兼容null行
        if (row == null) {
            return;
        }
        int lastColNum = row.getLastCellNum();
        // 防止最后读取到空行
        if(lastColNum == 0) {
            return;
        }
        // 获取行高度
        int heightPT = Math.round(row.getHeightInPoints());
        int heightNumber = Math.round(row.getHeightInPoints() + row.getHeightInPoints()/3);
        sb.append("<tr height=\"").append(heightNumber).append("\" style=\"mso-height-source:userset;height:").append(heightPT).append("pt\">");
        // 遍历td
        int serialNumberCount = 0;
        String serialNumber = "";
        for (int colNum = 0; colNum < lastColNum; colNum++) {
            Cell cell = row.getCell(colNum);
            if(cell != null) {
                String cellValue = getCellValue(cell);
                if(cellValue.contains("]serialNumber}}")) {
                    serialNumber = cellValue.substring(cellValue.indexOf("{{[") + 3, cellValue.indexOf("]"));
                    serialNumberCount ++;
                }
            }
        }
        boolean clearAllRowCell = ! ( serialNumberCount != 1 || dataRowReplaceMap.containsKey("{{[" + serialNumber + "]serialNumber}}") );
        for (int colNum = 0; colNum < lastColNum; colNum++) {
            parserCell(wb, sheet, row, row.getCell(colNum), sb, rowSpanColSpanMap, cssMap, mainReplaceMap, dataRowReplaceMap, sumReplaceMap, clearAllRowCell);
        }
        sb.append("</tr>");
    }

    private static void parserCell(Workbook wb, Sheet sheet, Row row, Cell cell, StringBuilder sb, Map<String, String>[] rowSpanColSpanMap, Map<String, String> cssMap,
            Map<String, String> mainReplaceMap, Map<String, String> dataRowReplaceMap, Map<String, String> sumReplaceMap, boolean clearAllRowCell) {
        // 兼容null单元格
        if (cell == null) {
            sb.append("<td></td>");
            return;
        }
        // 获取当前单元格的行号、列号
        int rowNum = cell.getRowIndex();
        int colNum = cell.getColumnIndex();
        Cell rightButtomCell = null;
        if (rowSpanColSpanMap[0].containsKey(rowNum + "," + colNum)) {
            // 合并单元格的左上单元格(需要对应创建td并指定rowspan和colspan)
            String endPoint = rowSpanColSpanMap[0].get(rowNum + "," + colNum);
            rowSpanColSpanMap[0].remove(rowNum + "," + colNum);
            int bottomeRow = Integer.valueOf(endPoint.split(",")[0]);
            int bottomeCol = Integer.valueOf(endPoint.split(",")[1]);
            int rowSpan = bottomeRow - rowNum + 1;
            int colSpan = bottomeCol - colNum + 1;
            sb.append("<td ");
            if(rowSpan > 1) {
                sb.append("rowspan=\"").append(rowSpan).append("\" ");
            }
            if(colSpan > 1) {
                sb.append("colspan=\"").append(colSpan).append("\" ");
            }
            rightButtomCell = sheet.getRow(bottomeRow).getCell(bottomeCol);
        } else if (rowSpanColSpanMap[1].containsKey(rowNum + "," + colNum)) {
            // 合并单元格内的非左上单元格（无需创建td对应标签）
            rowSpanColSpanMap[1].remove(rowNum + "," + colNum);
            return;
        } else {
            // 非合并单元格
            sb.append("<td ");
        }

        // 处理单元格样式
        getCellStyle(wb, cell, sb, rightButtomCell, cssMap);
        sb.append(">");
        if(clearAllRowCell) {
            sb.append(" &nbsp; ");
        } else {
            String stringValue = getCellValue(cell);
            stringValue = replaceCellValue(stringValue, mainReplaceMap, dataRowReplaceMap, sumReplaceMap);
            if (stringValue == null || "".equals(stringValue.trim())) {
                sb.append(" &nbsp; ");
            } else {
                // 将ascii码为160的空格转换为html下的空格（&nbsp;）
                //stringValue = HTMLFormatUtil.transUnSupportChars(HtmlUtils.htmlEscape(stringValue));
                sb.append(stringValue.replace(String.valueOf((char) 160), "&nbsp;"));
            }
        }

        sb.append("</td>");
    }

    /**
     * 获取Sheet内所有单元格合并信息
     *
     * @param sheet
     * @return
     */
    private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
        Map<String, String> map0 = new HashMap();
        Map<String, String> map1 = new HashMap();
        int mergedNum = sheet.getNumMergedRegions();
        for (int i = 0; i < mergedNum; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int topRow = range.getFirstRow();
            int topCol = range.getFirstColumn();
            int bottomRow = range.getLastRow();
            int bottomCol = range.getLastColumn();
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = {map0, map1};
        return map;
    }

    /**
     * 获取单元格内容
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            // 数字类型
            case Cell.CELL_TYPE_NUMERIC:
                // 处理日期格式、时间格式
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    return sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil
                            .getJavaDate(value);
                    return sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规  
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    return format.format(value);
                }
            // String类型 
            case Cell.CELL_TYPE_STRING: 
                return cell.getRichStringCellValue().toString();
            // 空类型
            case Cell.CELL_TYPE_BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * 获取单元格样式
     *
     * @param wb
     * @param cell
     * @param sb
     * @param rightButtomCell 右下角单元格（当前单元格为合并单元格时传此参数，否则为空）
     */
    private static void getCellStyle(Workbook wb, Cell cell, StringBuilder sb, Cell rightButtomCell, Map<String, String> cssMap) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle == null) {
            return;
        }
        StringBuilder cssSB = new StringBuilder();
        // ---------------------------- 水平对齐方式 ---------------------------
        short alignment = cellStyle.getAlignment();
        sb.append("align='").append(convertAlignToHtml(alignment)).append("' ");
        // ---------------------------- 垂直对齐方式 ---------------------------
        short verticalAlignment = cellStyle.getVerticalAlignment();
        sb.append("valign='").append(convertVerticalAlignToHtml(verticalAlignment)).append("' ");
        if (wb instanceof XSSFWorkbook) {
            // ------------------------------- 字体 ----------------------------
            XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
            // 字体名称
            String fontName = xf.getFontName();
            if(FONT_SUPPORTED_MAP.containsKey(fontName) && !DEFAULT_FONT.equals(FONT_SUPPORTED_MAP.get(fontName))) {
                // 字体如果被支持，且不是默认字体，则单独设置单元格字体
                cssSB.append("font-family:").append(FONT_SUPPORTED_MAP.get(fontName)).append(";"); 
            }
            // 字体加粗
            cssSB.append("font-weight:").append(xf.getBoldweight()).append(";"); 
            // 字体大小
            cssSB.append("font-size: ").append(xf.getFontHeight() / 2).append("%;"); 
            // 字体颜色
            XSSFColor xc = xf.getXSSFColor();
            if (xc != null && !"".equals(xc)) {
                cssSB.append(xc.getARGBHex().substring(2)).append("color:#;");
            }
            // ------------------------------- 列宽 ----------------------------
//            sb.append(" width=\"").append(Math.round(columnWidth * 8 /256)).append("px\"");
            // ----------------------------- 背景颜色 --------------------------
            XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
            if (bgColor != null && !"".equals(bgColor)) {
                cssSB.append("background-color:#").append(bgColor.getARGBHex().substring(2)).append(";"); // 背景颜色
            }
            // ----------------------------- 边框 --------------------------
            if(cellStyle.getBorderTop() != CellStyle.BORDER_NONE || rightButtomCell == null || rightButtomCell.getCellStyle() == null) {
                cssSB.append(getBorderStyle(0, cellStyle.getBorderTop(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
            } else {
            }
            
            cssSB.append(getBorderStyle(1, cellStyle.getBorderRight(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
            cssSB.append(getBorderStyle(2, cellStyle.getBorderBottom(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
            cssSB.append(getBorderStyle(3, cellStyle.getBorderLeft(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));
        
        } else if (wb instanceof HSSFWorkbook) {
            // ------------------------------- 字体 ----------------------------
            HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
            // 字体名称
            String fontName = hf.getFontName();
            if(FONT_SUPPORTED_MAP.containsKey(fontName) && !DEFAULT_FONT.equals(FONT_SUPPORTED_MAP.get(fontName))) {
                // 字体如果被支持，且不是默认字体，则单独设置单元格字体
                cssSB.append("font-family:").append(FONT_SUPPORTED_MAP.get(fontName)).append(";"); 
            }
            // 字体加粗
            cssSB.append("font-weight:").append(hf.getBoldweight()).append(";");
            // 字体大小
            cssSB.append("font-size: ").append(hf.getFontHeight() / 2).append("%;");
            // 字体颜色
            HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
            short fontColor = hf.getColor();
            HSSFColor hc = palette.getColor(fontColor);
            String fontColorStr = convertToStardColor(hc);
            if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
                cssSB.append("color:").append(fontColorStr).append(";");
            }
            // ----------------------------- 背景颜色 --------------------------
            short bgColor = cellStyle.getFillForegroundColor();
            hc = palette.getColor(bgColor);
            String bgColorStr = convertToStardColor(hc);
            if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
                cssSB.append("background-color:").append(bgColorStr).append(";"); // 背景颜色
            }
            // ------------------------------- 边框 ----------------------------
            // 上边框
            cssSB.append(getBorderStyle(palette, 0, cellStyle.getBorderTop(), cellStyle.getTopBorderColor()));
            // 右边框
            if(cellStyle.getBorderRight() != CellStyle.BORDER_NONE ||  rightButtomCell == null || rightButtomCell.getCellStyle() == null) {
                cssSB.append(getBorderStyle(palette, 1, cellStyle.getBorderRight(), cellStyle.getRightBorderColor()));
            } else {
                // 合并单元格的右边框取右下角的单元格右边框【poi bug修复】
                cssSB.append(getBorderStyle(palette, 1, rightButtomCell.getCellStyle().getBorderRight(), rightButtomCell.getCellStyle().getRightBorderColor()));
            }
            // 下边框
            if(cellStyle.getBorderBottom() != CellStyle.BORDER_NONE ||  rightButtomCell == null || rightButtomCell.getCellStyle() == null) {
                cssSB.append(getBorderStyle(palette, 2, cellStyle.getBorderBottom(), cellStyle.getBottomBorderColor()));
            } else {
                // 合并单元格的下边框取右下角的单元格下边框【poi bug修复】
                cssSB.append(getBorderStyle(palette, 2, rightButtomCell.getCellStyle().getBorderBottom(), rightButtomCell.getCellStyle().getBottomBorderColor()));
            }
            // 左边框
            cssSB.append(getBorderStyle(palette, 3, cellStyle.getBorderLeft(), cellStyle.getLeftBorderColor()));
        }
        String css = cssSB.toString();
        if(!cssMap.containsKey(css)) {
            String cssName = "c" + cssMap.size();
            cssMap.put(css, cssName);
        }
        sb.append(" class=\"").append(cssMap.get(css)).append("\" ");
    }

    /**
     * 单元格内容的水平对齐方式
     *
     * @param alignment
     * @return
     */
    private static String convertAlignToHtml(short alignment) {

        String align = "left";
        switch (alignment) {
            case CellStyle.ALIGN_LEFT:
                align = "left";
                break;
            case CellStyle.ALIGN_CENTER:
                align = "center";
                break;
            case CellStyle.ALIGN_RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }

    /**
     * 单元格中内容的垂直排列方式
     *
     * @param verticalAlignment
     * @return
     */
    private static String convertVerticalAlignToHtml(short verticalAlignment) {

        String valign = "middle";
        switch (verticalAlignment) {
            case CellStyle.VERTICAL_BOTTOM:
                valign = "bottom";
                break;
            case CellStyle.VERTICAL_CENTER:
                valign = "center";
                break;
            case CellStyle.VERTICAL_TOP:
                valign = "top";
                break;
            default:
                break;
        }
        return valign;
    }

    /**
     * 获取#格式的颜色
     * 
     */
    private static String convertToStardColor(HSSFColor hc) {
        StringBuilder sb = new StringBuilder("");
        if (hc != null) {
            if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
            }
        }
        return sb.toString();
    }

    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }

    static String[] bordesr = {"border-top:", "border-right:", "border-bottom:", "border-left:"};
    // 顺序：{无， 最细实线， 中等实线， 最细-虚线， 最细.虚线， 最粗实线， 双层实线， 最细、虚线， 中等-虚线， 最细-.虚线， 中等-.虚线， 最细-..虚线， 中等-..虚线， 中等-.虚线}
    static String[] borderStyles = {"none ", "solid 1px", "solid 1.5px", "solid 1px", "solid 1px", "solid 2px", "solid 2px", "solid 1px", "solid 1.5px", "solid 1px", "solid 1.5px", "solid 1px", "solid 1.5px", "solid 1.5px"};

    /**
     * 获取边框样式
     * 
     * @param palette
     * @param b 边框序号值（0 上， 1 右； 2 下； 3 左）
     * @param s  边框线型序号值
     * @param t 边框颜色序号值
     * @return 
     */
    private static String getBorderStyle(HSSFPalette palette, int b, short s, short t) {
        // 无边框
        if (s == CellStyle.BORDER_NONE) {
            return bordesr[b] + "none;";
        };
        // 有边框（边框宽度、线型【暂时都定为实线】、颜色）
        String borderColorStr = convertToStardColor(palette.getColor(t));
        borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
        return bordesr[b] + borderStyles[s] + borderColorStr + ";";

    }

    /**
     * 获取边框样式
     * 
     * @param b
     * @param s
     * @param xc
     * @return 
     */
    private static String getBorderStyle(int b, short s, XSSFColor xc) {
        if (s == 0) {
            return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        };
        if (xc != null && !"".equals(xc)) {
            String borderColorStr = xc.getARGBHex();//t.getARGBHex();
            borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr.substring(2);
            return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
        }

        return "";
    }
    
    /**
     * 获取sheet内嵌图片
     * @param sheet
     * @return 
     */
    private static String getPictureImgTag(Sheet sheet) {
        if(sheet instanceof HSSFSheet) {
            return getHSSFPictureImgTag((HSSFSheet) sheet);
        } else {
            return getXSSFPictureImgTag((XSSFSheet) sheet);
        }
    }
    
    /**
     * 获取sheet中特定标识内容需要加盖的印章图片
     * @param sheet
     * @param imgFlagText2Base64ReplaceMap
     * @return 
     */
    private static String getReplaceImgTag(Sheet sheet, Map<String, String> imgFlagText2Base64ReplaceMap) {
        if(imgFlagText2Base64ReplaceMap == null || imgFlagText2Base64ReplaceMap.keySet().isEmpty()) {
            return "";
        }
        // 遍历检索需要替换的内容
        String img = "";
        long heightPT = 0;
        for(int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex ++) {
            Row row = sheet.getRow(rowIndex);
            if(row == null) {
                heightPT += sheet.getDefaultRowHeightInPoints();
                continue;
            }
            
            for(short cellIndex = row.getFirstCellNum(); cellIndex <= row.getLastCellNum(); cellIndex ++ ) {
                Cell cell = row.getCell(cellIndex);
                if(cell == null) {
                    continue;
                }
                String cellValue = getCellValue(cell);
                if(cellValue == null || cellValue.trim().isEmpty()) {
                    continue;
                }
                Set<String> flagTextSet = imgFlagText2Base64ReplaceMap.keySet();
                for(String flagText : flagTextSet) {
                    if(cellValue.contains(flagText)) {
                        // 计算Cell
                        long width = 0;
                        for(int idx = 0; idx < cellIndex; idx ++) {
                            width += sheet.getColumnWidth((int)idx);
                        }
                        long widthPT = Math.round( (double)width * 6 / 256);
                        img += "<img style=\"position:absolute; left:" + (widthPT - 10) + "pt;top:" + (heightPT - 30) + "pt;\" "
                            + " width=\"150px\" heignt=\"150px\" "
                            + " src=\"data:image/jpeg|png|gif;base64," + imgFlagText2Base64ReplaceMap.get(flagText) + "\" />";
                    }
                }
            }
            heightPT += row.getHeightInPoints();
        }
        return img;
    }
    
    /**
     * 获取Excel的Sheet页内的图片资源并组装为带有数据与定位信息的img标签
     * @param sheet
     * @return 
     */
    private static String getHSSFPictureImgTag(HSSFSheet sheet) {
        // 获取图形封装顶层对象
        HSSFPatriarch hssfPatriarch = sheet.getDrawingPatriarch();
        if(hssfPatriarch == null) {
            return "";
        }
        // 获取所有的图形元素
        List<HSSFShape> shapes = hssfPatriarch.getChildren();
        if(shapes == null || shapes.isEmpty()) {
            return "";
        }
        // 遍历图形获取图形数据域图形位置
        String img = "";
        for(HSSFShape shape : shapes) {
            // 重点关注HSSFPicture图片
            if(! (shape instanceof HSSFPicture) ) {
                continue;
            }
            HSSFPicture pic = (HSSFPicture) shape;
            // 图片数据
            HSSFPictureData pictureData = pic.getPictureData();
            
            String base64Str = Base64.encodeBase64String(pictureData.getData()).replaceAll("\n", "");
            // 图片定位
            HSSFClientAnchor anchor = (HSSFClientAnchor) pic.getAnchor(); 
            int idx_row1 = anchor.getRow1();    // 图片左上角所在行index，0开始
            short idx_col1 = anchor.getCol1();  // 图片左上角所在列index，0开始
            int idx_row2 = anchor.getRow2();    // 图片右下角所在行index，0开始
            short idx_col2 = anchor.getCol2();  // 图片右下角所在列index，0开始
            int dx1 = anchor.getDx1();      // 图片左上角距离所在cell单元格左侧的距离（单位：所在行宽度的1/1024）
            int dy1 = anchor.getDy1();      // 图片左上角距离所在cell单元格上侧的距离（单位：所在行高度的1/256）
            int dx2 = anchor.getDx2();      // 图片右下角距离所在cell单元格左侧的距离（单位：所在行宽度的1/1024）
            int dy2 = anchor.getDy2();      // 图片右下角距离所在cell单元格上侧的距离（单位：所在行高度的1/256）
            
            // 计算左上角X与Y坐标
            long startX = Math.round(( (double) dx1 * sheet.getColumnWidth((int)idx_col1) ) / 1024);
            for(int idx = 0; idx < idx_col1; idx ++) {
                startX += sheet.getColumnWidth((int)idx);
            }
            
            long startY = Math.round(( (double) dy1 * sheet.getRow(idx_row1).getHeight() ) / 256);
            for(int idx = 0; idx < idx_row1; idx ++) {
                Row row = sheet.getRow(idx);
                if(row == null) {
                    startY += sheet.getDefaultRowHeight();
                } else {
                    startY += row.getHeight();
                }
            }
            // 单位转换为px像素
            
            // 计算右下角X与Y坐标
            long endX = Math.round(( (double) dx2 * sheet.getColumnWidth((int)idx_col2) ) / 1024);
            for(int idx = 0; idx < idx_col2; idx ++) {
                endX += sheet.getColumnWidth((int)idx);
            }
            long endY = Math.round(( (double) dy2 * sheet.getRow(idx_row2).getHeight() ) / 256);
            for(int idx = 0; idx < idx_row2; idx ++) {
                Row row = sheet.getRow(idx);
                if(row == null) {
                    endY += sheet.getDefaultRowHeight();
                } else {
                    endY += row.getHeight();
                }
            }
            // 单位从1/20 pt转换为px像素
            long px_startX = Math.round(( ((double)startX) * 8)/256);
            long px_Width = Math.round(( ((double)(endX - startX) ) * 8)/256);
            long px_startY = Math.round(( ((double)startY ) * 96)/(20 * 72));
            long px_Height = Math.round(( ((double)(endY - startY) ) * 96)/(20 * 72));
            
            // 图片绝对定位，图片数据直接base64编码内嵌到img标签中（仅指定宽度，高度自适应）
            img += "<img style=\"position:absolute; left:" + px_startX + "px;top:" + px_startY + "px;\" "
                        + " width=\"" + px_Width + "px\" heignt=\"" + px_Height + "px\" "
                        + " src=\"data:image/jpeg|png|gif;base64," + base64Str + "\" />";
            
        }
        return img;
    }
    
    /**
     * 获取Excel的Sheet页内的图片资源并组装为带有数据与定位信息的img标签
     * @param sheet
     * @return 
     */
    private static String getXSSFPictureImgTag(XSSFSheet sheet) {
        // 转换图片
        String img = "";
        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if(! (dr instanceof XSSFDrawing)) {
                continue;
            }
            XSSFDrawing drawing = (XSSFDrawing) dr;  
            List<XSSFShape> shapes = drawing.getShapes();  
            for (XSSFShape shape : shapes) {  
                XSSFPicture pic = (XSSFPicture) shape;
                // 图片定位
//                XSSFClientAnchor anchor = (XSSFClientAnchor)pic.getAnchor();
                XSSFClientAnchor anchor = pic.getPreferredSize();
                int idx_row1 = anchor.getRow1();    // 图片左上角所在行index，0开始
                short idx_col1 = anchor.getCol1();  // 图片左上角所在列index，0开始
                int idx_row2 = anchor.getRow2();    // 图片右下角所在行index，0开始
                short idx_col2 = anchor.getCol2();  // 图片右下角所在列index，0开始
                int dx1 = anchor.getDx1();      // 图片左上角距离所在cell单元格左侧的距离
                int dy1 = anchor.getDy1();      // 图片左上角距离所在cell单元格上侧的距离
                int dx2 = anchor.getDx2();      // 图片右下角距离所在cell单元格左侧的距离
                int dy2 = anchor.getDy2();      // 图片右下角距离所在cell单元格上侧的距离

                // 计算左上角X与Y坐标
                int startX = 0;
                for(int idx = 0; idx < idx_col1; idx ++) {
                    startX += sheet.getColumnWidth((int)idx);
                }
                int startY = 0;
                for(int idx = 0; idx < idx_row1; idx ++) {
                    startY += sheet.getRow(idx).getHeight();
                }
                // 单位转换为px像素

                // 计算右下角X与Y坐标
                int endX = 0;
                for(int idx = 0; idx < idx_col2; idx ++) {
                    endX += sheet.getColumnWidth((int)idx);
                }
                int endY = 0;
                for(int idx = 0; idx < idx_row2; idx ++) {
                    endY += sheet.getRow(idx).getHeight();
                }
                // 单位从1/20 pt转换为px像素
                long px_startX = Math.round(( ((double)startX + dx1 * 2.72) * 8)/256);
                long px_Width = Math.round(( ((double)(endX - startX) + (dx2 - dx1) *2.72 ) * 8)/256);
                long px_startY = Math.round(( ((double)startY + dy1 * 1.12) * 96)/(20 * 72));
                long px_Height = Math.round(( ((double)(endY - startY) + (dy2 - dy1)*1.12 ) * 96)/(20 * 72));

                // 图片数据
                XSSFPictureData pictureData = pic.getPictureData();
                String base64Str = Base64.encodeBase64String(pictureData.getData()).replaceAll("\n", "");
                
                // 图片绝对定位，图片数据直接base64编码内嵌到img标签中
                img += "<img style=\"position:absolute; left:" + px_startX + "px;top:" + px_startY + "px;\" "
                            + " width=\"" + px_Width + "px\" height=\"" + px_Height + "px\" " 
                            + " src=\"data:image/jpeg|png|gif;base64," + base64Str + "\" />";
            }
        }
        return img;
    }
    
    private static String ConvertCellStr(Cell cell) {
        String returnstr = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                // 读取String
                returnstr = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                // 得到Boolean对象的方法
                returnstr = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                // 先看是否是日期格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 读取日期格式
                    returnstr = formatTime(cell.getDateCellValue().toString());
                } else {
                    DecimalFormat df = new DecimalFormat("#.######");
                    // 读取数字
                    returnstr = df.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                // 读取公式
                returnstr = cell.getCellFormula();
                break;
        }
        return returnstr;
    }
    
    private static String formatTime(String s) {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sf.parse(s);
        } catch (ParseException ex) {
            logger.error("日期格式化异常！", ex);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
