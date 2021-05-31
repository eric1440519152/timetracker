package com.hhu.imis;
import java.sql.*;
import java.util.List;
import java.util.Map;

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
            int id = getIndicatorId(tag.tagName,tag.catagoryId);
            Map<String,String> tagLastTime = Global.tagLastTimes.get(tag.deviceId);
            boolean newTag = tagLastTime.get(tag.tagName) == null || !tagLastTime.get(tag.tagName).equals(tag.timestamp);
            //System.out.println("newTag状态："+newTag);
            
            if (id != 0 && newTag){

                String insertEvaSQL = "insert into T_SE_RealValue(F_IndicatorId,F_EquipmentId,F_Time,F_Value) values(?,?,?,?)";
                String insertLogSQL = "insert into T_SE_HistoryValue(F_IndicatorId,F_EquipmentId,F_Time,F_Value) values(?,?,?,?)";
                PreparedStatement stmtEva = conn.prepareStatement(insertEvaSQL);
                PreparedStatement stmtLog = conn.prepareStatement(insertLogSQL);
                //System.out.println("取到标识ID："+id);

                //删除原记录
                delRealRecord(id);

                //开始执行数据库操作
                stmtEva.setInt(1, id);
                stmtEva.setString(3, tag.timestamp);
                stmtEva.setString(4, tag.value);
                stmtEva.setString(2, tag.deviceId);
                
                stmtLog.setInt(1, id);
                stmtLog.setString(3, tag.timestamp);
                stmtLog.setString(4, tag.value);
                stmtLog.setString(2, tag.deviceId);
    
                stmtEva.executeUpdate();
                stmtLog.executeUpdate();

                //更新表单中的时间戳
                tagLastTime.put(tag.tagName, tag.timestamp);
                //向Map中添加记录
                //System.out.println(Global.tagLastTime.values());

                //System.out.println(tag.deviceNo +"成功添加Tag:" + tag.tagName);
            }
        } catch (Exception e) {
            System.err.println("添加记录时出现错误："+e);
            throw e;
        }
    }

    public void addRecord(List<Tag> tags) throws Exception{
        String insertEvaSQL = "insert into T_SE_RealValue(F_IndicatorId,F_EquipmentId,F_Time,F_Value) values(?,?,?,?)";
        String insertLogSQL = "insert into T_SE_HistoryValue(F_IndicatorId,F_EquipmentId,F_Time,F_Value) values(?,?,?,?)";
        String delSql = "delete from T_SE_RealValue where F_IndicatorId = ?";
        PreparedStatement stmtDel = conn.prepareStatement(delSql);
        PreparedStatement stmtEva = conn.prepareStatement(insertEvaSQL);
        PreparedStatement stmtLog = conn.prepareStatement(insertLogSQL);
        Map<String,String> tagLastTime = Global.tagLastTimes.get(tags.get(0).deviceId);

        for(Tag tag : tags){
            int id = getIndicatorId(tag.tagName,tag.catagoryId);
            boolean newTag = tagLastTime.get(tag.tagName) == null || !tagLastTime.get(tag.tagName).equals(tag.timestamp);
            if (id != 0 && newTag){
                //System.out.println(tag.deviceNo+"添加新Tag任务");
                stmtDel.setInt(1, id);
                stmtDel.addBatch();

                //开始执行数据库操作
                stmtEva.setInt(1, id);
                stmtEva.setString(3, tag.timestamp);
                stmtEva.setString(4, tag.value);
                stmtEva.setString(2, tag.deviceId);

                stmtEva.addBatch();

                stmtLog.setInt(1, id);
                stmtLog.setString(3, tag.timestamp);
                stmtLog.setString(4, tag.value);
                stmtLog.setString(2, tag.deviceId);

                stmtLog.addBatch();

                //更新表单中的时间戳
                tagLastTime.put(tag.tagName, tag.timestamp);
            }
                       
        }
        
        stmtDel.executeBatch();
        stmtEva.executeBatch();
        stmtLog.executeBatch();
    }

    public ResultSet getEvaStatus() throws Exception{
        String queryEvaStatusSQL = "select F_EquipmentId,F_Status from T_SE_Status";
        PreparedStatement stmt = conn.prepareStatement(queryEvaStatusSQL);
        ResultSet set = null;
        
        set = stmt.executeQuery();

        return set;
    }

    public ResultSet getEquipment() throws Exception{
        String getEquipmentSQL = "select F_Id,F_CatagryId,F_No from T_SE_Equipment";
        PreparedStatement stmt = conn.prepareStatement(getEquipmentSQL);
        ResultSet set = null;
        
        set = stmt.executeQuery();

        return set;
    }

    public int getIndicatorId(String tagName,String catagoryId){
        try {
            String querySQL = "SELECT F_Id From T_SE_Indicator WHERE F_Name = ? AND F_CatagoryId = ?";
            PreparedStatement stmt = conn.prepareStatement(querySQL);
            stmt.setString(1, tagName);
            stmt.setString(2, catagoryId);

            ResultSet set = stmt.executeQuery();
            set.next();

            return set.getInt(1);
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("查无此Tag ID:"+tagName);
            return 0;
        }
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

    public void delRealRecord(int id){
        try {
            String delSql = "delete from T_SE_RealValue where F_IndicatorId = ?";
            PreparedStatement stmt = conn.prepareStatement(delSql);
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("删除记录时出现错误"+e);
        }
    }
}
