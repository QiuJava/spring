package com.example.service.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.entity.Employee;

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

	public void sendResetPasswordSuccessMail(String employeeNumber, String email, String nickname) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setFrom(username, nickname);
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject("密码重置成功");
			mimeMessageHelper.setText("您的新密码：" + employeeNumber + Employee.INIT_PASSWORD_SUFFIX);
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("邮件构建异常", e);
			return;
		}
		javaMailSender.send(mimeMessage);
	}
}
