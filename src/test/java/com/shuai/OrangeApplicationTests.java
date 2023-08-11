package com.shuai;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuai.mapper.PostMapper;
import com.shuai.pojo.po.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrangeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private PostMapper postMapper;

    @Test
    public void test() {
//        postMapper.selectById("120230808143833");
        Post post = postMapper.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getId, "120230808143833"));
    }

}
