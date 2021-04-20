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
        System.out.println("正在连接数据库："+this.DB_URL);
        conn = DriverManager.getConnection(this.DB_URL, this.User, this.Password);
    }

    public void addRecord(Tag tag) throws SQLException{
        try {
            String insertEvaSQL = "insert into T_SE_EvaluationDetail(F_EvaluationId,F_IndicatorId,F_Value) values(?,?,?)";
            String insertLogSQL = "insert into T_SE_HistoryValue(F_IndicatorId,F_Time,F_Value) values(?,?,?)";
            PreparedStatement stmtEva = conn.prepareStatement(insertEvaSQL);
            PreparedStatement stmtLog = conn.prepareStatement(insertLogSQL);

            stmtEva.setString(1, tag.deviceId);
            stmtEva.setString(2, tag.tagName);
            stmtEva.setString(3, tag.value);
            
            stmtLog.setString(1, tag.tagName);
            stmtLog.setString(2, tag.timestamp);
            stmtLog.setString(3, tag.value);

            stmtEva.executeUpdate();
            stmtEva.executeUpdate();

            System.out.println("成功添加Tag");
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        }
    }

    public void addRecord(List<Tag> tags) throws Exception{
        for(int i = 0;i<tags.size();i++){
            addRecord(tags.get(i));
        }
    }
}
