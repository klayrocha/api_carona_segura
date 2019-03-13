package br.com.caronasegura.util;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSender mailSender(){
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("mail.smtp.host"));
		mailSender.setPort(env.getProperty("mail.smtp.port", Integer.class));
		mailSender.setUsername(env.getProperty("mail.smtp.username"));
		mailSender.setPassword(env.getProperty("mail.smtp.password"));
		
		Properties props = new Properties();
		props.put("mail.transport.protocol","smtp");
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.connectiontimeout",100000);
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory"); //SSL
		
		Session session = Session.getDefaultInstance(props,
		        new javax.mail.Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                        return new PasswordAuthentication(env.getProperty("mail.smtp.username"),env.getProperty("mail.smtp.password"));
		                }
		        });
		
		mailSender.setSession(session);
		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}

}
