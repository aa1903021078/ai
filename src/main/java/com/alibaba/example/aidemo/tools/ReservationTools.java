package com.alibaba.example.aidemo.tools;

import com.alibaba.example.aidemo.entity.Reservation;
import com.alibaba.example.aidemo.service.ReservationService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 预约相关工具，供 AI 调用
 */
@Component("reservationTools")
public class ReservationTools {

    private static final DateTimeFormatter[] TIME_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    };

    @Autowired
    private ReservationService reservationService;

    /**
     * 一对一志愿指导服务预约下单
     * 仅当用户完整提供所有信息时才调用
     */
    @Tool("高考志愿填报一对一沟通预约下单，仅当考生完整提供姓名、性别、手机号、预约沟通时间、省份、预估分数时才调用")
    public String bookReservation(
            @P("考生姓名") String name,
            @P("考生性别") String gender,
            @P("考生手机号") String phone,
            @P("考生预约沟通时间，格式 yyyy-MM-dd HH:mm") String communicationTime,
            @P("考生所在省份") String province,
            @P("考生预估分数") int estimatedScore) {

        Reservation reservation = new Reservation();
        reservation.setName(name);
        reservation.setGender(gender);
        reservation.setPhone(phone);
        reservation.setCommunicationTime(parseTime(communicationTime));
        reservation.setProvince(province);
        reservation.setEstimatedScore(estimatedScore);
        reservationService.save(reservation);

        return "预约成功，预约ID：" + reservation.getId();
    }

    /**
     * 查询预约详情：按手机号精确查，查不到再按姓名模糊查
     */
    @Tool("根据考生手机号或姓名查询志愿指导服务的预约详情")
    public String queryReservation(@P("考生手机号或姓名") String keyword) {

        Reservation reservation = findReservation(keyword);
        if (reservation == null) {
            return "未查询到相关预约记录";
        }

        return "预约详情：姓名=" + reservation.getName()
                + "，性别=" + reservation.getGender()
                + "，电话=" + reservation.getPhone()
                + "，沟通时间=" + reservation.getCommunicationTime()
                + "，省份=" + reservation.getProvince()
                + "，预估分数=" + reservation.getEstimatedScore();
    }

    /**
     * 修改预约信息：仅按手机号精确查找；仅更新考生提供的字段
     */
    @Tool("根据考生手机号修改志愿指导服务的预约信息，仅更新考生提供的字段（姓名、性别、手机号、沟通时间、省份、预估分数）")
    public String modifyReservation(
            @P("考生手机号，用于查找原预约") String phone,
            @P("新的考生姓名，不修改则传空") String name,
            @P("新的考生性别，不修改则传空") String gender,
            @P("新的考生手机号，不修改则传空") String newPhone,
            @P("新的预约沟通时间，格式 yyyy-MM-dd HH:mm，不修改则传空") String communicationTime,
            @P("新的考生所在省份，不修改则传空") String province,
            @P("新的考生预估分数，不修改则传空") Integer estimatedScore) {

        Reservation reservation = findByPhone(phone);
        if (reservation == null) {
            return "未查询到该手机号对应的预约记录，无法修改";
        }

        if (notBlank(name)) {
            reservation.setName(name.trim());
        }
        if (notBlank(gender)) {
            reservation.setGender(gender.trim());
        }
        if (notBlank(newPhone)) {
            reservation.setPhone(newPhone.trim());
        }
        if (notBlank(communicationTime)) {
            LocalDateTime time = parseTime(communicationTime);
            if (time == null) {
                return "沟通时间格式不正确，请使用 yyyy-MM-dd HH:mm 格式，预约未修改";
            }
            reservation.setCommunicationTime(time);
        }
        if (notBlank(province)) {
            reservation.setProvince(province.trim());
        }
        if (estimatedScore != null) {
            reservation.setEstimatedScore(estimatedScore);
        }

        reservationService.updateById(reservation);
        return "修改成功，预约ID：" + reservation.getId();
    }

    /**
     * 按手机号精确查，查不到再按姓名模糊查
     */
    private Reservation findReservation(String keyword) {
        Reservation reservation = findByPhone(keyword);
        if (reservation == null) {
            List<Reservation> byName = reservationService.lambdaQuery()
                    .like(Reservation::getName, keyword)
                    .orderByDesc(Reservation::getId)
                    .list();
            reservation = byName.isEmpty() ? null : byName.get(0);
        }
        return reservation;
    }

    /**
     * 仅按手机号精确查找
     */
    private Reservation findByPhone(String phone) {
        List<Reservation> byPhone = reservationService.lambdaQuery()
                .eq(Reservation::getPhone, phone)
                .orderByDesc(Reservation::getId)
                .list();
        return byPhone.isEmpty() ? null : byPhone.get(0);
    }

    private boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private LocalDateTime parseTime(String time) {
        for (DateTimeFormatter formatter : TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(time.trim(), formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
