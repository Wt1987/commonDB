package commondb.db.table;

import java.io.Serializable;

import commondb.db.DataBaseCommonUtil;
import commondb.db.DefaultDao;


public class BaseData implements Serializable {
    // 数据集UUID 使用 java.util.UUID.randomUUID().toString();生成
    public String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // 保存到数据库
    public boolean saveToDB() {
        return DefaultDao.getInstance(DataBaseCommonUtil.getmInstance()).dataSave(this);
    }

    // 更新数据
    public boolean modifyToDB() {
        return DefaultDao.getInstance(DataBaseCommonUtil.getmInstance()).dataModify(this);
    }

    // 删除数据
    public boolean deleteFromDB() {
        return DefaultDao.getInstance(DataBaseCommonUtil.getmInstance()).dataDelete(this);
    }

    // 判断数据库是否存在
    public boolean isExistInDataBase() {
        return DefaultDao.getInstance(DataBaseCommonUtil.getmInstance()).isExistInDataBase(this);
    }

}
