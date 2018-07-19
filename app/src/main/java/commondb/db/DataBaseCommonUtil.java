/*
 * Copyright (C) 2017 贵阳货车帮科技有限公司
 */

package commondb.db;

import android.content.Context;

public final class DataBaseCommonUtil {

    /**
     * 在你使用数据库工具包之前，一定要调用setmInstance方法为后续的，数据库操作提供上下文对象， 如果你没有调用该方法，在程序是无法正常运行的
     */

    private static Context sContext = null;

    public static Context getmInstance() {
        return sContext;
    }

    public static void setInstance(Context mInstance) {
        DataBaseCommonUtil.sContext = mInstance;
    }

    private DataBaseCommonUtil() {

    }


}
