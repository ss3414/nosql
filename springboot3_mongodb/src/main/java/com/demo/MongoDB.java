package com.demo;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@SpringBootApplication
public class MongoDB {

    public static void main(String[] args) {
        SpringApplication.run(MongoDB.class, args);
    }

    @RequestMapping("/")
    public Map index() {
        return new LinkedHashMap();
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /* MongoRepository */
    @RequestMapping("/test")
    public Map test() {
//        User user = userRepository.save(User.builder().name("name123").password("pwd1").build()); /* 主键回写 */

        /* Example查询 */
//        Example<User> userExample = Example.of(User.builder().name("name123").build());
//        Optional<User> record = userRepository.findOne(userExample);
//        System.out.println(record.get());

        userRepository.delete(User.builder().id("5ea793545eaf338c9c4e90db").build());
        return new LinkedHashMap();
    }

    /* MongoTemplate */
    @RequestMapping("/test2")
    public Map test2() {
//        User user = mongoTemplate.save(User.builder().name("name123").password("pwd1").build());

//        Query query = new Query(Criteria.where("name").is("name123"));
//        User user2 = mongoTemplate.findOne(query, User.class);

        Query query2 = new Query(Criteria.where("id").is("5ea793545eaf338c9c4e90db"));
        Update update = new Update()
            .set("name", "name123")
            .set("test", "test123");
        mongoTemplate.updateFirst(query2, update, User.class);
        return new LinkedHashMap();
    }

    /* 边查边改 */
    @RequestMapping("/test3")
    public Map test3() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            mongoTemplate.updateFirst(
                new Query(Criteria.where("id").is(user.getId())),
                new Update().set("test", "test123"),
                User.class);
        }
        return new LinkedHashMap();
    }

    @Value("${spring.data.mongodb.auth}")
    private String url;

    @RequestMapping("/test4")
    public Map test4() {
        /* 连接 */
        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
        MongoCollection<Document> mongoCollection = mongoClient.getDatabase("untitled").getCollection("user");

        /* 查询 */
//        FindIterable<Document> iterator = mongoCollection.find(); /* 查询所有 */
//        BasicDBObject query = new BasicDBObject("name", "name123"); /* 条件查询 */
//        BasicDBObject query = new BasicDBObject("name", "null"); /* 为空 */
//        BasicDBObject query = new BasicDBObject("name", new BasicDBObject("$ne", "null")); /* 不为空 */
//        BasicDBObject query = new BasicDBObject("name", new BasicDBObject("$exists", true)); /* 字段存在 */
        BasicDBObject[] conditions = {
            new BasicDBObject("name", "name123"),
            new BasicDBObject("password", new BasicDBObject("$exists", false))
        };
        BasicDBObject query = new BasicDBObject();
        query.put("$and", conditions); /* 多条件 */
        FindIterable<Document> iterator = mongoCollection.find(query);
        MongoCursor<Document> cursor = iterator.iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();

            /* 更新 */
            ObjectId id = document.getObjectId("_id");
            BasicDBObject update = new BasicDBObject("_id", id);
            Bson data = Updates.set("name", "name123");
            mongoCollection.updateOne(update, data);
        }

//        /* 更新 */
//        BasicDBObject update = new BasicDBObject("_id", new ObjectId("5ea793545eaf338c9c4e90db"));
//        Bson data = Updates.set("test", "test456");
//        mongoCollection.updateOne(update, data);
        return new LinkedHashMap();
    }

}
