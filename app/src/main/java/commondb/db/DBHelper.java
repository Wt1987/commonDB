/*
 * Copyright (C) 2017 贵阳货车帮科技有限公司
 */

package commondb.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;

public class DBHelper extends SQLiteOpenHelper {

    @Override
    public void onCreate(SQLiteDatabase db) {
        ormDataBase(DBConstant.sBeanClassArray, db);
    }

    public DBHelper(Context context) {
        super(context, DBConstant.DB_NAME, null, DBConstant.DB_VER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     * @Title: ormDataBase
     * @Description: 根据类，创建数据库表
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean ormDataBase(Class[] classes, SQLiteDatabase db) {
        boolean isSuc = false;
        try {
            if (classes != null && classes.length > 0) {
                for (int i = 0; i < classes.length; i++) {
                    classToDataBaseTable(classes[i], db);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuc;
    }

    /**
     * 检测表是否存在
     *
     * @param name 表名
     * @param db   数据库对象
     * @return 表存在返回true 表不存在返回false
     */
    public boolean isTableExist(String name, SQLiteDatabase db) {
        boolean exist = false;

        try {
            Cursor cursor;
            cursor = db.rawQuery("select count(name) from sqlite_master where type='table' and name= " + "?",
                    new String[]{name});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int tablecount = cursor.getInt(0);
                if (tablecount > 0) {
                    exist = true;
                }
            }
            if (cursor != null && (!cursor.isClosed())) {
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exist;
    }

    /**
     * @Title: classToDataBaseTable
     * @Description: 根据类实例创建数据库表
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public void classToDataBaseTable(Class<?> mClass, SQLiteDatabase db) {
        try {
            // 判断表是否在数据库中存在 不存在则创建
            if (!isTableExist(mClass.getSimpleName(), db)) {
                // 不存在创建表
                db.execSQL("CREATE TABLE IF NOT EXISTS [" + mClass.getSimpleName() + "]([uuid] CHAR NOT NULL)");
            }
            // 查询数据库中该表的字段，如果某个字段不存在，则添加这个字段
            Cursor cursor = db.rawQuery("select * from " + mClass.getSimpleName() + " where 1 = ?", new String[]{"1"});
            Field[] FieldArrary = mClass.getDeclaredFields();
            for (int j = 0; j < FieldArrary.length; j++) {
                if (cursor != null && (cursor.getColumnIndex(FieldArrary[j].getName()) == -1)) {
                    // 返回下标为-1表示数据库当中没有该字段
                    String columnType = reflectionObjectTypeToDataType(FieldArrary[j].getType().toString());
                    try {
                        db.execSQL("alter table " + mClass.getSimpleName() + " ADD COLUMN "
                                + FieldArrary[j].getName() + " " + columnType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // 关闭游标
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Title: reflectionObjectTypeToDataType
     * @Description: 根据属性的类型判断，设置对应sqlite数据库表的字段的类型
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public static String reflectionObjectTypeToDataType(String subClassType) {
        String columnType = "";
        if ("class java.lang.String".equals(subClassType)) {
            columnType = " CHAR";
        } else if ("long".equals(subClassType) || "Long".equals(subClassType)) {
            columnType = " NUMBER";
        } else if ("int".equals(subClassType) || "Int".equals(subClassType)) {
            columnType = " NUMBER";
        } else if (("double".equals(subClassType) || "Double".equals(subClassType))) {
            columnType = " NUMBER";
        } // 需要其他类型在此加入
        return columnType;
    }
}
