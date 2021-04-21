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

    public void addRecord(Tag tag) throws Exception{
        try {
            int id = getIndicatorId(tag.tagName);
            if (id != 0){
                String insertEvaSQL = "insert into T_SE_RealValue(F_IndicatorId,F_Time,F_Value) values(?,?,?) on DUPLICATE KEY UPDATE F_Time = ? , F_Value = ?";
                String insertLogSQL = "insert into T_SE_HistoryValue(F_IndicatorId,F_Time,F_Value) values(?,?,?)";
                PreparedStatement stmtEva = conn.prepareStatement(insertEvaSQL);
                PreparedStatement stmtLog = conn.prepareStatement(insertLogSQL);
                System.out.println("取到标识ID："+id);

                stmtEva.setInt(1, id);
                stmtEva.setString(2, tag.timestamp);
                stmtEva.setString(3, tag.value);
                stmtEva.setString(4, tag.timestamp);
                stmtEva.setString(5, tag.value);
                
                stmtLog.setInt(1, id);
                stmtLog.setString(2, tag.timestamp);
                stmtLog.setString(3, tag.value);
    
                stmtEva.executeUpdate();
                stmtLog.executeUpdate();
    
                System.out.println("成功添加Tag");
            }
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

    public ResultSet getEvaStatus() throws Exception{
        String queryEvaStatusSQL = "select F_EquipmentId,F_Status from T_SE_Status";
        PreparedStatement stmt = conn.prepareStatement(queryEvaStatusSQL);
        ResultSet set = null;
        
        set = stmt.executeQuery();

        return set;
    }

    public int getIndicatorId(String tagName) throws Exception{
        String querySQL = "SELECT F_Id From T_SE_Indicator WHERE F_Name = ?";
        PreparedStatement stmt = conn.prepareStatement(querySQL);
        stmt.setString(1, tagName);

        ResultSet set = stmt.executeQuery();
        set.next();

        return set.getInt(1);
    }

    public String getEvaStatus(String tagName) throws Exception{
        String queryEvaStatusSQL = "select F_EquipmentId,F_Status from T_SE_Status where F_EquipmentId = ?";
        PreparedStatement stmt = conn.prepareStatement(queryEvaStatusSQL);
        ResultSet set = null;

        stmt.setString(1, tagName);
        set = stmt.executeQuery();
        set.next();

        return set.getString("F_Status");
    }
}
