package com.qf.shop.shop_item.controller;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {
    //静态页
    @Autowired
    private Configuration configuration;

    @RequestMapping("/createhtml")
    public  String createHtml(@RequestBody Goods goods,HttpServletRequest request){
        System.out.println("模板进来了");
        Writer out=null;
        try {
            //获得模板
            Template template = configuration.getTemplate("item.ftl");
            Map<String,Object>map=new HashMap<>();
            map.put("goods",goods);
            map.put("context",request.getContextPath());
            //获得classPath路径
            String path= this.getClass().getResource("/").getPath() + "static/page/" +goods.getId()+".html";
            //输出路径
            out=new FileWriter(path);
            //模板生成在哪里
            System.out.println("打算出去了");
            System.out.println("classPath为"+path);
            System.out.println("context为1"+request.getContextPath());
            System.out.println("context为2"+this.getClass().getResource("/").getPath()+"resource/");
            template.process(map,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
