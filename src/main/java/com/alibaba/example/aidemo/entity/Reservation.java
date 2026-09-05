package com.alibaba.example.aidemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 考生预约表
 */
@TableName("reservation")
public class Reservation {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 考生姓名
     */
    private String name;

    /**
     * 考生性别
     */
    private String gender;

    /**
     * 考生手机号
     */
    private String phone;

    /**
     * 沟通时间
     */
    private LocalDateTime communicationTime;

    /**
     * 考生所处的省份
     */
    private String province;

    /**
     * 考生预估分数
     */
    private Integer estimatedScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCommunicationTime() {
        return communicationTime;
    }

    public void setCommunicationTime(LocalDateTime communicationTime) {
        this.communicationTime = communicationTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getEstimatedScore() {
        return estimatedScore;
    }

    public void setEstimatedScore(Integer estimatedScore) {
        this.estimatedScore = estimatedScore;
    }
}
