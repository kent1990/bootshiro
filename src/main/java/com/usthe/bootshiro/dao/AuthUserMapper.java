package com.usthe.bootshiro.dao;

import com.usthe.bootshiro.domain.bo.AuthUser;
import org.springframework.dao.DataAccessException;
import java.util.List;

/**
 * @author tomsun28
 * @date 10:35 2018/4/22
 */
public interface AuthUserMapper {

    /**
     * description TODO
     *
     * @param record 1
     * @return int
     * @throws DataAccessException when
     */
    int insert(AuthUser record) throws DataAccessException;

    /**
     * description TODO
     *
     * @param appId 1
     * @return java.lang.String
     * @throws DataAccessException when
     */
    String selectUserRoles(String appId) throws DataAccessException;
}