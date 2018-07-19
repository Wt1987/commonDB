/*
 * Copyright (C) 2017 贵阳货车帮科技有限公司
 */

package commondb.db;



import commondb.db.table.UserBean;

public class DBConstant {

    // 数据库版本
    static final int DB_VER = 4;
    // 数据库名称
    static final String DB_NAME = "Verify_Code_Data";
    /**
     * 数据库表，对应的bean类，自己动态的添加
     **/
    static Class[] sBeanClassArray = new Class[]{UserBean.class};

}
