/*
 * Copyright (C) 2017 贵阳货车帮科技有限公司
 */

package commondb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DaoImpl<T> implements Dao<T> {

    private final Class<T> mDataClass;
    // 上下文，句柄
    static Context sContext;
    // DBHelper对象
    static DBHelper sDBHelper;

    private static final String DO_HAVE_CLASS = "do not have calss ";
    private static final String EQUAL_STRING = " = ?";

    public DaoImpl(Class<T> dataClass, Context context) {

        this.mDataClass = dataClass;
        sDBHelper = new DBHelper(context.getApplicationContext());
        sContext = context.getApplicationContext();
    }

    @Override
    public ArrayList<T> queryByField(String fieldName, String valued) throws Exception {

        if (mDataClass == null) {
            throw new SQLException(DO_HAVE_CLASS);
        }
        ArrayList<T> resultList = null;
        // 查询数据库
        String[] tableInfo = getTableInfo();
        if (tableInfo != null && tableInfo.length > 0) {
            SQLiteDatabase db = null;
            try {
                db = sDBHelper.getWritableDatabase();
                // 查询数据库
                Cursor cursor = db.query(mDataClass.getSimpleName(), tableInfo, fieldName + EQUAL_STRING,
                        new String[]{valued}, null, null, null);

                resultList = getDataFromCursor(cursor);

            } catch (ClassNotFoundException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw e;
            } catch (InstantiationException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
        }

        return resultList;
    }

    /**
     * @Title: getTableInfo
     * @Description: 获取表格中的字段
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    private String[] getTableInfo() {

        Field[] subclass = mDataClass.getDeclaredFields();
        Class<? super T> superclass = mDataClass.getSuperclass();
        // 父类的数据
        Field[] superSubClass = superclass.getDeclaredFields();
        String[] tableInfo = new String[subclass.length + superSubClass.length];
        for (int i = 0; i < subclass.length; i++) {

            tableInfo[i] = subclass[i].getName();
        }

        for (int j = subclass.length; j < superSubClass.length + subclass.length; j++) {
            tableInfo[j] = superSubClass[j - subclass.length].getName();
        }
        return tableInfo;
    }

    /**
     * @Title: getDataFromCursor
     * @Description: 从游标中获取需要的字段
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    private ArrayList<T> getDataFromCursor(Cursor cursor) throws Exception {

        if (cursor == null) {
            return null;
        }
        ArrayList<T> resultList = new ArrayList<T>();

        // 开始组装值的时候
        while (cursor.moveToNext()) {

            Object result = Class.forName(mDataClass.getName()).newInstance();
            Field[] subclass = mDataClass.getDeclaredFields();
            Class superclass = mDataClass.getSuperclass();
            // 父类的数据
            Field[] superSubClass = superclass.getDeclaredFields();
            for (int i = 0; i < superSubClass.length; i++) {

                Field valField = superSubClass[i];
                String valFieldType = valField.getType().toString();
                valField.setAccessible(true);
                if ("class java.lang.String".equals(valFieldType)) {
                    valField.set(result, cursor.getString(cursor.getColumnIndex(valField.getName())));
                } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                    valField.set(result, cursor.getLong(cursor.getColumnIndex(valField.getName())));
                } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                    valField.set(result, cursor.getInt(cursor.getColumnIndex(valField.getName())));
                } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                    valField.set(result, cursor.getDouble(cursor.getColumnIndex(valField.getName())));
                }
            }
            // 子类数据
            for (int i = 0; i < subclass.length; i++) {

                Field valField = subclass[i];
                String valFieldType = valField.getType().toString();
                valField.setAccessible(true);
                if ("class java.lang.String".equals(valFieldType)) {
                    valField.set(result, cursor.getString(cursor.getColumnIndex(valField.getName())));
                } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                    valField.set(result, cursor.getLong(cursor.getColumnIndex(valField.getName())));
                } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                    valField.set(result, cursor.getInt(cursor.getColumnIndex(valField.getName())));
                } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                    valField.set(result, cursor.getDouble(cursor.getColumnIndex(valField.getName())));
                }
            }
            resultList.add((T) result);
        }
        if ((!cursor.isClosed())) {
            cursor.close();
            cursor = null;
        }
        return resultList;
    }

    @Override
    public void close() {

        if (sDBHelper != null) {
            sDBHelper.close();
            sDBHelper = null;
        }
    }

    @Override
    public List<T> queryAllData() throws Exception {
        if (mDataClass == null) {
            throw new ClassNotFoundException("do not have class ");
        }
        ArrayList<T> resultList = null;
        // 查询数据库
        String[] tableInfo = getTableInfo();
        if (tableInfo != null && tableInfo.length > 0) {
            SQLiteDatabase db = null;
            try {
                db = sDBHelper.getWritableDatabase();
                // 查询数据库
                Cursor cursor = db.query(mDataClass.getSimpleName(), tableInfo, null, null, null, null, null);
                resultList = getDataFromCursor(cursor);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
        }

        return resultList;
    }

    /**
     *
     */
    @Override
    public List<T> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                         String having, String orderBy) throws Exception {

        SQLiteDatabase db = null;
        db = sDBHelper.getWritableDatabase();
        ArrayList<T> resultList = null;
        if (db == null) {
            return resultList;
        }
        // 查询数据库
        Cursor cursor = db
                .query(mDataClass.getSimpleName(), columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            resultList = getDataFromCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db.isOpen()) {
            db.close();
            db = null;
        }
        return resultList;
    }

    @Override
    public List<T> queryForFieldValues(Map<String, Object> fieldValues) throws Exception {

        if (mDataClass == null) {
            throw new SQLException(DO_HAVE_CLASS);
        }
        ArrayList<T> resultList = null;
        String[] tableInfo = getTableInfo();
        if (tableInfo != null && tableInfo.length > 0) {
            SQLiteDatabase db = null;
            try {
                db = sDBHelper.getWritableDatabase();
                // 查询数据库
                // 遍历map对象获得selection和对应的值
                String[] valuedArr = null;
                StringBuffer selectionSb = null;
                int i = 0;
                if (fieldValues != null) {
                    valuedArr = new String[fieldValues.size()];
                    selectionSb = new StringBuffer();
                    Iterator iter = fieldValues.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        String val = (String) entry.getValue();
                        valuedArr[i++] = val;
                        selectionSb.append(key);
                        selectionSb.append(EQUAL_STRING);
                        if (i < fieldValues.size()) {
                            selectionSb.append(" and ");
                        }
                    }
                }

                Cursor cursor = db.query(mDataClass.getSimpleName(), tableInfo,
                        selectionSb == null ? null : selectionSb.toString(), valuedArr, null, null, null);

                resultList = getDataFromCursor(cursor);

            } catch (ClassNotFoundException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw e;
            } catch (InstantiationException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
        }
        return resultList;
    }

    @Override
    public int updateTalbeInfo(ContentValues values, Map<String, Object> fieldValues) throws SQLException {

        if (mDataClass == null) {
            throw new SQLException(DO_HAVE_CLASS);
        }
        int resultFlag = -1;
        SQLiteDatabase db = sDBHelper.getWritableDatabase();
        // 查询数据库
        // 遍历map对象获得selection和对应的值
        String[] valuedArr = null;
        StringBuffer selectionSb = null;
        int i = 0;
        if (fieldValues != null) {
            Iterator iterator = fieldValues.entrySet().iterator();
            valuedArr = new String[fieldValues.size()];
            selectionSb = new StringBuffer();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                valuedArr[i++] = val;
                selectionSb.append(key);
                selectionSb.append(EQUAL_STRING);
            }
        }
        resultFlag = db.update(mDataClass.getSimpleName(), values, selectionSb == null ? null : selectionSb.toString(),
                valuedArr);
        db.close();
        return resultFlag;
    }

    @Override
    public int delTalbelInfo(Map<String, Object> fieldValues) throws SQLException {
        if (mDataClass == null) {
            throw new SQLException(DO_HAVE_CLASS);
        }
        int resultFlag = -1;
        SQLiteDatabase db = sDBHelper.getWritableDatabase();
        // 遍历map对象获得selection和对应的值

        String[] valuedArr = null;
        StringBuffer selectionSb = null;
        int i = 0;
        if (fieldValues != null) {
            Iterator iter = fieldValues.entrySet().iterator();
            valuedArr = new String[fieldValues.size()];
            selectionSb = new StringBuffer();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                valuedArr[i++] = val;
                selectionSb.append(key);
                selectionSb.append(EQUAL_STRING);
            }
        }
        resultFlag = db.delete(mDataClass.getSimpleName(), selectionSb == null ? null : selectionSb.toString(),
                valuedArr);
        return resultFlag;
    }

    @Override
    public int insertDataToDB(ArrayList<T> dataList) throws SQLException {
        if (mDataClass == null) {
            throw new SQLException(DO_HAVE_CLASS);
        }
        SQLiteDatabase db = sDBHelper.getWritableDatabase();
        db.execSQL("PRAGMA cache_size=12000;");
        db.beginTransaction();
        // 批量插入数据
        for (int i = 0; i < dataList.size(); i++) {

        }

        return 0;
    }
}
