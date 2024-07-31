package com.lpc.mapper;

import com.lpc.pojo.NotarialRecord;
import com.lpc.response.DetapsSign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotaryMapper {
    @Select("select pke from pket where id= #{id}")
    String getPublicKeyEncryptionById(int id);

    @Select("select id, gid, m, signature from detaps_sign")
    List<DetapsSign> getAllDetapsSigns();

    @Select("select upk from notary_key where id= #{id}")
    String finduvkById(Integer id);

    @Select("select * from crimerecords")
    List<NotarialRecord>getRecords();
}
