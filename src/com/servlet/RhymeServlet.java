package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dao.DAO;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

/**
 * Servlet implementation class RhymeServlet
 */
public class RhymeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public RhymeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("got it!!!");
		System.out.println(request.getParameter("requestType"));
		String jsonString = IOUtils.toString(request.getInputStream());
		JSONObject json = null;
		String requestType = null;
		DAO dao = new DAO();

		try {
			json = new JSONObject(jsonString);
			requestType = (String) json.get("requestType");
		} catch (JSONException e) {
			System.out.println("No data input");
			return;
		}

		// result to return
		JSONObject result = new JSONObject();
		switch (requestType) {

		case "dictionaryList":
			try {
				result.put("dictionaryList", dao.getDictionaryList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "downloadDictionary":
			try {
				result.put("dictionaryContent",
						dao.getDictionary((int) json.get("maxLength"), (String) json.get("hexCode")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "autoParse":

			JSONArray parsedLyrics = new JSONArray();
			JiebaSegmenter segmenter = new JiebaSegmenter();

			// Parse lyrics
			try {
				for (SegToken segToken : segmenter.process((String) json.getString("lyrics"), SegMode.SEARCH)) {
					if (segToken.word != null && segToken.word.length() > 1) {
						parsedLyrics.put(segToken.word);
						System.out.println(segToken.word);
					}
				}

				result.put("parsedLyrics", parsedLyrics);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		}

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(result.toString());
	}

}
