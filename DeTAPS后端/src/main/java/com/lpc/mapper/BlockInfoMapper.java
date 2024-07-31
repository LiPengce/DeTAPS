package com.lpc.mapper;

import com.lpc.pojo.BlockInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-21
 */

@Mapper
public interface BlockInfoMapper {

    @Select("SELECT * FROM BlockData ORDER BY id DESC LIMIT 6")
    List<BlockInfo> getLastSixBlockInfo();

    @Select("SELECT COUNT(id) FROM BlockData")
    int getBlockCount();
}
