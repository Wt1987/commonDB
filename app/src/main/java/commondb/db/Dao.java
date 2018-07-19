/*
 * Copyright (C) 2017 贵阳货车帮科技有限公司
 */

package commondb.db;

import android.content.ContentValues;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Dao<T> {

    /**
     * @param fieldName  对应的列
     * @param fieldValue 对应的字段
     * @Title: queryByUUID
     * @Description:通过某一列对应的值进行查询数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    ArrayList<T> queryByField(String fieldName, String fieldValue) throws Exception;

    /**
     * @Title: close
     * @Description: 关闭数据库连接
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    void close();

    /**
     * @Title: queryAllData
     * @Description:查询出T,对应的表的所有数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    List<T> queryAllData() throws Exception;

    /**
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    List<T> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                  String having, String orderBy) throws Exception;

    /**
     * @param fieldValues 查询条件
     * @Title: queryForFieldValues
     * @Description: 根据键值对，查询数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    List<T> queryForFieldValues(Map<String, Object> fieldValues) throws Exception;

    /**
     * @param fieldValues 更新条件
     * @Title: updateTalbeInfo
     * @Description: 更新数据库表
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     * 需要更新的数据
     */
    int updateTalbeInfo(ContentValues values, Map<String, Object> fieldValues) throws SQLException;

    /**
     * @param fieldValues 删除条件
     * @Title: delTalbelInfo
     * @Description: 删除数据库表信息, 根据特定的删除条件
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     */
    int delTalbelInfo(Map<String, Object> fieldValues) throws SQLException;

    /**
     * @Title: insertDataToDB
     * @Description: 批量插入数据, 在这里使用事务，可以提高操作效率
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     */
    int insertDataToDB(ArrayList<T> dataList) throws SQLException;

}
