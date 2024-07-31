package com.lpc.blockchain;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.4.
 */
@SuppressWarnings("rawtypes")
public class DeTAPSContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50611093806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063144200681461005c578063182239781461007857806397050a19146100a8578063a1beb14b146100c4578063d2246b77146100e0575b600080fd5b610076600480360381019061007191906107de565b610110565b005b610092600480360381019061008d9190610869565b6101f9565b60405161009f9190610a06565b60405180910390f35b6100c260048036038101906100bd9190610a28565b610375565b005b6100de60048036038101906100d99190610aa0565b610426565b005b6100fa60048036038101906100f59190610b0f565b6104e6565b6040516101079190610a06565b60405180910390f35b600080848152602001908152602001600020826040516101309190610b8b565b9081526020016040518091039020819080600181540180825580915050600190039060005260206000200160009091909190915090816101709190610dae565b508160405161017f9190610b8b565b6040518091039020837fa613cd8eaf48282752729dc124fb42dd346bb737f51d67ff6c15eb05612d7cb98585600080898152602001908152602001600020876040516101cb9190610b8b565b9081526020016040518091039020805490506040516101ec93929190610ed9565b60405180910390a3505050565b606060008060008581526020019081526020016000208360405161021d9190610b8b565b9081526020016040518091039020805490501161026f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161026690610f63565b60405180910390fd5b6000808481526020019081526020016000208260405161028f9190610b8b565b9081526020016040518091039020805480602002602001604051908101604052809291908181526020016000905b828210156103695783829060005260206000200180546102dc90610bd1565b80601f016020809104026020016040519081016040528092919081815260200182805461030890610bd1565b80156103555780601f1061032a57610100808354040283529160200191610355565b820191906000526020600020905b81548152906001019060200180831161033857829003601f168201915b5050505050815260200190600101906102bd565b50505050905092915050565b60006040518060400160405280848152602001838152509050600281908060018154018082558091505060019003906000526020600020906002020160009091909190915060008201518160000190816103cf9190610dae565b5060208201518160010190816103e59190610dae565b5050507f8ad03d5afe57062c1abc4e5afa11c91dbe7e8e2066383cee4474650265b2fa228383604051610419929190610f83565b60405180910390a1505050565b6001600084815260200190815260200160002060008381526020019081526020016000208190806001815401808255809150506001900390600052602060002001600090919091909150908161047c9190610dae565b5081837f1276a0c85daeec5a89205d3b1ce4c4af99d455b2742001cee97914155deb5bc88585600160008981526020019081526020016000206000888152602001908152602001600020805490506040516104d993929190610fba565b60405180910390a3505050565b606060006001600085815260200190815260200160002060008481526020019081526020016000208054905011610552576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105499061103d565b60405180910390fd5b600160008481526020019081526020016000206000838152602001908152602001600020805480602002602001604051908101604052809291908181526020016000905b828210156106425783829060005260206000200180546105b590610bd1565b80601f01602080910402602001604051908101604052809291908181526020018280546105e190610bd1565b801561062e5780601f106106035761010080835404028352916020019161062e565b820191906000526020600020905b81548152906001019060200180831161061157829003601f168201915b505050505081526020019060010190610596565b50505050905092915050565b6000604051905090565b600080fd5b600080fd5b6000819050919050565b61067581610662565b811461068057600080fd5b50565b6000813590506106928161066c565b92915050565b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6106eb826106a2565b810181811067ffffffffffffffff8211171561070a576107096106b3565b5b80604052505050565b600061071d61064e565b905061072982826106e2565b919050565b600067ffffffffffffffff821115610749576107486106b3565b5b610752826106a2565b9050602081019050919050565b82818337600083830152505050565b600061078161077c8461072e565b610713565b90508281526020810184848401111561079d5761079c61069d565b5b6107a884828561075f565b509392505050565b600082601f8301126107c5576107c4610698565b5b81356107d584826020860161076e565b91505092915050565b6000806000606084860312156107f7576107f6610658565b5b600061080586828701610683565b935050602084013567ffffffffffffffff8111156108265761082561065d565b5b610832868287016107b0565b925050604084013567ffffffffffffffff8111156108535761085261065d565b5b61085f868287016107b0565b9150509250925092565b600080604083850312156108805761087f610658565b5b600061088e85828601610683565b925050602083013567ffffffffffffffff8111156108af576108ae61065d565b5b6108bb858286016107b0565b9150509250929050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b600081519050919050565b600082825260208201905092915050565b60005b8381101561092b578082015181840152602081019050610910565b60008484015250505050565b6000610942826108f1565b61094c81856108fc565b935061095c81856020860161090d565b610965816106a2565b840191505092915050565b600061097c8383610937565b905092915050565b6000602082019050919050565b600061099c826108c5565b6109a681856108d0565b9350836020820285016109b8856108e1565b8060005b858110156109f457848403895281516109d58582610970565b94506109e083610984565b925060208a019950506001810190506109bc565b50829750879550505050505092915050565b60006020820190508181036000830152610a208184610991565b905092915050565b60008060408385031215610a3f57610a3e610658565b5b600083013567ffffffffffffffff811115610a5d57610a5c61065d565b5b610a69858286016107b0565b925050602083013567ffffffffffffffff811115610a8a57610a8961065d565b5b610a96858286016107b0565b9150509250929050565b600080600060608486031215610ab957610ab8610658565b5b6000610ac786828701610683565b9350506020610ad886828701610683565b925050604084013567ffffffffffffffff811115610af957610af861065d565b5b610b05868287016107b0565b9150509250925092565b60008060408385031215610b2657610b25610658565b5b6000610b3485828601610683565b9250506020610b4585828601610683565b9150509250929050565b600081905092915050565b6000610b65826108f1565b610b6f8185610b4f565b9350610b7f81856020860161090d565b80840191505092915050565b6000610b978284610b5a565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680610be957607f821691505b602082108103610bfc57610bfb610ba2565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302610c647fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610c27565b610c6e8683610c27565b95508019841693508086168417925050509392505050565b6000819050919050565b6000610cab610ca6610ca184610662565b610c86565b610662565b9050919050565b6000819050919050565b610cc583610c90565b610cd9610cd182610cb2565b848454610c34565b825550505050565b600090565b610cee610ce1565b610cf9818484610cbc565b505050565b5b81811015610d1d57610d12600082610ce6565b600181019050610cff565b5050565b601f821115610d6257610d3381610c02565b610d3c84610c17565b81016020851015610d4b578190505b610d5f610d5785610c17565b830182610cfe565b50505b505050565b600082821c905092915050565b6000610d8560001984600802610d67565b1980831691505092915050565b6000610d9e8383610d74565b9150826002028217905092915050565b610db7826108f1565b67ffffffffffffffff811115610dd057610dcf6106b3565b5b610dda8254610bd1565b610de5828285610d21565b600060209050601f831160018114610e185760008415610e06578287015190505b610e108582610d92565b865550610e78565b601f198416610e2686610c02565b60005b82811015610e4e57848901518255600182019150602085019450602081019050610e29565b86831015610e6b5784890151610e67601f891682610d74565b8355505b6001600288020188555050505b505050505050565b610e8981610662565b82525050565b600082825260208201905092915050565b6000610eab826108f1565b610eb58185610e8f565b9350610ec581856020860161090d565b610ece816106a2565b840191505092915050565b6000606082019050610eee6000830186610e80565b8181036020830152610f008185610ea0565b9050610f0f6040830184610e80565b949350505050565b7f6e6f207369676e20736861726573000000000000000000000000000000000000600082015250565b6000610f4d600e83610e8f565b9150610f5882610f17565b602082019050919050565b60006020820190508181036000830152610f7c81610f40565b9050919050565b60006040820190508181036000830152610f9d8185610ea0565b90508181036020830152610fb18184610ea0565b90509392505050565b6000606082019050610fcf6000830186610e80565b610fdc6020830185610e80565b610fe96040830184610e80565b949350505050565b7f6e6f206e6f746172792073686172657300000000000000000000000000000000600082015250565b6000611027601083610e8f565b915061103282610ff1565b602082019050919050565b600060208201905081810360008301526110568161101a565b905091905056fea2646970667358221220d5de6e0d9d5d48214ee7997ac556a3a95a943bb9d5dea379473aa5d1a76f0dad64736f6c63430008130033";

    public static final String FUNC_GETNOTARYENCSHARES = "getNotaryEncShares";

    public static final String FUNC_GETSIGNSHARES = "getSignShares";

    public static final String FUNC_SUBMITENCSHARE = "submitEncshare";

    public static final String FUNC_SUBMITM_DETAPSSIGN = "submitM_DeTAPSsign";

    public static final String FUNC_SUBMITSIGNSHARE = "submitSignShare";

    public static final Event NOTARYSHARESUBMITTED_EVENT = new Event("NotaryShareSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SIGNSHARESUBMITTED_EVENT = new Event("SignShareSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event M_DETAPSSIGNSUBMITTED_EVENT = new Event("m_DeTAPSsignSubmitted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

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

    public List<NotaryShareSubmittedEventResponse> getNotaryShareSubmittedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NOTARYSHARESUBMITTED_EVENT, transactionReceipt);
        ArrayList<NotaryShareSubmittedEventResponse> responses = new ArrayList<NotaryShareSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NotaryShareSubmittedEventResponse typedResponse = new NotaryShareSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tracerID = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.DeTAPSsignId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tracerId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.DeTAPSsignID = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.notaryShareCount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NotaryShareSubmittedEventResponse> notaryShareSubmittedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NotaryShareSubmittedEventResponse>() {
            @Override
            public NotaryShareSubmittedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NOTARYSHARESUBMITTED_EVENT, log);
                NotaryShareSubmittedEventResponse typedResponse = new NotaryShareSubmittedEventResponse();
                typedResponse.log = log;
                typedResponse.tracerID = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.DeTAPSsignId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tracerId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.DeTAPSsignID = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.notaryShareCount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NotaryShareSubmittedEventResponse> notaryShareSubmittedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOTARYSHARESUBMITTED_EVENT));
        return notaryShareSubmittedEventFlowable(filter);
    }

    public List<SignShareSubmittedEventResponse> getSignShareSubmittedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SIGNSHARESUBMITTED_EVENT, transactionReceipt);
        ArrayList<SignShareSubmittedEventResponse> responses = new ArrayList<SignShareSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SignShareSubmittedEventResponse typedResponse = new SignShareSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.combinerID = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.GID = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.combinerId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.gid = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.shareCount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SignShareSubmittedEventResponse> signShareSubmittedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SignShareSubmittedEventResponse>() {
            @Override
            public SignShareSubmittedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SIGNSHARESUBMITTED_EVENT, log);
                SignShareSubmittedEventResponse typedResponse = new SignShareSubmittedEventResponse();
                typedResponse.log = log;
                typedResponse.combinerID = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.GID = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.combinerId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.gid = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.shareCount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SignShareSubmittedEventResponse> signShareSubmittedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SIGNSHARESUBMITTED_EVENT));
        return signShareSubmittedEventFlowable(filter);
    }

    public List<M_DeTAPSsignSubmittedEventResponse> getM_DeTAPSsignSubmittedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(M_DETAPSSIGNSUBMITTED_EVENT, transactionReceipt);
        ArrayList<M_DeTAPSsignSubmittedEventResponse> responses = new ArrayList<M_DeTAPSsignSubmittedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            M_DeTAPSsignSubmittedEventResponse typedResponse = new M_DeTAPSsignSubmittedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.DeTAPSsign = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<M_DeTAPSsignSubmittedEventResponse> m_DeTAPSsignSubmittedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, M_DeTAPSsignSubmittedEventResponse>() {
            @Override
            public M_DeTAPSsignSubmittedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(M_DETAPSSIGNSUBMITTED_EVENT, log);
                M_DeTAPSsignSubmittedEventResponse typedResponse = new M_DeTAPSsignSubmittedEventResponse();
                typedResponse.log = log;
                typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.DeTAPSsign = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<M_DeTAPSsignSubmittedEventResponse> m_DeTAPSsignSubmittedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(M_DETAPSSIGNSUBMITTED_EVENT));
        return m_DeTAPSsignSubmittedEventFlowable(filter);
    }

    public RemoteFunctionCall<List> getNotaryEncShares(BigInteger tracerId, BigInteger DeTAPSsignId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNOTARYENCSHARES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tracerId), 
                new org.web3j.abi.datatypes.generated.Uint256(DeTAPSsignId)), 
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

    public RemoteFunctionCall<List> getSignShares(BigInteger combinerId, String gid) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSIGNSHARES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(combinerId), 
                new org.web3j.abi.datatypes.Utf8String(gid)), 
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

    public RemoteFunctionCall<TransactionReceipt> submitEncshare(BigInteger tracerId, BigInteger DeTAPSsignId, String encshare) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SUBMITENCSHARE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tracerId), 
                new org.web3j.abi.datatypes.generated.Uint256(DeTAPSsignId), 
                new org.web3j.abi.datatypes.Utf8String(encshare)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> submitM_DeTAPSsign(String _message, String _DeTAPSsign) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SUBMITM_DETAPSSIGN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_message), 
                new org.web3j.abi.datatypes.Utf8String(_DeTAPSsign)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> submitSignShare(BigInteger combinerId, String gid, String share) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SUBMITSIGNSHARE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(combinerId), 
                new org.web3j.abi.datatypes.Utf8String(gid), 
                new org.web3j.abi.datatypes.Utf8String(share)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    @Deprecated
    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DeTAPSContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DeTAPSContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DeTAPSContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DeTAPSContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NotaryShareSubmittedEventResponse extends BaseEventResponse {
        public BigInteger tracerID;

        public BigInteger DeTAPSsignId;

        public BigInteger tracerId;

        public BigInteger DeTAPSsignID;

        public BigInteger notaryShareCount;
    }

    public static class SignShareSubmittedEventResponse extends BaseEventResponse {
        public BigInteger combinerID;

        public byte[] GID;

        public BigInteger combinerId;

        public String gid;

        public BigInteger shareCount;
    }

    public static class M_DeTAPSsignSubmittedEventResponse extends BaseEventResponse {
        public String message;

        public String DeTAPSsign;
    }
}
