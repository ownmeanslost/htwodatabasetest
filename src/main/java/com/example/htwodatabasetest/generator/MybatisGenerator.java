package com.example.htwodatabasetest.generator;

import com.easy.mybatis.common.mbg.plugin.EMGenerator;

import java.io.InputStream;

/**
 * mybatis 操作组件生成器
 * 支持单表CRUD、分页等操作
 * @author peng.fu
 *
 * @date 2017年11月24日
 */
public class MybatisGenerator {

	public static void main(String[] args) throws Exception {

		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("generator/mybatis-generator.xml");
		EMGenerator.run(is);
	}

}
