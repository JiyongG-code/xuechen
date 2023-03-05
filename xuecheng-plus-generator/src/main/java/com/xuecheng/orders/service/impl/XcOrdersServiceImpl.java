package com.xuecheng.orders.service.impl;

import com.xuecheng.orders.model.po.XcOrders;
import com.xuecheng.orders.mapper.XcOrdersMapper;
import com.xuecheng.orders.service.XcOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class XcOrdersServiceImpl extends ServiceImpl<XcOrdersMapper, XcOrders> implements XcOrdersService {

}
