package com.lpc.cryptography.blockchain;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.1.
 */
@SuppressWarnings("rawtypes")
public class DeTAPSContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5060008081905550610fff806100276000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063932016ba1161005b578063932016ba146100ea578063a7845ff81461011b578063d988e11c14610139578063e36f22091461016a5761007d565b80630c7bb3e614610082578063704096501461009e57806392c60ad3146100ba575b600080fd5b61009c60048036038101906100979190610874565b610186565b005b6100b860048036038101906100b391906108ff565b61020a565b005b6100d460048036038101906100cf919061095b565b610254565b6040516100e19190610ac9565b60405180910390f35b61010460048036038101906100ff919061095b565b610340565b604051610112929190610b35565b60405180910390f35b610123610404565b6040516101309190610b7b565b60405180910390f35b610153600480360381019061014e919061095b565b61040d565b604051610161929190610b96565b60405180910390f35b610184600480360381019061017f9190610bcd565b61065b565b005b60016000848152602001908152602001600020604051806040016040528084815260200183815250908060018154018082558091505060019003906000526020600020906002020160009091909190915060008201518160000190816101ec9190610e51565b5060208201518160010190816102029190610e51565b505050505050565b600260008381526020019081526020016000208190806001815401808255809150506001900390600052602060002001600090919091909150908161024f9190610e51565b505050565b606060026000838152602001908152602001600020805480602002602001604051908101604052809291908181526020016000905b828210156103355783829060005260206000200180546102a890610c74565b80601f01602080910402602001604051908101604052809291908181526020018280546102d490610c74565b80156103215780601f106102f657610100808354040283529160200191610321565b820191906000526020600020905b81548152906001019060200180831161030457829003601f168201915b505050505081526020019060010190610289565b505050509050919050565b60608060006003848154811061035957610358610f23565b5b9060005260206000209060020201600001805461037590610c74565b80601f01602080910402602001604051908101604052809291908181526020018280546103a190610c74565b80156103ee5780601f106103c3576101008083540402835291602001916103ee565b820191906000526020600020905b8154815290600101906020018083116103d157829003601f168201915b5050505050905060608181935093505050915091565b60008054905090565b6060806000600160008581526020019081526020016000208054905067ffffffffffffffff81111561044257610441610749565b5b60405190808252806020026020018201604052801561047557816020015b60608152602001906001900390816104605790505b50905060005b6001600086815260200190815260200160002080549050811015610589576001600086815260200190815260200160002081815481106104be576104bd610f23565b5b906000526020600020906002020160010180546104da90610c74565b80601f016020809104026020016040519081016040528092919081815260200182805461050690610c74565b80156105535780601f1061052857610100808354040283529160200191610553565b820191906000526020600020905b81548152906001019060200180831161053657829003601f168201915b505050505082828151811061056b5761056a610f23565b5b6020026020010181905250808061058190610f81565b91505061047b565b506000600160008681526020019081526020016000206000815481106105b2576105b1610f23565b5b906000526020600020906002020160000180546105ce90610c74565b80601f01602080910402602001604051908101604052809291908181526020018280546105fa90610c74565b80156106475780601f1061061c57610100808354040283529160200191610647565b820191906000526020600020905b81548152906001019060200180831161062a57829003601f168201915b505050505090508082935093505050915091565b6003604051806040016040528084815260200183815250908060018154018082558091505060019003906000526020600020906002020160009091909190915060008201518160000190816106b09190610e51565b5060208201518160010190816106c69190610e51565b5050506000808154809291906106db90610f81565b91905055505050565b6000604051905090565b600080fd5b600080fd5b6000819050919050565b61070b816106f8565b811461071657600080fd5b50565b60008135905061072881610702565b92915050565b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b61078182610738565b810181811067ffffffffffffffff821117156107a05761079f610749565b5b80604052505050565b60006107b36106e4565b90506107bf8282610778565b919050565b600067ffffffffffffffff8211156107df576107de610749565b5b6107e882610738565b9050602081019050919050565b82818337600083830152505050565b6000610817610812846107c4565b6107a9565b90508281526020810184848401111561083357610832610733565b5b61083e8482856107f5565b509392505050565b600082601f83011261085b5761085a61072e565b5b813561086b848260208601610804565b91505092915050565b60008060006060848603121561088d5761088c6106ee565b5b600061089b86828701610719565b935050602084013567ffffffffffffffff8111156108bc576108bb6106f3565b5b6108c886828701610846565b925050604084013567ffffffffffffffff8111156108e9576108e86106f3565b5b6108f586828701610846565b9150509250925092565b60008060408385031215610916576109156106ee565b5b600061092485828601610719565b925050602083013567ffffffffffffffff811115610945576109446106f3565b5b61095185828601610846565b9150509250929050565b600060208284031215610971576109706106ee565b5b600061097f84828501610719565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b600081519050919050565b600082825260208201905092915050565b60005b838110156109ee5780820151818401526020810190506109d3565b60008484015250505050565b6000610a05826109b4565b610a0f81856109bf565b9350610a1f8185602086016109d0565b610a2881610738565b840191505092915050565b6000610a3f83836109fa565b905092915050565b6000602082019050919050565b6000610a5f82610988565b610a698185610993565b935083602082028501610a7b856109a4565b8060005b85811015610ab75784840389528151610a988582610a33565b9450610aa383610a47565b925060208a01995050600181019050610a7f565b50829750879550505050505092915050565b60006020820190508181036000830152610ae38184610a54565b905092915050565b600082825260208201905092915050565b6000610b07826109b4565b610b118185610aeb565b9350610b218185602086016109d0565b610b2a81610738565b840191505092915050565b60006040820190508181036000830152610b4f8185610afc565b90508181036020830152610b638184610afc565b90509392505050565b610b75816106f8565b82525050565b6000602082019050610b906000830184610b6c565b92915050565b60006040820190508181036000830152610bb08185610afc565b90508181036020830152610bc48184610a54565b90509392505050565b60008060408385031215610be457610be36106ee565b5b600083013567ffffffffffffffff811115610c0257610c016106f3565b5b610c0e85828601610846565b925050602083013567ffffffffffffffff811115610c2f57610c2e6106f3565b5b610c3b85828601610846565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680610c8c57607f821691505b602082108103610c9f57610c9e610c45565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302610d077fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610cca565b610d118683610cca565b95508019841693508086168417925050509392505050565b6000819050919050565b6000610d4e610d49610d44846106f8565b610d29565b6106f8565b9050919050565b6000819050919050565b610d6883610d33565b610d7c610d7482610d55565b848454610cd7565b825550505050565b600090565b610d91610d84565b610d9c818484610d5f565b505050565b5b81811015610dc057610db5600082610d89565b600181019050610da2565b5050565b601f821115610e0557610dd681610ca5565b610ddf84610cba565b81016020851015610dee578190505b610e02610dfa85610cba565b830182610da1565b50505b505050565b600082821c905092915050565b6000610e2860001984600802610e0a565b1980831691505092915050565b6000610e418383610e17565b9150826002028217905092915050565b610e5a826109b4565b67ffffffffffffffff811115610e7357610e72610749565b5b610e7d8254610c74565b610e88828285610dc4565b600060209050601f831160018114610ebb5760008415610ea9578287015190505b610eb38582610e35565b865550610f1b565b601f198416610ec986610ca5565b60005b82811015610ef157848901518255600182019150602085019450602081019050610ecc565b86831015610f0e5784890151610f0a601f891682610e17565b8355505b6001600288020188555050505b505050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000610f8c826106f8565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203610fbe57610fbd610f52565b5b60018201905091905056fea26469706673582212201301a8ca6f195efaa607ae2554984bc5b5a4313b4f1effecab98363d83c0336764736f6c63430008130033";

    public static final String FUNC_SEND_DECSHARE = "send_decShare";

    public static final String FUNC_SENDMESSAGEANDDETAPSSIGN = "sendMessageAndDeTAPSsign";

    public static final String FUNC_SENDMESSAGEANDSHARE = "sendMessageAndshare";

    public static final String FUNC_GETALLATSSHARES = "getAllATSshares";

    public static final String FUNC_GETM_DETAPSSIGN = "getM_DeTAPSsign";

    public static final String FUNC_GETMESSAGE_DETAPSSIGNCOUNT = "getmessage_DeTAPSsigncount";

    public static final String FUNC_GETNOTARIESSHARES = "getNotariesShares";

    @Deprecated
    protected DeTAPSContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DeTAPSContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DeTAPSContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DeTAPSContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> send_decShare(BigInteger gid, String decshare) {
        final Function function = new Function(
                FUNC_SEND_DECSHARE, 
                Arrays.<Type>asList(new Uint256(gid),
                new Utf8String(decshare)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendMessageAndDeTAPSsign(String message, String DeTAPSSignature) {
        final Function function = new Function(
                FUNC_SENDMESSAGEANDDETAPSSIGN, 
                Arrays.<Type>asList(new Utf8String(message),
                new Utf8String(DeTAPSSignature)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendMessageAndshare(BigInteger gid, String _message, String _share) {
        final Function function = new Function(
                FUNC_SENDMESSAGEANDSHARE, 
                Arrays.<Type>asList(new Uint256(gid),
                new Utf8String(_message),
                new Utf8String(_share)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<String, List<String>>> getAllATSshares(BigInteger gid) {
        final Function function = new Function(FUNC_GETALLATSSHARES, 
                Arrays.<Type>asList(new Uint256(gid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<Tuple2<String, List<String>>>(function,
                new Callable<Tuple2<String, List<String>>>() {
                    @Override
                    public Tuple2<String, List<String>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, List<String>>(
                                (String) results.get(0).getValue(), 
                                convertToNative((List<Utf8String>) results.get(1).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<Tuple2<String, String>> getM_DeTAPSsign(BigInteger index) {
        final Function function = new Function(FUNC_GETM_DETAPSSIGN, 
                Arrays.<Type>asList(new Uint256(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple2<String, String>>(function,
                new Callable<Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getmessage_DeTAPSsigncount() {
        final Function function = new Function(FUNC_GETMESSAGE_DETAPSSIGNCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> getNotariesShares(BigInteger gid) {
        final Function function = new Function(FUNC_GETNOTARIESSHARES, 
                Arrays.<Type>asList(new Uint256(gid)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @Deprecated
    public static DeTAPSContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DeTAPSContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DeTAPSContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DeTAPSContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DeTAPSContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DeTAPSContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DeTAPSContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DeTAPSContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DeTAPSContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DeTAPSContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DeTAPSContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DeTAPSContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
