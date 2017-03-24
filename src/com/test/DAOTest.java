package com.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONObject;
import org.junit.Test;

import com.dao.DAO;
import com.db.JdbcUtils;

public class DAOTest {

	@Test
	public void testGetDictionaryList() throws SQLException {

		DAO dao = new DAO();

		JSONObject test = dao.getDictionaryList();
	}

}
