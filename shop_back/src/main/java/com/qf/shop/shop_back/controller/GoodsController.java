package com.qf.shop.shop_back.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.gson.Gson;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;


    @Reference
    private IGoodsService iGoodsService;

    @Value("${image.path}")
    private String path;

    @RequestMapping("/goodslist")
    public  String goodsManager(Model model){
        List<Goods> goods = iGoodsService.queryAll();
        model.addAttribute("goods",goods);
        model.addAttribute("path",path);
        return "goodslist";
    }

    @RequestMapping("/goodsadd")
    public String goodsAdd(@RequestParam("file") MultipartFile file, Goods goods) throws IOException {
        StorePath jpg = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), "JPG", null);
        //StorePath jpg = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "JPG", null);
        //StorePath spath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "JPG", null);
        String fullPath = jpg.getFullPath();
        goods.setGimage(fullPath);
        goods = iGoodsService.addGoods(goods);
        System.out.println("住建回填为"+goods.getId());
         HttpClientUtil.sendJsonPost("http://localhost:8083/solr/add", new Gson().toJson(goods));
         HttpClientUtil.sendJsonPost("http://localhost:8084/item/createhtml", new Gson().toJson(goods));
        System.out.println("回来了");
        return "redirect:/goods/goodslist";
    }

    @RequestMapping("/querynew")
    @ResponseBody
    @CrossOrigin
    public List<Goods> queryNewGoods(){
        List<Goods> goods = iGoodsService.queryNew();
        System.out.println("最新的四建商品为"+goods);
        return goods;
    }
}
