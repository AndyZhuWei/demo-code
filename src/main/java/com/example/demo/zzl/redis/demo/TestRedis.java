package com.example.demo.zzl.redis.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author HP
 * @Description
 * @date 2020/11/25-10:04
 */
@Component
public class TestRedis {


    //通过spring-boot-starter向容器中添加了一个RedisTemplate对象
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void testRedis()  {

        //opsForValue表示字符串类型操作
        redisTemplate.opsForValue().set("hello","china");


        //可以打印出hello，如果用redis-cli登陆查看，则使用keys * 输出一个乱码的串（1) "\xac\xed\x00\x05t\x00\x05hello"），
        // 并不是hello，因为redis是二进制安全的
        //只存储字节数组，任何客户端要注意一个事情，就是我的数据是怎么编程二进制数组的
        //其实redisTemplate这种高级api是面向java序列化的，java序列化的时候会加一些东西并不是字面的字符串
        //除了redisTemplate这种template还有一种专门面向字符串的template即StringRedisTemplate
        //此时在打印keys * 可以看到正确输出了"stringRedisTemplate"
        System.out.println(redisTemplate.opsForValue().get("hello"));

        stringRedisTemplate.opsForValue().set("hello01","china");
        System.out.println(stringRedisTemplate.opsForValue().get("hello01"));




        /////////low api//////////////
        //存入的key就是字面字符串
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.set("hello02".getBytes(),"zhuwei".getBytes());
        System.out.println(new String(connection.get("hello02".getBytes())));





        //使用hash类型
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.put("zhuwei","name","andy");
        hash.put("zhuwei","age","12");

        System.out.println(hash.entries("zhuwei"));



        /////////处理对象///////////
        Person person = new Person();
        person.setUserName("zhuwei");
        person.setAge(23);

        //通过jm将我们的对象直接转换成map对象，false表示不对对象内部的对象在展开
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper,false);

        //此处如果使用redisTemplate，则使用的是java的序列化，存入到redis服务器的时候还是有一些乱码
        //但是如果使用stringRedisTemplate，则会因为年龄字段是整数型而报错，此时，我们需要自定义
        //hashValue的序列化
        stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));


        stringRedisTemplate.opsForHash().putAll("andy",jm.toHash(person));
        Map andy = stringRedisTemplate.opsForHash().entries("andy");
        Person person1 = objectMapper.convertValue(andy, Person.class);
        System.out.println(person1.getUserName());

        ///自定义template,使其在初始化时就已经定义好了序列化的类型////////////////
        //只需要在java配置类中进行配置，任何注入即可，参照MyTemplate中的ooxx


        //发布订阅中的发布
       stringRedisTemplate.convertAndSend("ooxx","1");

        //发布订阅中的订阅
        RedisConnection connection1 = stringRedisTemplate.getConnectionFactory().getConnection();
        connection1.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] bytes) {
                byte[] body = message.getBody();
                System.out.println(new String(body));
            }
        },"ooxx".getBytes());

        while (true) {
            System.out.println("===================");
            stringRedisTemplate.convertAndSend("ooxx","hello from wo zi ji");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }




}
