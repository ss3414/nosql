package com.module.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "product", type = "product") /* index（ES库名），type（ES表名） */
public class Product extends Model<Product> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String category;

    private Float price;

    private String area;

    private String code;

    private Integer score;

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + ", area=" + area + ", code=" + code + ", score=" + score + "]";
    }

}
