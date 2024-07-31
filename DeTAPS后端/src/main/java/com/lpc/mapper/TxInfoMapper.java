package com.lpc.mapper;

import com.lpc.pojo.TransactionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-21
 */

@Mapper
public interface TxInfoMapper {

    @Select("SELECT * FROM transactions ORDER BY id DESC LIMIT 6")
    List<TransactionInfo> getLastSixTxInfo();

    @Select("SELECT COUNT(id) FROM transactions")
    int getTxCount();

    @Select("SELECT * FROM transactions WHERE Hash=#{hash} LIMIT 1")
    TransactionInfo getTxInfoByHash(String hash);

}
