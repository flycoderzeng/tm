package com.tm.ma.utils;

import cn.hutool.core.bean.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JDBCUtils {
    public static Logger logger = LoggerFactory.getLogger(JDBCUtils.class);

    public static void setParameter(PreparedStatement ps, int index, Object value) throws Exception {
        if (value == null) {
            ps.setObject(index, null);
        } else if (value instanceof String) {
            String val = (String) value;
            if ("null".equalsIgnoreCase(val)) {
                ps.setObject(index, null);
            } else if ("setNull".equalsIgnoreCase(val)) {
                ps.setNull(index, Types.NULL);
            } else {
                ps.setString(index, val);
            }
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof Short) {
            ps.setShort(index, (Short) value);
        } else if (value instanceof Float) {
            ps.setFloat(index, (Float) value);
        } else if (value instanceof Double) {
            ps.setDouble(index, (Double) value);
        } else if (value instanceof Byte) {
            ps.setByte(index, (Byte) value);
        } else if (value instanceof byte[]) {
            ps.setBytes(index, (byte[]) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (Long) value);
        } else if (value instanceof Date) {
            ps.setDate(index, (Date) value);
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else if (value instanceof BigDecimal) {
            ps.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof Boolean) {
            ps.setBoolean(index, (Boolean) value);
        } else if (value instanceof Blob) {
            ps.setBlob(index, (Blob) value);
        } else if (value instanceof Clob) {
            ps.setClob(index, (Clob) value);
        } else if (value instanceof NClob) {
            ps.setNClob(index, (NClob) value);
        } else if (value instanceof ByteArrayInputStream) {
            ps.setBinaryStream(index, (ByteArrayInputStream) value);
        } else {
            ps.setObject(index, value);
        }
    }

    public static boolean doExecute(Connection conn, String sql) throws Exception {
        Statement ps = null;
        try {
            ps = conn.createStatement();
            return ps.execute(sql);
        } finally {
            close(ps);
        }
    }

    public static int doExecuteUpdate(Connection conn, String sql) throws Exception {
        Statement ps = null;
        try {
            ps = conn.createStatement();
            return ps.executeUpdate(sql);
        } finally {
            close(ps);
        }
    }

    public static List<Map<String, Object>> doQuery(Connection conn, String sql) throws Exception {
        List<Map<String, Object>> ret = new ArrayList<>();
        Statement ps = null;
        try {
            ps = conn.createStatement();
            ps.setQueryTimeout(180);
            ResultSet rs = ps.executeQuery(sql);
            Map<Integer, String> fieldMap = getFieldTitle(rs);
            while (rs.next()) {
                ret.add(getRowData(fieldMap, rs));
            }
        } catch (Exception e) {
            throw new Exception(sql, e);
        } finally {
            close(ps);
        }
        return ret;
    }

    public static <T> List<T> doQuery(Connection conn, String sql, Class<T> clazz) throws Exception {
        List<Map<String, Object>> maps = doQuery(conn, sql);
        List<T> list = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            T row = BeanUtil.fillBeanWithMap(map, clazz.newInstance(), false);
            list.add(row);
        }
        return list;
    }

    public static List<Map<String, Object>> doQuery(Connection conn, String sql, Object... param) throws Exception {
        PreparedStatement ps = null;
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    setParameter(ps, i + 1, param[i]);
                }
            }
            ResultSet rs = ps.executeQuery();
            Map<Integer, String> fieldMap = getFieldTitle(rs);
            while (rs.next()) {
                ret.add(getRowData(fieldMap, rs));
            }
        } finally {
            close(ps);
        }
        return ret;
    }

    public static long doUpdate(Connection conn, String sql, Object... param) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    setParameter(ps, i + 1, param[i]);
                }
            }
            int effected = ps.executeUpdate();
            if(conn.getMetaData().getDatabaseProductName().equals("MySQL") && effected < 2) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
            return effected;
        } finally {
            close(ps);
        }
    }

    public static int doCacheStatement(Connection conn, String sql, Object... param) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    setParameter(ps, i + 1, param[i]);
                }
            }
            ps.executeUpdate();
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    setParameter(ps, i + 1, param[i]);
                }
            }
            int r = ps.executeUpdate();
            return r;
        } finally {
            close(ps);
        }
    }

    public static List doBatchQuery(Connection conn, String sql, Object[]... params) throws Exception {
        List<Map<String, Object>> ret = new ArrayList<>();
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (Object[] param : params) {
                for (int j = 0; j < param.length; j++) {
                    setParameter(ps, j + 1, param[j]);
                }
                ps.addBatch();
            }
            int[] r = ps.executeBatch();
            conn.commit();
        } finally {
            close(ps);
        }
        return ret;
    }

    public static int[] doBatchUpdate(Connection conn, String sql, Object[]... params) throws Exception {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object[] param = params[i];
                for (int j = 0; j < param.length; j++) {
                    setParameter(ps, j + 1, param[j]);
                }
                ps.addBatch();
                if (i > 0 && i % 10000 == 0) {
                    ps.executeBatch();
                }
            }
            int[] r = ps.executeBatch();
            conn.commit();
            return r;
        } finally {
            close(ps);
        }
    }

    public static void doCommit(Connection conn, String[] sqls) throws Exception {
        conn.setAutoCommit(false);
        for (String sql : sqls) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
        }
        conn.commit();
    }

    public static void doRollback(Connection conn, String[] sqls) throws Exception {
        conn.setAutoCommit(false);
        for (String sql : sqls) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
        }
        conn.rollback();
    }

    public static Integer insertReturnId(Connection conn, String sql, int columnIndex, Object... param) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql, new int[]{columnIndex});
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    setParameter(ps, i + 1, param[i]);
                }
            }
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            return null;
        } finally {
            close(ps);
        }
    }

    public static List ThreadPool(final DataSource ds, final String sql, final Object... params) throws SQLException {
        int threadNum = 20, taskNum = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Object> result = new LinkedList<>();
        final List<Object> synchronizedList = Collections.synchronizedList(result);
        for (int i = 0; i < taskNum; i++) {
            executor.execute(() -> {
                try {
                    Connection conn = ds.getConnection();
                    if (sql.startsWith("select")) {
                        List ret = doQuery(conn, sql, params);
                        synchronizedList.add(ret);
                    } else {
                        long r = doUpdate(conn, sql, params);
                        synchronizedList.add(r);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        executor.shutdown();

        try {
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        return result;
    }

    public static List ThreadPool(final DataSource ds, int threadNum, int taskNum, final String sql, final Object... params) throws SQLException {
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Object> result = new LinkedList<>();
        final List<Object> synchronizedList = Collections.synchronizedList(result);
        for (int i = 0; i < taskNum; i++) {
            executor.execute(() -> {
                try {
                    Connection conn = ds.getConnection();
                    if (sql.toLowerCase().startsWith("select")) {
                        List ret = doQuery(conn, sql, params);
                        synchronizedList.addAll(ret);
                    } else {
                        long r = doUpdate(conn, sql, params);
                        synchronizedList.addAll(Arrays.asList(r));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        executor.shutdown();

        try {
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        return result;
    }

    public static List ThreadPoolBatch(final DataSource ds, int threadNum, int taskNum, final String sql, final Object[]... params) throws SQLException {
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Object> result = new LinkedList<>();
        final List<Object> synchronizedList = Collections.synchronizedList(result);
        for (int i = 0; i < taskNum; i++) {
            executor.execute(() -> {
                try {
                    Connection conn = ds.getConnection();
                    if (sql.startsWith("select")) {
                        List ret = doBatchQuery(conn, sql, params);
                        synchronizedList.addAll(ret);
                    } else {
                        int[] r = doBatchUpdate(conn, sql, params);
                        synchronizedList.addAll(Arrays.asList(r));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        executor.shutdown();

        try {
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        return synchronizedList;
    }

    public static boolean isTableExists(Connection conn, String tableName, String checkTableExistSql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(checkTableExistSql);
        } catch (SQLException e) {
            logger.error("error, ", e);
            return false;
        }

        try {
            ps.executeQuery();
        } catch (SQLException e) {
            logger.error("error, ", e);
            if (e.getMessage().contains("doesn't exist") || e.getMessage().contains("does not exist")) {
                return false;
            }
            if (e.getMessage().contains("对象名 'test." + tableName + "' 无效")) {
                return false;
            }
            if (e.getMessage().contains("表或视图不存在")) {
                return false;
            }
        }
        return true;
    }

    public static Map<Integer, String> getFieldTitle(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int count = md.getColumnCount();
        Map<Integer, String> fieldMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String val = md.getColumnLabel(i + 1);
            String fieldName = val == null || "".equals(val) ? "NULL" : val;
            fieldMap.put(i, fieldName);
        }
        return fieldMap;
    }

    public static Map<String, Object> getRowData(ResultSet rs) throws SQLException {
        return getRowData(getFieldTitle(rs), rs);
    }

    public static Map<String, Object> getRowData(Map<Integer, String> fieldMap, ResultSet rs) throws SQLException {
        Map<String, Object> row = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 0; i < count; ++i) {
            Object object = rs.getObject(i + 1);
//            if(object instanceof byte[]) {
//                object = new String((byte[])object, StandardCharsets.UTF_8);
//            }
            row.put(fieldMap.get(i), object);
        }
        return row;
    }

    public static void close(Closeable x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            logger.debug("close error ", e);
        }
    }

    public static void close(Statement x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            logger.debug("close error ", e);
        }
    }

    public static void close(Connection x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            logger.debug("close error ", e);
        }
    }
}
