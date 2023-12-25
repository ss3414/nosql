# nosql
非关系型数据库<br>

************************************************************************************************************************

# springboot3_es（待处理）
①MBP+ES6.2.2+spring-data-elasticsearch3.2.7+elasticsearch-rest-high-level-client6.4.3（HttpClient4.4）<br>
②spring-data-elasticsearch<br>
spring-data-elasticsearch通过@Document+ES Repository可以实现直接对ES快速CURD（端口9300）<br>
HTTP：9200
TCP：9300（7.0废弃）
③功能<br>
启动项目前同步ES<br>
CURD+分页+查询<br>

# springboot3_mongodb
文档型数据库MongoDB<br>
（MongoDB可以看作动态表结构的关系型数据库）<br>

# springboot3_kafka
Kafka<br>

# springboot3_redis
①键值对型非关系型数据库Redis<br>
手动插入键值对/对象<br>
②缓存<br>
实体类需要实现Serializable接口<br>
③Redis执行Lua脚本<br>
