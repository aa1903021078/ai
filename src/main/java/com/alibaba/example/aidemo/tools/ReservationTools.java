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

        List<Reservation> byPhone = reservationService.lambdaQuery()
                .eq(Reservation::getPhone, keyword)
                .orderByDesc(Reservation::getId)
                .list();
        Reservation reservation = byPhone.isEmpty() ? null : byPhone.get(0);

        if (reservation == null) {
            List<Reservation> byName = reservationService.lambdaQuery()
                    .like(Reservation::getName, keyword)
                    .orderByDesc(Reservation::getId)
                    .list();
            reservation = byName.isEmpty() ? null : byName.get(0);
        }

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
