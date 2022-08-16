package com.imooc;

import com.imooc.dao.MatchDao;
import com.imooc.entity.Match;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

//http本地连接的代码，如果仅在本地进行http连接测试，则开放下面代码

//@SpringBootApplication
//@EnableTransactionManagement
//@MapperScan("com.imooc.dao")
//public class JeffApplication  {
//	public static void main(String[] args) {
//		System.out.println("程序启动:----");
//		SpringApplication.run(com.imooc.JeffApplication.class, args);
//	}
//}
//
//https连接的代码，用于部署在正式环境下的，如果仅在本地进行http连接测试，请注释掉下面所有代码

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.imooc.dao")
public class JeffApplication implements EmbeddedServletContainerCustomizer {
	public static void main(String[] args) {
		System.out.println("程序启动:----");
		SpringApplication.run(JeffApplication.class, args);
	}


	//拦截所有请求
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint constraint = new SecurityConstraint();
				constraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				constraint.addCollection(collection);
				context.addConstraint(constraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

	//配置http转https
	@Bean
	public Connector httpConnector() {
		Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		//Connector监听的http的端口号
		connector.setPort(80);
		connector.setSecure(false);
		//监听到http的端口号后转向到的https的端口号
		connector.setRedirectPort(443);
		return connector;
	}

	//这里设置默认端口为443，即https的，如果这里不设置，会https和http争夺80端口
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(443);
	}
}
