package com.example.htwodatabasetest.excel.model;

import com.example.htwodatabasetest.excel.util.DateUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * @ClassName: B1Data
 * @Author: shaofan.li
 * @Description: 数据对象
 * @Date: 2019/11/23 11:40
 */
public class B1DataBO implements Comparable<B1DataBO>{
    private String id;
    private String code;
    private String customerName;
    private String status;
    private String supplierName;
    private String addTime;
    private String tradeName;
    private String qty;
    private String sfQty;
    private String areaName;
    private String stDepartmentName;
    private String finance;
    private String weight;
    private String sfWeight;
    private String B2SettlementStatus;
    private String contacterTel;
    private String contacterName;
    private String remark;
    private String shouldReceiptedAmt;
    private String source;
    private String type;
    private String preReceiptedAmt;
    private String department;
    private String syb;
    private String canUseBT;
    private String creAmt;
    private String owner;

    private String lastModifyTime;
    private String partCompany;
    private String returnPayTime;
    private String ip;
    private String otherContractCode;
    private String b1SettlementTime;
    private String GYPayAmt;
    private String STYF;
    private String HY;
    private String KFRY;

    //差异数量
    private int diffQty;

    public int getDiffQty() {
        return diffQty;
    }

    public void setDiffQty(int diffQty) {
        this.diffQty = diffQty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSfQty() {
        return sfQty;
    }

    public void setSfQty(String sfQty) {
        this.sfQty = sfQty;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getStDepartmentName() {
        return stDepartmentName;
    }

    public void setStDepartmentName(String stDepartmentName) {
        this.stDepartmentName = stDepartmentName;
    }

    public String getFinance() {
        return finance;
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSfWeight() {
        return sfWeight;
    }

    public void setSfWeight(String sfWeight) {
        this.sfWeight = sfWeight;
    }

    public String getB2SettlementStatus() {
        return B2SettlementStatus;
    }

    public void setB2SettlementStatus(String b2SettlementStatus) {
        B2SettlementStatus = b2SettlementStatus;
    }

    public String getContacterTel() {
        return contacterTel;
    }

    public void setContacterTel(String contacterTel) {
        this.contacterTel = contacterTel;
    }

    public String getContacterName() {
        return contacterName;
    }

    public void setContacterName(String contacterName) {
        this.contacterName = contacterName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShouldReceiptedAmt() {
        return shouldReceiptedAmt;
    }

    public void setShouldReceiptedAmt(String shouldReceiptedAmt) {
        this.shouldReceiptedAmt = shouldReceiptedAmt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreReceiptedAmt() {
        return preReceiptedAmt;
    }

    public void setPreReceiptedAmt(String preReceiptedAmt) {
        this.preReceiptedAmt = preReceiptedAmt;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSyb() {
        return syb;
    }

    public void setSyb(String syb) {
        this.syb = syb;
    }

    public String getCanUseBT() {
        return canUseBT;
    }

    public void setCanUseBT(String canUseBT) {
        this.canUseBT = canUseBT;
    }

    public String getCreAmt() {
        return creAmt;
    }

    public void setCreAmt(String creAmt) {
        this.creAmt = creAmt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getPartCompany() {
        return partCompany;
    }

    public void setPartCompany(String partCompany) {
        this.partCompany = partCompany;
    }

    public String getReturnPayTime() {
        return returnPayTime;
    }

    public void setReturnPayTime(String returnPayTime) {
        this.returnPayTime = returnPayTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOtherContractCode() {
        return otherContractCode;
    }

    public void setOtherContractCode(String otherContractCode) {
        this.otherContractCode = otherContractCode;
    }

    public String getB1SettlementTime() {
        return b1SettlementTime;
    }

    public void setB1SettlementTime(String b1SettlementTime) {
        this.b1SettlementTime = b1SettlementTime;
    }

    public String getGYPayAmt() {
        return GYPayAmt;
    }

    public void setGYPayAmt(String GYPayAmt) {
        this.GYPayAmt = GYPayAmt;
    }

    public String getSTYF() {
        return STYF;
    }

    public void setSTYF(String STYF) {
        this.STYF = STYF;
    }

    public String getHY() {
        return HY;
    }

    public void setHY(String HY) {
        this.HY = HY;
    }

    public String getKFRY() {
        return KFRY;
    }

    public void setKFRY(String KFRY) {
        this.KFRY = KFRY;
    }

    @Override
    public int compareTo(B1DataBO o) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = DateUtil.strToDateLong(this.getAddTime());
            date2 = DateUtil.strToDateLong(o.getAddTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateUtil.compareDate(date2, date1);
    }
}
