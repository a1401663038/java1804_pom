package com.qf.shop.shop_search.controller;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/solr")
public class SolrController {
    @Autowired
    private SolrClient solrClient;

    @RequestMapping("/add")
    @ResponseBody
    public boolean solrAdd(@RequestBody Goods goods) {
        System.out.println("实体类为11" + goods);
        SolrInputDocument solrInputFields = new SolrInputDocument();
        solrInputFields.addField("id", goods.getId());
        solrInputFields.addField("gtitle", goods.getTitle());
        solrInputFields.addField("gimage", goods.getGimage());
        solrInputFields.addField("ginfo", goods.getGinfo());
        solrInputFields.addField("gprice", goods.getPrice());
        try {
            solrClient.add(solrInputFields);
            solrClient.commit();
            return true;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 搜索商品
     * @param keyword
     * @param model
     * @return
     */
    @RequestMapping("/query")
    public String solrQuery(String keyword, Model model) {
        SolrQuery solrQuery = new SolrQuery();

        if (keyword == null && keyword.trim().equals("")) {
            solrQuery.setQuery("*:*");
        } else {
            solrQuery.setQuery("goods_info:" + keyword);
        }
        //添加高亮
        //开启高亮
        solrQuery.setHighlight(true);
        //设置高亮的前后
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //那些字段设置高亮
        solrQuery.addHighlightField("gtitle");

        QueryResponse query =null;
        List<Goods>list=new ArrayList<>();
        try {
            query= solrClient.query(solrQuery);
            //获得高亮
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            SolrDocumentList results = query.getResults();
            for (SolrDocument solrDocuments :results){
                Goods goods=new Goods();
              goods.setId( Integer.parseInt(solrDocuments.getFieldValue("id")+""));
              goods.setTitle(solrDocuments.getFieldValue("gtitle")+"");
              goods.setGimage(solrDocuments.getFieldValue("gimage")+"");
              goods.setPrice( Float.parseFloat(solrDocuments.getFieldValue("gprice")+""));
              //设置高亮
                if(highlighting.containsKey(goods.getId()+"")) {
                    String gtitle1 = highlighting.get(goods.getId() + "").get("gtitle").get(0);
                    if (gtitle1 != null) {
                        goods.setTitle(gtitle1);
                    }
                }
                list.add(goods);
                System.out.println("goods标题"+goods.getTitle());
            }

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("goods",list);
        return "searchlist";
    }


}
