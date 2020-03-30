package com.example.htwodatabasetest.unit.controller;

import com.easy.mybatis.common.criteria.Criteria;
import com.easy.mybatis.common.criteria.QueryCondition;
import com.example.htwodatabasetest.unit.mapper.UnitMapper;
import com.example.htwodatabasetest.unit.model.UnitPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Scanner;

/**
 * @ClassName: UnitController
 * @Author: shaofan.li
 * @Description:
 * @Date: 2019/8/29 17:14
 */
@Controller
@RequestMapping("/UnitController")
public class UnitController {
    @Autowired
    private UnitMapper unitMapper;

    @RequestMapping("/test2")
    public void test2(){
        try {
            // 组装查询条件
            QueryCondition condition = new QueryCondition();
            condition.start(Criteria.newCriteria()
                    .eq(UnitPO.PKID, 1));
            // 执行查询
            List<UnitPO> poList = unitMapper.query(condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入第一个字符串:");
        String str1 = scanner.next();
        System.out.println("请输入第二个字符串:");
        String str2 = scanner.next();

        if(str1.length() != str2.length()){
            System.out.println("-1");
        }
        //字符串比较
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();

        int legth = str1.length();
        String temp ="";
           for (int j=0;j<legth; j++){
               if(chars1[0] == chars2[j]){
                   //获取字符串前半截
                   String pre = str2.substring(0, j);
                   //获取字符串前后半截
                   String after = str2.substring(j, legth);
                   temp = after + pre;
                   if(temp.equals(str1)){//相等
                       System.out.println("1");
                       break;
                   }else {
                       temp ="";
                   }
               }
           }
    }


}
