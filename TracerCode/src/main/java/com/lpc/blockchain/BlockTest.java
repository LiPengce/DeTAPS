package com.lpc.blockchain;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-07-26
 */
public class BlockTest {
    public static void main(String[] args) throws URISyntaxException {
        final WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:8546/"));
        final boolean includeRawResponses = false;
        final WebSocketService webSocketService = new WebSocketService(webSocketClient, includeRawResponses);
        Web3j web3 = Web3j.build(webSocketService);
        try {
            Web3ClientVersion send = web3.web3ClientVersion().send();
            System.out.println("send = " + send.getWeb3ClientVersion());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
