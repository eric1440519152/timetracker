package com.hhu.imis;

import java.util.Vector;

public class ConnPool {
    private String jdbcDriver = "com.mysql.jdbc.Driver"; // 数据库驱动
	private String dbUrl = ""; // 数据 URL
	private String dbUsername = ""; // 数据库用户名
	private String dbPassword = ""; // 数据库用户密码
	
	private int initialConnections = 10; // 连接池的初始大小
	private int incrementalConnections = 5;// 连接池自动增加的大小
	private int maxConnections = 50; // 连接池最大的大小
	private Vector connections = null; // 存放连接池中数据库连接的向量 , 初始时为 null
	// 它中存放的对象为 PooledConnection 型
 
	/**
	 * 构造函数
	 * 
	 * @param jdbcDriver
	 *            String JDBC 驱动类串
	 * @param dbUrl
	 *            String 数据库 URL
	 * @param dbUsername
	 *            String 连接数据库用户名
	 * @param dbPassword
	 *            String 连接数据库用户的密码
	 * 
	 */
	public ConnPool(String jdbcDriver, String dbUrl, String dbUsername,String dbPassword) {
		this.jdbcDriver = jdbcDriver;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

}
