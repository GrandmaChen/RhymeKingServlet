package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.db.JdbcUtils;
import com.domain.Phrase;

public class DAO {

	private QueryRunner queryRunner = new QueryRunner();
	private QueryRunner pinyinRunner = new QueryRunner();

	private JSONObject dictionaryLists;

	public JSONObject getDictionaryList() throws SQLException {

		dictionaryLists = new JSONObject();
		Connection connection = null;

		try {
			connection = JdbcUtils.getConnection();

			queryRunner.query(connection, "SELECT * FROM CATALOGUE", new ResultSetHandler<JSONObject>() {

				@Override
				public JSONObject handle(ResultSet resultSet) throws SQLException {

					List<String> hexCode = new ArrayList<String>();
					List<String> name = new ArrayList<String>();
					List<Integer> quantity = new ArrayList<Integer>();
					List<Integer> maxLength = new ArrayList<Integer>();

					while (resultSet.next()) {

						hexCode.add(resultSet.getString(1));
						name.add(resultSet.getString(2));
						quantity.add(resultSet.getInt(3));
						maxLength.add(resultSet.getInt(5));

					}

					try {
						dictionaryLists.put("hexCode", hexCode);
						dictionaryLists.put("name", name);
						dictionaryLists.put("quantity", quantity);
						dictionaryLists.put("maxLength", maxLength);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.releaseConnection(connection);
		}

		return dictionaryLists;
	}

	public JSONArray getDictionary(int maxLength, String hexCode) throws SQLException {

		Connection connection = JdbcUtils.getConnection();

		return queryRunner.query(connection, "SELECT * FROM " + hexCode, new ResultSetHandler<JSONArray>() {

			@Override
			public JSONArray handle(ResultSet resultSet) throws SQLException {

				JSONArray dictionaryContent = new JSONArray();

				while (resultSet.next()) {

					String characters = resultSet.getString(1);
					int length = resultSet.getInt(2);

					String sql = "SELECT * FROM " + hexCode;

					// Get pinyin of this phrase from the database
					List<List<String>> pinyin = pinyinRunner.query(connection, sql,
							new ResultSetHandler<List<List<String>>>() {

								@Override
								public List<List<String>> handle(ResultSet arg0) throws SQLException {

									List<List<String>> pinyin = new ArrayList<List<String>>();

									List<String> shengmus = new ArrayList<String>();
									List<String> yunmus = new ArrayList<String>();
									List<String> tones = new ArrayList<String>();

									for (int i = 0; i < length; i++) {
										shengmus.add(resultSet.getString(i * 3 + 3));
										yunmus.add(resultSet.getString(i * 3 + 4));
										tones.add(resultSet.getString(i * 3 + 5));
									}

									pinyin.add(shengmus);
									pinyin.add(yunmus);
									pinyin.add(tones);

									return pinyin;
								}
							});

					Phrase phrase = new Phrase(hexCode, characters, pinyin.get(0), pinyin.get(1), pinyin.get(2));

					dictionaryContent.put(phrase);
				}

				JdbcUtils.releaseConnection(connection);
				return dictionaryContent;
			}
		});

	}
}