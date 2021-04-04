package org.byron4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.byron4j.mp.entity.User;
import org.byron4j.mp.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapperTest extends BaseTestCase{
    @Autowired
    private UserMapper userMapper;

    @Test
    public void 根据id查询() {
        User user = userMapper.selectById(1L);
        Assert.assertEquals(user.getUserName(), "zhangsan");
    }

    @Test
    public void 插入(){
        int insert = userMapper.insert(User.builder()
                .age(20)
                .email("wb@qq.cn")
                .name("wb")
                .userName("wb")
                .password("123")
                .build());
        Assert.assertTrue(insert == 1);
    }

    @Test
    public void 删除(){
        int byId = userMapper.deleteById(6);
        Assert.assertTrue(1 == byId);
    }

    @Test
    public void 批量删除(){
        int batchIds = userMapper.deleteBatchIds(Arrays.asList(new Integer[]{1, 2}));
        Assert.assertTrue(2 == batchIds);
    }

    @Test
    public void map条件删除(){
        Map<String,Object> map = new HashMap<>();
        map.put("user_name", "wangwu");

        int deleteByMap = userMapper.deleteByMap(map);
        Assert.assertTrue(1 == deleteByMap);
    }

    @Test
    public void 更新(){
        int update = userMapper.updateById(User.builder().id(4L).name("周六").build());
        Assert.assertTrue(1 == update);
    }

    @Test
    public void 分页查询(){
        IPage page = new Page(2, 5);
        IPage page1 = userMapper.selectPage(page, new QueryWrapper<>());
        Assert.assertTrue(page1 == page);
        Assert.assertTrue(page.getRecords().size() == 5);
    }

    @Test
    public void QueryWrapper(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("name", "password", "email")
                .ge("age", 18)
                .in("name", "张三", "李四");
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }

    @Test
    public void LambdaQueryWrapper(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper
                .eq(User::getUserName, "zhangsan")
                .select(User::getUserName, User::getName, User::getPassword, User::getEmail)
                .orderByDesc(User::getAge);
        List list = userMapper.selectList(lambdaQueryWrapper);
        System.out.println(list);
    }

    @Test
    public void testWrapper8(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name","bbb");
        userMapper.delete(wrapper);
    }

    @Test
    public void updateLambdaQueryWrapper(){
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper();
        lambdaUpdateWrapper
                .set(User::getPassword, "11111111")
                .ge(User::getAge, 35);
        userMapper.update(User.builder().build(), lambdaUpdateWrapper);
    }

}
