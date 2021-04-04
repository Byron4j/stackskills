package org.byron4j.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.byron4j.mp.entity.User;
import org.byron4j.mp.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author byron4j
 * @since 2021-04-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User> {

}
