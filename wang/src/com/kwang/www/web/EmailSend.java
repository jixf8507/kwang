package com.kwang.www.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kwang.www.tools.SendMail;

public class EmailSend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EmailSend() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String userEmail = request.getParameter("userEmail");
		String userPhone = request.getParameter("userPhone");
		String userMsg = request.getParameter("userMsg");

		StringBuffer sendHtml = new StringBuffer("Name:");
		sendHtml.append(userName);
		sendHtml.append("<br/>Mobile:");
		sendHtml.append(userPhone);
		sendHtml.append("<br/>E-Mali:");
		sendHtml.append(userEmail);
		sendHtml.append("<br/>Subject:");
		sendHtml.append(userMsg);
		SendMail se = new SendMail(false);
		boolean bool = se.doSendHtmlEmail("kwang网页留言", sendHtml.toString());
		PrintWriter out = response.getWriter();
		if (bool) {
			out.println("success");
		} else {
			out.println("false");
		}

	}
}
