# nosql
非关系型数据库<br>

************************************************************************************************************************

# springboot_es
①MBP+ES6.2.2+spring-data-elasticsearch3.2.7+elasticsearch-rest-high-level-client6.4.3（HttpClient4.4）<br>
②spring-data-elasticsearch<br>
spring-data-elasticsearch通过@Document+ES Repository可以实现直接对ES快速CURD（端口9300）<br>
HTTP：9200
TCP：9300（7.0废弃）
③功能<br>
initial：启动项目前同步ES<br>
demo：CURD+分页+查询<br>

# springboot_mongodb
文档型数据库MongoDB<br>
（MongoDB可以看作动态表结构的关系型数据库）<br>

# springboot_rabbitmq
①Gradle<br>
②接收消息
MQListener1：@RabbitListener
MQListener2：MQConfig+MessageListener接口

# springboot_redis
①键值对型非关系型数据库Redis<br>
手动插入键值对/对象<br>
②spring-session-data-redis<br>
@EnableCaching+@Cacheable<br>
