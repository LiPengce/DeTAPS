package com.lpc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SignerMapper {
    @Select("select pk from signer_key order by id asc")
    List<String> getAllPublicKeys();

    @Select("select pke from pkec where id = #{id}")
    String getPublicKeyEncryptionById(int id);

    @Select("<script>" +
            "select upk from notary_key where pid in " +
            "<foreach item='item' collection='pids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<String> findUpksByPids(@Param("pids") List<String> pids);

    @Select("select pk from signer_key where id = #{id}")
    String findpkById(int id);

}
