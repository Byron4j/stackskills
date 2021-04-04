package org.byron4j;


import org.byron4j.mp.entity.User;
import org.byron4j.mp.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseTestCase {
    @Autowired
    private UserService userService;

    @Test
    public void 测试Service() {
        boolean save =
                userService.save(User.builder()
                        .userName("lb")
                        .name("李白")
                        .password("111111")
                        .build());
        Assert.assertTrue(save);
    }


}

