package com.rxv5.system;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.mysql.cj.jdbc.Driver;
import com.rxv5.util.PathKit;

/**
 * mybatis-plus 自动生成代码
 * 1、配置好数据库后会读取数据库中的所有表，但会和include参数比较，然后报表不存在。这是一个BUG，运行时可以忽略
 * 2、还没有找到可以生成VO的类代码 ！！！
 * 3、mybatis-plus-generator不可以更新Model变更字段，需手动处理！！！！！！！！！！！！！
 * @author congrixu
 *
 */
public class SystemGenerator {

	public static void main(String[] args) {
		String[] tableNames = new String[] { "T_ROLE" };

		moduleGenerator(tableNames);
	}

	private static void moduleGenerator(String[] tableNames) {
		GlobalConfig globalConfig = getGlobalConfig();// 全局配置

		DataSourceConfig dataSourceConfig = getDataSourceConfig();// 数据源配置

		PackageConfig packageConfig = getPackageConfig();// 包配置

		StrategyConfig strategyConfig = getStrategyConfig(tableNames);// 策略配置

		new AutoGenerator().setGlobalConfig(globalConfig).setDataSource(dataSourceConfig).setPackageInfo(packageConfig)
				.setStrategy(strategyConfig).setTemplate(getTemplateConfig()).execute();

	}

	private static GlobalConfig getGlobalConfig() {
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setOpen(false);
		//new File(module).getAbsolutePath()得到模块根目录路径，因事Maven项目，代码指定路径自定义调整
		globalConfig.setOutputDir(PathKit.getRootClassPath() + "/../../src/main/java");//生成文件的输出目录
		globalConfig.setFileOverride(false);//是否覆盖已有文件
		globalConfig.setBaseResultMap(true);
		globalConfig.setBaseColumnList(true);
		globalConfig.setActiveRecord(false);
		globalConfig.setControllerName(null);
		globalConfig.setServiceName(null);
		globalConfig.setServiceImplName(null);
		globalConfig.setEntityName("%s");//不加会使生成的model不带@TableName
		globalConfig.setDateType(DateType.ONLY_DATE);//因为jdk1.8引入java.time.*新api,在日期的转换上转为java.util.Date
		return globalConfig;
	}

	//TODO
	private static DataSourceConfig getDataSourceConfig() {
		String dbUrl = "jdbc:mysql://127.0.0.1:3306/userms?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setDbType(DbType.MYSQL);
		dataSourceConfig.setDriverName(Driver.class.getName());
		dataSourceConfig.setUsername("root");
		dataSourceConfig.setPassword("123456");
		dataSourceConfig.setUrl(dbUrl);
		return dataSourceConfig;
	}

	private static PackageConfig getPackageConfig() {
		PackageConfig packageConfig = new PackageConfig();
		String packageName = "com.rxv5.system";//不同模块 代码生成具体路径自定义指定

		packageConfig.setParent(packageName);
		//packageConfig.setModuleName("System")//模块名称，单独生成模块时使用！！！！！！！！！！！
		packageConfig.setController(null);// 这里是控制器包名，默认 web
		packageConfig.setService(null);
		packageConfig.setServiceImpl(null);
		packageConfig.setMapper("dao");
		packageConfig.setEntity("model");
		packageConfig.setXml(null);
		return packageConfig;
	}

	private static StrategyConfig getStrategyConfig(String[] tableNames) {
		StrategyConfig strategyConfig = new StrategyConfig();
		strategyConfig.setCapitalMode(true);//驼峰命名
		strategyConfig.setEntityLombokModel(false);// 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
		strategyConfig.setRestControllerStyle(false);
		strategyConfig.setNaming(NamingStrategy.underline_to_camel);
		strategyConfig.setTablePrefix("T_");
		strategyConfig.setInclude(tableNames);// 需要生成的表
		strategyConfig.setEntityTableFieldAnnotationEnable(true);//是否生成实体时，生成字段注解
		strategyConfig.setEntitySerialVersionUID(false);
		strategyConfig.setCapitalMode(true);//是否大写命名
		// .setExclude(new String[]{"gente2"}) // 排除生成的表
		// 自定义实体父类
		// .setSuperEntityClass("com.baomidou.demo.TestEntity")
		// 自定义实体，公共字段
		// .setSuperEntityColumns(new String[]{"test_id"})
		// .setTableFillList(tableFillList)
		// 自定义 mapper 父类 默认BaseMapper
		// .setSuperMapperClass("com.baomidou.mybatisplus.mapper.BaseMapper")
		// 自定义 service 父类 默认IService
		// .setSuperServiceClass("com.baomidou.demo.TestService")
		// 自定义 service 实现类父类 默认ServiceImpl
		// .setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl")
		// 自定义 controller 父类
		// .setSuperControllerClass("com.caocc.portal.controller.BaseController")
		// 【实体】是否生成字段常量（默认 false）
		// public static final String ID = "test_id";
		// .setEntityColumnConstant(true)
		// 【实体】是否为构建者模型（默认 false）
		// public User setName(String name) {this.name = name; return this;}
		// .setEntityBuilderModel(true)
		// Boolean类型字段是否移除is前缀处理
		// .setEntityBooleanColumnRemoveIsPrefix(true)
		// .setRestControllerStyle(true)
		// .setControllerMappingHyphenStyle(true)

		return strategyConfig;
	}

	private static TemplateConfig getTemplateConfig() {
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setEntity(new TemplateConfig().getEntity(false));
		templateConfig.setMapper(new TemplateConfig().getMapper());//mapper模板采用mybatis-plus自己模板
		templateConfig.setXml(null);
		templateConfig.setController(null);//不生成controller代码
		templateConfig.setService(null);
		templateConfig.setServiceImpl(null);
		return templateConfig;
	}

}
