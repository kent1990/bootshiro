package com.usthe.bootshiro.dao;

import com.usthe.bootshiro.domain.bo.AuthResource;
import com.usthe.bootshiro.shiro.rule.RolePermRule;
import org.springframework.dao.DataAccessException;
import java.util.List;

/**
 * @author tomsun28
 * @date 9:28 2018/4/22
 */
public interface AuthResourceMapper {

    /**
     * description TODO
     *
     * @param record 1
     * @return int
     * @throws DataAccessException when
     */
    int insert(AuthResource record) throws DataAccessException;

    /**
     * description TODO
     *
     * @return java.util.List<com.usthe.bootshiro.shiro.rule.RolePermRule>
     * @throws DataAccessException when
     */
    List<RolePermRule> selectRoleRules()  throws DataAccessException;

}