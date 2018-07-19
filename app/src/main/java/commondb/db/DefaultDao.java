/*
 * Copyright (C) 2017 贵阳货车帮科技有限公司
 */

package commondb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.lang.reflect.Field;

import commondb.db.table.BaseData;

public final class DefaultDao {

    // 锁对象
    // private static final Object lock = new Object();
    // 上下文，句柄
    static Context sContext;
    // DBHelper对象
    static DBHelper sHelper;
    // DefaultDao操作句柄
    protected static DefaultDao sInstance;
    private static final String UUID = "uuid";

    private DefaultDao(Context context) {
        sHelper = new DBHelper(context.getApplicationContext());
    }

    public static DefaultDao getInstance(Context context) {
        if (sInstance == null || sContext == null
                || context.getApplicationContext() != sContext.getApplicationContext()) {
            sContext = context.getApplicationContext();
            sInstance = new DefaultDao(context);
        }
        return sInstance;
    }

    public static DBHelper getHelper() {
        return sHelper;
    }


    private DefaultDao() {

    }

    /**
     * @Title: dataModify
     * @Description: 根据bean对象，更新数据库中对应列的数据
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean dataModify(BaseData bean) {
        boolean modifsuc = false;
        SQLiteDatabase db = null;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField(UUID);
            uuidField.setAccessible(true);
            String uuid = (String) uuidField.get(bean);

            db = sHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            Field[] subclass = bean.getClass().getDeclaredFields();
            for (int i = 0; i < subclass.length; i++) {
                Field valField = subclass[i];
                valField.setAccessible(true);
                String valFieldType = valField.getType().toString();
                if ("class java.lang.String".equals(valFieldType)) {
                    values.put(valField.getName(), (String) valField.get(bean));
                } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                    values.put(valField.getName(), (Long) valField.get(bean));
                } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                    values.put(valField.getName(), (Integer) valField.get(bean));
                } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                    values.put(valField.getName(), (Double) valField.get(bean));
                } // 需要其他类型在此加入
            }
            if (values != null) {

                // uuid是由java工具类产生的，永远都不会重复
                int result = db.update(bean.getClass().getSimpleName(), values, "uuid='" + uuid + "'", null);
                if (result > 0) {
                    modifsuc = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
        return modifsuc;
    }

    /**
     * @Title: dataDelete
     * @Description: 根据bean删除数据库元素
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean dataDelete(BaseData bean) {
        boolean deletesuc = false;
        SQLiteDatabase db = null;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField(UUID);
            uuidField.setAccessible(true);
            String uuid = (String) uuidField.get(bean);
            db = sHelper.getWritableDatabase();

            int result = db.delete(bean.getClass().getSimpleName(), "uuid='" + uuid + "'", null);
            if (result > 0) {
                deletesuc = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null && db.isOpen()) {
            db.close();
        }
        return deletesuc;
    }

    /**
     * @Title: isExistInDataBase
     * @Description: 某个对象在表中是否存在
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     */
    public boolean isExistInDataBase(BaseData bean) {

        boolean existData = false;
        SQLiteDatabase db = null;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField(UUID);
            uuidField.setAccessible(true);
            String uuid = (String) uuidField.get(bean);
            db = sHelper.getWritableDatabase();
            Cursor cursor = db.query(bean.getClass().getSimpleName(), null, "uuid='" + uuid + "'", null, null, null,
                    null);
            if (cursor != null && cursor.getCount() > 0) {
                existData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null && db.isOpen()) {
            db.close();
        }
        return existData;

    }

    /**
     * @Title: dataSave
     * @Description: 保存到数据库中
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean dataSave(BaseData bean) {
        boolean savesuc = false;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField(UUID);
            uuidField.setAccessible(true);
            String uuid = java.util.UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(uuidField.getName(), uuid);
            // 获取子类属性及值
            Field[] subclass = bean.getClass().getDeclaredFields();
            for (int i = 0; i < subclass.length; i++) {
                Field valField = subclass[i];
                valField.setAccessible(true);
                String valFieldType = valField.getType().toString();
                if ("class java.lang.String".equals(valFieldType)) {
                    values.put(valField.getName(), (String) valField.get(bean));
                } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                    values.put(valField.getName(), (Long) valField.get(bean));
                } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                    values.put(valField.getName(), (Integer) valField.get(bean));
                } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                    values.put(valField.getName(), (Double) valField.get(bean));
                }
                // 需要其他类型在此加入
            }
            if (values != null) {
                SQLiteDatabase db = null;
                try {
                    db = sHelper.getWritableDatabase();
                    db.insert(bean.getClass().getSimpleName(), null, values);
                    savesuc = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savesuc;
    }

    /**
     * @Title: close
     * @Description: 关闭所有打开的数据库连接
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public void close() {
        if (sHelper != null) {
            sHelper.close();
            sHelper = null;
        }
    }

}
