package com.qf.shop.shop_back.controller;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {

    @RequestMapping("/")
    public  String  welcome(){
        return "index";
    }

    @RequestMapping("/topage/{pagename}")
    public  String toPage(@PathVariable("pagename") String pagename){

        return pagename;
    }
}
