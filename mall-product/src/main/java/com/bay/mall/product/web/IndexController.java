package com.bay.mall.product.web;

import com.bay.mall.product.entity.CategoryEntity;
import com.bay.mall.product.service.CategoryService;
import com.bay.mall.product.vo.Catelog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description 处理首页请求
 * @Author baymux
 * @Date 2020/6/22 1:10
 * @Vsrsion 1.0
 **/
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {

        // TODO 1.查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        // 视图解析器 拼串
        // classpath:/templates/ + 返回值+.html
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }


    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {

        Map<String, List<Catelog2Vo>> catalogJsonMap = categoryService.getCatalogJson();
        return catalogJsonMap;
    }
}
