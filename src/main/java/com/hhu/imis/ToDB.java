package com.hhu.imis;
import java.sql.*;
import java.util.List;

public class ToDB {
    private String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private String DB_URL;
    private String User;
    private String Password;
    private Connection conn;
    public ToDB(String DB_URL,String User,String Password) throws Exception{
        this.DB_URL = "jdbc:"+DB_URL;
        this.User = User;
        this.Password = Password;

        Class.forName(JDBC_DRIVER);
        System.out.println("正在连接数据库："+DB_URL);
        conn = DriverManager.getConnection(DB_URL, this.User, this.Password);
    }

    public void addRecord(Tag tag){
        
    }

    public void addRecord(List<Tag> tags){

    }
}
