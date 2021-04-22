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
            //在全局中查询上一次标识的时间，如果上一次标识和本次标识的时间相同，则跳过更新。
            int id = getIndicatorId(tag.tagName);
            System.out.println(Global.tagLastTime.get(tag.tagName));
            boolean newTag = Global.tagLastTime.get(tag.tagName) == null || Global.tagLastTime.get(tag.tagName) != tag.timestamp;
            System.out.println("newTag状态："+newTag);
            
            if (id != 0 && newTag){

                String insertEvaSQL = "insert into T_SE_RealValue(F_IndicatorId,F_Time,F_Value) values(?,?,?)";
                String insertLogSQL = "insert into T_SE_HistoryValue(F_IndicatorId,F_Time,F_Value) values(?,?,?)";
                PreparedStatement stmtEva = conn.prepareStatement(insertEvaSQL);
                PreparedStatement stmtLog = conn.prepareStatement(insertLogSQL);
                System.out.println("取到标识ID："+id);

                //删除原记录
                delRealRecord(id);

                //开始执行数据库操作
                stmtEva.setInt(1, id);
                stmtEva.setString(2, tag.timestamp);
                stmtEva.setString(3, tag.value);
                
                stmtLog.setInt(1, id);
                stmtLog.setString(2, tag.timestamp);
                stmtLog.setString(3, tag.value);
    
                stmtEva.executeUpdate();
                stmtLog.executeUpdate();
                
                //更新表单中的时间戳
                Global.tagLastTime.put(tag.tagName, tag.timestamp);
                //向Map中添加记录
                System.out.println(Global.tagLastTime.values());
                System.out.println("成功添加Tag");
            }
        } catch (Exception e) {
            System.err.println("添加记录时出现错误："+e);
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

    public void delRealRecord(int id) throws Exception{
        String delSql = "delete from T_SE_RealValue where F_IndicatorId = ?";
        PreparedStatement stmt = conn.prepareStatement(delSql);
        
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
