package com.lpc.controller;

import com.lpc.mapper.BlockInfoMapper;
import com.lpc.mapper.TxInfoMapper;
import com.lpc.pojo.BlockInfo;
import com.lpc.pojo.TransactionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-21
 */

@RestController
@RequestMapping("/block")
@CrossOrigin
public class BlockInfoController {

    @Autowired
    BlockInfoMapper blockInfoMapper;

    @Autowired
    TxInfoMapper txInfoMapper;

    @GetMapping("/blockInfo")
    public List<BlockInfo> getLastSixBlockInfo() {
        return blockInfoMapper.getLastSixBlockInfo();
    }

    @GetMapping("/blockCount")
    public int getBlockCount(){
        return blockInfoMapper.getBlockCount();
    }

    @GetMapping("/txInfo")
    public List<TransactionInfo>getLastSixTxInfo(){
        return txInfoMapper.getLastSixTxInfo();
    }

    @GetMapping("/txCount")
    public int getTxCount(){
        return txInfoMapper.getTxCount();
    }

    @GetMapping("/findTxByHash")
    public TransactionInfo getTxByHash(String hash){
        String hash2 = hash.trim();
        TransactionInfo txInfoByHash = txInfoMapper.getTxInfoByHash(hash2);

        return txInfoByHash;
    }


}
