package com.lpc.mapper;

import com.lpc.pojo.Signer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("<script>" +
            "SELECT * FROM " +
            "<choose>" +
            "   <when test='role.equals(\"签名者\")'>" +
            "       signer" +
            "   </when>" +
            "   <when test='role.equals(\"公证人\")'>" +
            "       notary" +
            "   </when>" +
            "</choose>" +
            " WHERE username = #{username}" +
            "</script>")
    Signer findUserByName(@Param("username") String username, @Param("role") String role);

    @Insert("<script>" +
            "insert into " +
            "<choose>" +
            "   <when test='role.equals(\"签名者\")'>" +
            "       signer" +
            "   </when>" +
            "   <when test='role.equals(\"公证人\")'>" +
            "       notary" +
            "   </when>" +
            "</choose>" +
            " (username, password) values (#{username}, #{password})" +
            "</script>")
    void add(String role,String username,String password);


    @Insert("insert into notary (username,password) values (#{username}, #{sm3psd})")
    void addNotary(String username,String sm3psd);

    @Insert("insert into signer (username,password,pk) values (#{username}, #{sm3psd},#{pk})")
    void addSigner(String username, String sm3psd,String pk);

    @Select("select max(id) as maxId from signer")
    Integer getMaxIdFromSigner(); //防止空表


    @Select("select pk from signer_key by id")
    String getpkById(Integer id); //防止空表

}
