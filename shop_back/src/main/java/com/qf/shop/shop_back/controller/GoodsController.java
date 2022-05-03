package com.qf.shop.shop_back.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private IGoodsService iGoodsService;

    @RequestMapping("/goodslist")
    public  String goodsManager(){
        System.out.println("进来了");
        List<Goods> goods = iGoodsService.queryAll();
        System.out.println("查询到的所有商品"+goods);
        return "goodslist";
    }
}
