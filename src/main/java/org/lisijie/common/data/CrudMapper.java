package org.lisijie.common.data;

import java.util.Map;

/**
 * 规范DAO常用的CRUD方法
 * @author jesse
 */
public interface CrudMapper<T> {

    /**
     * 插入一条记录
     * @param entity
     * @return 返回插入的对象
     */
    T insert(T entity);

    /**
     * 删除一条记录
     * @param entity 要删除的实体对象
     * @return 返回删除数量
     */
    int delete(T entity);

    /**
     * 根据主键ID删除一条记录
     * @param id
     * @return
     */
    int deleteById(int id);

    /**
     * 更新记录
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 根据ID查询一条记录
     * @param id
     * @return 返回实体对象
     */
    T findById(int id);

    /**
     * 统计记录数
     * @param filter 过滤条件
     * @return
     */
    int count(Map<String, Object> filter);
}
