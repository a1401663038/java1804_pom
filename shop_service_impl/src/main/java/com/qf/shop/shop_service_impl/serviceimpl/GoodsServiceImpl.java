package com.qf.shop.shop_service_impl.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.shop.dao.IGoodsDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public List<Goods> queryAll() {
        System.out.println("调用了Service实现类");
        List<Goods> goods = iGoodsDao.queryAll();
        return goods;
    }
}
