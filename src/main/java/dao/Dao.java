package dao;

import java.sql.SQLException;
import java.util.Set;

/**
 * Database entities DAO interface.
 * @author Tomoto
 * @date 2020/11/2 8:46
 */
public interface Dao<T, I> extends AutoCloseable {
//    Dao getInstance(); // static

    /**
     * Create a new entity.
     *
     * @param entity the new entity
     * @return the dao itself
     */
    Dao<T, I> create(T entity);

    /**
     * Insert new entities in bulk.
     * @param entities the new entities
     * @return the dao itself
     */
    Dao<T, I> createInBulk(T... entities);

    /**
     * Delete an entity by its id.
     *
     * @param id the id for delete
     * @return the dao itself
     */
    Dao<T, I> delete(I id);

    /**
     * Read an entity by its id.
     *
     *
     * @param tClass
     * @param id the id for read.
     * @return the entity
     */
    T read(Class<T> tClass, I id);

    /**
     * Read all entities.
     *
     * @return a list of entities
     * @param tClass
     */
    Set<T> readAll(Class<T> tClass);

    /**
     * Update an entity by its id.
     *
     * @param id     the id for update
     * @param entity new entity data.
     * @return the dao itself
     */
    Dao<T, I> update(I id, T entity);

    /**
     * Close the connection.
     */
    @Override
    void close() throws SQLException;
}
