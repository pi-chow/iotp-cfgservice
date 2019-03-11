package com.cetiti.ddapv2.iotplatform.biz.domain;


import com.cetiti.iotpcfgservice.domain.BaseDomain;

public class DeviceAttr extends BaseDomain {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String sn;
    private Integer ver;
    private String k;
    private String v;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
