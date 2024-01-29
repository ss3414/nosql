//package com.demo.initial;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.demo.mapper.ProductMapper;
//import com.demo.model.Product;
//import com.demo.repository.ProductRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Slf4j
//@Order(1)
//@Component
//public class Runner implements ApplicationRunner {
//
//    @Autowired
//    private ElasticsearchOperations operations;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    /* 启动项目前同步ES */
//    @Override
//    public void run(ApplicationArguments args) {
//        /* 手动创建索引/@Document注解会自动创建索引 */
//        boolean exists = operations.indexOps(Product.class).exists();
//        if (!exists) {
//            log.info(String.valueOf(operations.indexOps(Product.class).create()));
//        }
//
//        /* fixme 类型转换错误 */
//        var list = productRepository.findAll();
//
//        if (productRepository.count() != productMapper.selectCount(new QueryWrapper<>())) {
//            productRepository.deleteAll();
//            List<Product> productList = productMapper.selectList(new QueryWrapper<>());
//            productRepository.saveAll(productList);
//        }
//    }
//
//}
