package com.kwang.www.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class SendMail {

	public SendMail() {

	}

	/**
	 * Message对象将存储我们实际发送的电子邮件信息，
	 * Message对象被作为一个MimeMessage对象来创建并且需要知道应当选择哪一个JavaMail session。
	 */
	private MimeMessage message;

	/**
	 * Session类代表JavaMail中的一个邮件会话。
	 * 每一个基于JavaMail的应用程序至少有一个Session（可以有任意多的Session）。
	 * 
	 * JavaMail需要Properties来创建一个session对象。 寻找"mail.smtp.host" 属性值就是发送邮件的主机
	 * 寻找"mail.smtp.auth" 身份验证，目前免费邮件服务器都需要这一项
	 */
	private Session session;

	/***
	 * 邮件是既可以被发送也可以被受到。JavaMail使用了两个不同的类来完成这两个功能：Transport 和 Store。 Transport
	 * 是用来发送信息的，而Store用来收信。对于这的教程我们只需要用到Transport对象。
	 */
	private Transport transport;

	private String mailHost = "";
	private String sender_username = "";
	private String receiveUser = "";
	private String sender_password = "";

	private Properties properties = new Properties();

	/*
	 * 初始化方法
	 */
	public SendMail(boolean debug) {
		// InputStream in =
		// JavaMailWithoutAttachment.class.getResourceAsStream("MailServer.properties");
		InputStream inputStream = SendMail.class.getClassLoader()
				.getResourceAsStream("email.properties");
		BufferedReader bf = new BufferedReader(new InputStreamReader(
				inputStream));
		try {
			properties.load(bf);
			this.mailHost = properties.getProperty("mail.smtp.host");
			this.sender_username = properties
					.getProperty("mail.sender.username");
			this.receiveUser = properties
					.getProperty("receiveUser");
			this.sender_password = properties
					.getProperty("mail.sender.password");
		} catch (IOException e) {
			e.printStackTrace();
		}

		session = Session.getInstance(properties);
		session.setDebug(debug);// 开启后有调试信息
		message = new MimeMessage(session);
	}

	/**
	 * 发送邮件
	 * 
	 * @param subject
	 *            邮件主题
	 * @param sendHtml
	 *            邮件内容
	 * @param receiveUser
	 *            收件人地址
	 */
	public boolean doSendHtmlEmail(String subject, String sendHtml,
			String receiveUser) {
		try {
			// 发件人
			// InternetAddress from = new InternetAddress(sender_username);
			// 下面这个是设置发送人的Nick name
			InternetAddress from = new InternetAddress(
					MimeUtility.encodeWord("kwang") + " <" + sender_username
							+ ">");
			message.setFrom(from);

			// 收件人
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);// 还可以有CC、BCC

			// 邮件主题
			message.setSubject(subject);

			String content = sendHtml.toString();
			// 邮件内容,也可以使纯文本"text/plain"
			message.setContent(content, "text/html;charset=UTF-8");

			// 保存邮件
			message.saveChanges();

			transport = session.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(mailHost, sender_username, sender_password);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		SendMail se = new SendMail(false);
		se.doSendHtmlEmail("留言", "网页留言", "jixf.js@eakay.cn");
	}

	public boolean doSendHtmlEmail(String subject, String sendHtml) {
		return doSendHtmlEmail(subject, sendHtml, receiveUser);
	}

}