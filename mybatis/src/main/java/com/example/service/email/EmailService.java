package com.example.service.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 邮件服务
 * 
 * @author Qiu Jian
 *
 */
@Service
@Slf4j
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String username;

	public void sendResetPasswordSuccessMail(String email, String username) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setFrom(email, username);
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject("密码重置成功");
			mimeMessageHelper.setText("密码重置成功");
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("邮件构建异常", e);
			return;
		}
		javaMailSender.send(mimeMessage);
	}
}
