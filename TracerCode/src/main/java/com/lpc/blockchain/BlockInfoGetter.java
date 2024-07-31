package com.lpc.blockchain;

import com.lpc.pojo.BlockInfo;
import com.lpc.util.JDBCUtilsByDruid;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class BlockInfoGetter {
    private static Web3j web3;

    public static void main(String[] args) {
        String url="http://47.96.177.120:8545";
        web3 = Web3j.build(new HttpService(url));
        String SQL="INSERT INTO BlockData (blockNumber, blockHash, timestamp, transactionSize, gasLimit,"+
                "totalDifficulty, parentHash, stateRoot, extraData,miner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        //打印从第一个区块开始的信息
        try(Connection connection = JDBCUtilsByDruid.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(SQL)){
            BigInteger latestBlockNumber = web3.ethBlockNumber().send().getBlockNumber();
            BigInteger startBlock = latestBlockNumber.subtract(new BigInteger("10")); //从最后100个区块开始遍历

            for (BigInteger i = startBlock; i.compareTo(latestBlockNumber) <= 0; i = i.add(BigInteger.ONE)) {
                EthBlock.Block ethBlock = web3.ethGetBlockByNumber(new DefaultBlockParameterNumber(i), false).send().getBlock();
                //转换日期格式
                BigInteger timestampInSeconds = ethBlock.getTimestamp();
                long timestampInMillis = timestampInSeconds.multiply(BigInteger.valueOf(1000)).longValue();
                Date blockDate = new Date(timestampInMillis);
                int blockNumber = ethBlock.getNumber().intValue();
                String blockHash = ethBlock.getHash();
                int transactionSize = ethBlock.getTransactions().size();
                String gasLimit = ethBlock.getGasLimit().toString();
                String totalDifficulty = ethBlock.getTotalDifficulty().toString();
                String parentHash =  ethBlock.getParentHash();
                String stateRoot = ethBlock.getStateRoot();
                String extraData = ethBlock.getExtraData();
                String miner = ethBlock.getMiner();
                BlockInfo blockInfo = new BlockInfo(
                        blockNumber,
                        blockHash,
                        blockDate,
                        transactionSize,
                        gasLimit,
                        totalDifficulty,
                        parentHash,
                        stateRoot,
                        extraData,
                        miner
                );
                pstmt.setInt(1, blockNumber);
                pstmt.setString(2, blockHash);
                pstmt.setTimestamp(3,new Timestamp(blockDate.getTime()));
                pstmt.setInt(4, transactionSize);
                pstmt.setString(5, gasLimit);
                pstmt.setString(6, totalDifficulty);
                pstmt.setString(7, parentHash);
                pstmt.setString(8, stateRoot);
                pstmt.setString(9, extraData);
                pstmt.setString(10,miner);

                pstmt.executeUpdate();
                System.out.println("---------插入区块成功---------");
                System.out.println("blockInfo = " + blockInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //订阅新的区块
        web3.blockFlowable(false).subscribe(block -> {
            EthBlock.Block ethBlock = block.getBlock();
            // 获取区块时间戳（以秒为单位）
            BigInteger timestampInSeconds = ethBlock.getTimestamp();
            // 将秒转换为毫秒
            long timestampInMillis = timestampInSeconds.multiply(BigInteger.valueOf(1000)).longValue();

            // 使用毫秒值创建Date对象
            Date blockDate = new Date(timestampInMillis);
            BlockInfo blockInfo = new BlockInfo(
                    ethBlock.getNumber().intValue(),
                    ethBlock.getHash(),
                    blockDate,
                    ethBlock.getTransactions().size(),
                    ethBlock.getGasLimit().toString(),
                    ethBlock.getTotalDifficulty().toString(),
                    ethBlock.getParentHash(),
                    ethBlock.getStateRoot(),
                    ethBlock.getExtraData(),
                    ethBlock.getMiner()
            );
            System.out.println("ethBlock.getAuthor() = " + ethBlock.getAuthor());
            saveBlockInfoToDatabase(blockInfo);
            System.out.println("---------------------------");
        }, error -> {
            error.printStackTrace();
        });

        //交易过滤
        web3.transactionFlowable().subscribe(tx -> {
            System.out.println("New transaction observed:");
            System.out.println("From: " + tx.getFrom());
            System.out.println("To: " + tx.getTo());
            System.out.println("BlockNumber = " + tx.getBlockNumber());
            System.out.println("Hash: " + tx.getHash());
            System.out.println("Nonce: " + tx.getNonce());
            System.out.println("Gas price: " + tx.getGasPrice());
            System.out.println("Gas limit: " + tx.getGas());
            System.out.println("Input data: " + tx.getInput());
            System.out.println("************************************");
            saveTxInfoToDatabase(tx);
        }, error -> {
            System.err.println("Error in subscription: ");
            error.printStackTrace();
        });

        // To keep the program running to continue listening
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private static void saveBlockInfoToDatabase(BlockInfo blockInfo) {
        int blockNumber = blockInfo.getBlockNumber();
        String blockHash = blockInfo.getBlockHash();
        Date timestamp = blockInfo.getTimestamp();
        int transactionSize = blockInfo.getTransactionSize();
        String gasLimit = blockInfo.getGasLimit();
        String totalDifficulty = blockInfo.getTotalDifficulty();
        String parentHash = blockInfo.getParentHash();
        String stateRoot = blockInfo.getStateRoot();
        String extraData = blockInfo.getExtraData();
        String miner = blockInfo.getMiner();
        String sql = "INSERT INTO BlockData (blockNumber, blockHash, timestamp, transactionSize, gasLimit,"+
                "totalDifficulty, parentHash, stateRoot, extraData,miner) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection connection = JDBCUtilsByDruid.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, blockNumber);
            pstmt.setString(2, blockHash);
            pstmt.setTimestamp(3,new Timestamp(timestamp.getTime()));
            pstmt.setInt(4, transactionSize);
            pstmt.setString(5, gasLimit);
            pstmt.setString(6, totalDifficulty);
            pstmt.setString(7, parentHash);
            pstmt.setString(8, stateRoot);
            pstmt.setString(9, extraData);
            pstmt.setString(10,miner);
            pstmt.executeUpdate();
            System.out.println("---------插入区块成功---------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveTxInfoToDatabase(Transaction tx) throws SQLException {

        String txfrom = tx.getFrom();

        String txTo = tx.getTo();
        int BlockNumber = tx.getBlockNumber().intValue();
        boolean Status=true;
        String hash = tx.getHash();
        int nonce = tx.getNonce().intValue();
        int gasPrice = tx.getGasPrice().intValue();
        int gasLimit = tx.getGas().intValue();

        String inputData = tx.getInput();
        String sql="insert into transactions (`From`,`To`,BlockNumber,Status,`Hash`,Nonce,GasPrice,GasLimit,InputData) values (?,?,?,?,?,?,?,?,?)";
        try (Connection connection = JDBCUtilsByDruid.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, txfrom);
            statement.setString(2, txTo);
            statement.setInt(3, BlockNumber);
            statement.setInt(4, Status ? 1 : 0); // 将boolean转换为int
            statement.setString(5, hash);
            statement.setInt(6, nonce);
            statement.setLong(7, gasPrice); // 使用setLong为GasPrice
            statement.setInt(8, gasLimit);
            statement.setString(9, inputData);
            statement.executeUpdate();
            System.out.println("---------插入交易成功---------");
        }catch (SQLException e){
            throw e;
        }
    }
}

