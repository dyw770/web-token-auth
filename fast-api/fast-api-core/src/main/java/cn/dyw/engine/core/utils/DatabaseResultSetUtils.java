package cn.dyw.engine.core.utils;

import cn.dyw.engine.core.exception.EngineExecException;
import cn.dyw.engine.core.exec.result.DataSetResult;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行结果工具
 *
 * @author dyw770
 * @since 2021-08-03
 */
public final class DatabaseResultSetUtils {

    /**
     * 处理结果
     *
     * @param resultSet 查询结果
     * @param result    报表结果
     */
    public static void handleResultSet(ResultSet resultSet, DataSetResult result)
            throws SQLException {

        List<String> schema = new ArrayList<>(16);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            schema.add(columnName);
        }

        result.initSchema(schema);

        Map<String, Object> data;
        while (resultSet.next()) {
            data = new HashMap<>(16);
            for (String columnName : schema) {
                Object value = resultSet.getObject(columnName);
                data.put(columnName, value);
            }
            result.addData(data);
        }
    }

    public static Object handleOneResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        String columnName = metaData.getColumnName(1);

        while (resultSet.next()) {
            return resultSet.getObject(columnName);
        }

        throw new EngineExecException("空结果集");
    }
}
