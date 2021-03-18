package com;

import com.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.repository.UserRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /* MongoRepository */
//    @Test
    public void test() {
//        User user = userRepository.save(User.builder().name("name123").password("pwd1").build()); /* 主键回写 */

        /* Example查询 */
//        Example<User> userExample = Example.of(User.builder().name("name123").build());
//        Optional<User> record = userRepository.findOne(userExample);
//        System.out.println(record.get());

        userRepository.delete(User.builder().id("5ea793545eaf338c9c4e90db").build());
    }

    /* MongoTemplate */
//    @Test
    public void test2() {
//        User user = mongoTemplate.save(User.builder().name("name123").password("pwd1").build());

//        Query query = new Query(Criteria.where("name").is("name123"));
//        User user2 = mongoTemplate.findOne(query, User.class);

        Query query2 = new Query(Criteria.where("id").is("5ea793545eaf338c9c4e90db"));
        Update update = new Update()
//                .set("name", "name123")
                .set("test", "test123");
        mongoTemplate.updateFirst(query2, update, User.class);
    }

    /* 边查边改 */
//    @Test
    public void test3() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("id").is(user.getId())),
                    new Update().set("test", "test123"),
                    User.class);
        }
    }

    @Value("${spring.data.mongodb.auth}")
    private String url;

    /* MongoClient */
    @Test
    public void test4() {
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
    }

}
