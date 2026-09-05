package com.alibaba.example.aidemo.service.impl;

import com.alibaba.example.aidemo.dao.ReservationMapper;
import com.alibaba.example.aidemo.entity.Reservation;
import com.alibaba.example.aidemo.service.ReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 考生预约表 Service 实现
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {
}
