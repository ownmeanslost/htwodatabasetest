package com.example.htwodatabasetest.unit.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="UNIT")
public class UnitPO implements Serializable {
    public static final String PKID = "PKID";

    @Id
    private Long pkid;

    public static final String IS_DELETE = "IS_DELETE";

    /**
     * 逻辑删除
     */
    @Column(name="IS_DELETE")
    private String isDelete;

    public static final String ADDED_TIME = "ADDED_TIME";

    /**
     * 创建时间
     */
    @Column(name="ADDED_TIME")
    private Date addedTime;

    public static final String ADDED_BY_CODE = "ADDED_BY_CODE";

    /**
     * 新增人编号
     */
    @Column(name="ADDED_BY_CODE")
    private String addedByCode;

    public static final String ADDED_BY_NAME = "ADDED_BY_NAME";

    /**
     * 新增人名称
     */
    @Column(name="ADDED_BY_NAME")
    private String addedByName;

    public static final String ADDED_BY_IP = "ADDED_BY_IP";

    /**
     * 新增人IP
     */
    @Column(name="ADDED_BY_IP")
    private String addedByIp;

    public static final String LAST_HANDLE_TIME = "LAST_HANDLE_TIME";

    /**
     * 最后执行时间
     */
    @Column(name="LAST_HANDLE_TIME")
    private Date lastHandleTime;

    public static final String LAST_MODIFIED_TIME = "LAST_MODIFIED_TIME";

    /**
     * 最后修改时间
     */
    @Column(name="LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    public static final String LAST_MODIFIED_BY_CODE = "LAST_MODIFIED_BY_CODE";

    /**
     * 最后修改人编号
     */
    @Column(name="LAST_MODIFIED_BY_CODE")
    private String lastModifiedByCode;

    public static final String LAST_MODIFIED_BY_NAME = "LAST_MODIFIED_BY_NAME";

    /**
     * 最后修改人名称
     */
    @Column(name="LAST_MODIFIED_BY_NAME")
    private String lastModifiedByName;

    public static final String LAST_MODIFIED_BY_IP = "LAST_MODIFIED_BY_IP";

    /**
     * 最后修改人IP
     */
    @Column(name="LAST_MODIFIED_BY_IP")
    private String lastModifiedByIp;

    private static final long serialVersionUID = 1L;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }

    public String getAddedByCode() {
        return addedByCode;
    }

    public void setAddedByCode(String addedByCode) {
        this.addedByCode = addedByCode == null ? null : addedByCode.trim();
    }

    public String getAddedByName() {
        return addedByName;
    }

    public void setAddedByName(String addedByName) {
        this.addedByName = addedByName == null ? null : addedByName.trim();
    }

    public String getAddedByIp() {
        return addedByIp;
    }

    public void setAddedByIp(String addedByIp) {
        this.addedByIp = addedByIp == null ? null : addedByIp.trim();
    }

    public Date getLastHandleTime() {
        return lastHandleTime;
    }

    public void setLastHandleTime(Date lastHandleTime) {
        this.lastHandleTime = lastHandleTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedByCode() {
        return lastModifiedByCode;
    }

    public void setLastModifiedByCode(String lastModifiedByCode) {
        this.lastModifiedByCode = lastModifiedByCode == null ? null : lastModifiedByCode.trim();
    }

    public String getLastModifiedByName() {
        return lastModifiedByName;
    }

    public void setLastModifiedByName(String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName == null ? null : lastModifiedByName.trim();
    }

    public String getLastModifiedByIp() {
        return lastModifiedByIp;
    }

    public void setLastModifiedByIp(String lastModifiedByIp) {
        this.lastModifiedByIp = lastModifiedByIp == null ? null : lastModifiedByIp.trim();
    }

    public UnitPO(long pkid) {
        this.pkid = pkid;
    }

    public UnitPO() {
    }
}