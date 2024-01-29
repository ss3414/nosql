package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName(value = "product")
@Document(indexName = "product")
public class Product extends Model<Product> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Field(type = FieldType.Text)
    private String uuid;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Float)
    private Float price;

    @Field(type = FieldType.Text)
    private String area;

    @Field(type = FieldType.Text)
    private String code;

    @Field(type = FieldType.Integer)
    private Integer score;

}
