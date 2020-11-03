package dao.impl;

import dao.Dao;
import dao.util.DaoUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomoto
 * <p>
 * 2020/11/2 18:49
 */
public class DaoImpl<T, I> implements Dao<T, I> {
    private final Connection connection;

    public DaoImpl() throws IOException, SQLException {
        connection = DaoUtil.getConnection();
    }

    @Override
    public DaoImpl<T, I> create(T entity) {
        Class<?> entityClass = entity.getClass();
        String tableName = entityClass.getSimpleName() + "_table";
        Field[] fields = entityClass.getDeclaredFields(); // get T fields

        StringBuilder fieldsStatement = new StringBuilder();
        StringBuilder valuesStatement = new StringBuilder();
        fieldsStatement.append("INSERT INTO `").append(tableName).append("` (");
        valuesStatement.append("'");
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            fieldsStatement.append(fields[i].getName()); // build parameter list
            try {
                valuesStatement.append(fields[i].get(entity)); // build value list
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (i != fields.length - 1) { // if not the last one, append comma for next one
                fieldsStatement.append(", ");
                valuesStatement.append("', '");
            }
        }
        fieldsStatement.append(") VALUES (").append(valuesStatement).append("');"); // combine two statement

        try (Statement connectionStatement = connection.createStatement()) {
            connectionStatement.executeUpdate(fieldsStatement.toString()); // execute the statement
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @SafeVarargs
    @Override
    public final DaoImpl<T, I> createInBulk(T... entities) {
        return null;
    }

    @Override
    public DaoImpl<T, I> delete(I id) {
        return null;
    }

    @Override
    public T read(Class<T> tClass, I id) {
        Field[] fields = tClass.getDeclaredFields();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT * FROM `" + tClass.getSimpleName() + "_table` " +
                             "WHERE " + fields[0].getName() + " = '" + id + "';")) {
            if (resultSet.next()) { // read once
                return (T) readOne(tClass, fields, resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<T> readAll(Class<T> tClass) {
        HashSet<T> result = new HashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + tClass.getSimpleName() + "_table`;")) {
            Field[] fields = tClass.getDeclaredFields();
            while (resultSet.next()) { // read multiple times
                result.add((T) readOne(tClass, fields, resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Gets result set fist element.
     * <p>
     * Simplify the structure of read / readAll code.
     *
     * @param tClass    the instance origin type
     * @param fields    the type fields
     * @param resultSet the result set
     * @return the first instance
     */
    private Object readOne(Class<T> tClass, Field[] fields, ResultSet resultSet) {
        try {
            Object instance = tClass.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = resultSet.getString(name);
                field.set(instance, value);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DaoImpl<T, I> update(I id, T entity) {
        return null;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
