package com.example.pro.commondb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import commondb.db.Dao;
import commondb.db.DaoImpl;
import commondb.db.DataBaseCommonUtil;
import commondb.db.table.UserBean;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DataBaseCommonUtil.setInstance(this.getApplicationContext());
        UserBean tempUserBean2 = new UserBean();
        tempUserBean2.setNickname("wr");
        tempUserBean2.saveToDB();
        Dao<UserBean> test = new DaoImpl<>(UserBean.class, DataBaseCommonUtil.getmInstance());

        ArrayList<UserBean> userBeanList = null;
        try {
            userBeanList = (ArrayList<UserBean>) test.queryAllData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("my", "size-----ff-------> " + userBeanList.size());

    }
}
