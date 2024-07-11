package com.ABI;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
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
 * <a href="https://github.com/web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.0.
 */
@SuppressWarnings("rawtypes")
public class ServiceProvider extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_GSM = "GSM";

    public static final String FUNC_GSMIPS = "GSMIPS";

    public static final String FUNC_INDEX_OF_METADATA = "INDEX_OF_METADATA";

    public static final String FUNC_KAMAILIOIPS = "KamailioIPS";

    public static final String FUNC_OWNER = "OWNER";

    public static final String FUNC_SERVICE_PROVIDER_DOMAIN = "SERVICE_PROVIDER_DOMAIN";

    public static final String FUNC_SERVICE_PROVIDER_NODE = "SERVICE_PROVIDER_NODE";

    public static final String FUNC_ADDADMIN = "addAdmin";

    public static final String FUNC_ADDGSM = "addGSM";

    public static final String FUNC_ADDNEWIPROUTE = "addNewIPRoute";

    public static final String FUNC_ADDPRODUCT = "addProduct";

    public static final String FUNC_ADMINS = "admins";

    public static final String FUNC_CREATESUBSCRIPTION = "createSubscription";

    public static final String FUNC_EXTENDSUBSCRIPTION = "extendSubscription";

    public static final String FUNC_GETIPSFROMNUMBER = "getIPSFromNumber";

    public static final String FUNC_GETIPSFORNUMBER = "getIPsForNumber";

    public static final String FUNC_GETPRODUCTMETADATA = "getProductMetaData";

    public static final String FUNC_GETSERVICEPROVIDERDOMAIN = "getServiceProviderDomain";

    public static final String FUNC_GETSERVICEPROVIDERMETADATA = "getServiceProviderMetadata";

    public static final String FUNC_INDEXOFIP = "indexOfIP";

    public static final String FUNC_ISUSERVALID = "isUserValid";

    public static final String FUNC_LISTGSM = "listGSM";

    public static final String FUNC_REMOVEADMIN = "removeAdmin";

    public static final String FUNC_STARTSUBSCRIPTION = "startSubscription";

    public static final String FUNC_STRINGTOUINT = "stringToUint";

    public static final String FUNC_UPDATENEWSERVICEPROVIDER = "updateNewServiceProvider";

    public static final Event ADMINCHANGED_EVENT = new Event("AdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event SHOWADDRESS_EVENT = new Event("showAddress", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event SHOWENS_EVENT = new Event("showENS", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected ServiceProvider(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ServiceProvider(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ServiceProvider(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ServiceProvider(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AdminChangedEventResponse> getAdminChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<AdminChangedEventResponse> responses = new ArrayList<AdminChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.admin = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.isAdded = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AdminChangedEventResponse getAdminChangedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ADMINCHANGED_EVENT, log);
        AdminChangedEventResponse typedResponse = new AdminChangedEventResponse();
        typedResponse.log = log;
        typedResponse.admin = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.isAdded = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAdminChangedEventFromLog(log));
    }

    public Flowable<AdminChangedEventResponse> adminChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINCHANGED_EVENT));
        return adminChangedEventFlowable(filter);
    }

    public static List<ShowAddressEventResponse> getShowAddressEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SHOWADDRESS_EVENT, transactionReceipt);
        ArrayList<ShowAddressEventResponse> responses = new ArrayList<ShowAddressEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ShowAddressEventResponse typedResponse = new ShowAddressEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.subscriptionContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ShowAddressEventResponse getShowAddressEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SHOWADDRESS_EVENT, log);
        ShowAddressEventResponse typedResponse = new ShowAddressEventResponse();
        typedResponse.log = log;
        typedResponse.subscriptionContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ShowAddressEventResponse> showAddressEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getShowAddressEventFromLog(log));
    }

    public Flowable<ShowAddressEventResponse> showAddressEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SHOWADDRESS_EVENT));
        return showAddressEventFlowable(filter);
    }

    public static List<ShowENSEventResponse> getShowENSEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SHOWENS_EVENT, transactionReceipt);
        ArrayList<ShowENSEventResponse> responses = new ArrayList<ShowENSEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ShowENSEventResponse typedResponse = new ShowENSEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ens = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ShowENSEventResponse getShowENSEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SHOWENS_EVENT, log);
        ShowENSEventResponse typedResponse = new ShowENSEventResponse();
        typedResponse.log = log;
        typedResponse.ens = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ShowENSEventResponse> showENSEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getShowENSEventFromLog(log));
    }

    public Flowable<ShowENSEventResponse> showENSEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SHOWENS_EVENT));
        return showENSEventFlowable(filter);
    }

    public RemoteFunctionCall<String> GSM(String param0) {
        final Function function = new Function(FUNC_GSM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> GSMIPS(String param0) {
        final Function function = new Function(FUNC_GSMIPS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> INDEX_OF_METADATA() {
        final Function function = new Function(FUNC_INDEX_OF_METADATA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> KamailioIPS(BigInteger param0) {
        final Function function = new Function(FUNC_KAMAILIOIPS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> OWNER() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> SERVICE_PROVIDER_DOMAIN() {
        final Function function = new Function(FUNC_SERVICE_PROVIDER_DOMAIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> SERVICE_PROVIDER_NODE() {
        final Function function = new Function(FUNC_SERVICE_PROVIDER_NODE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> addAdmin(String _admin) {
        final Function function = new Function(
                FUNC_ADDADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _admin)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addGSM(String gsm_number, String gsm_metadata,
            BigInteger IPSForNumber) {
        final Function function = new Function(
                FUNC_ADDGSM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(gsm_number), 
                new org.web3j.abi.datatypes.Utf8String(gsm_metadata), 
                new org.web3j.abi.datatypes.generated.Uint256(IPSForNumber)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addNewIPRoute(String ip) {
        final Function function = new Function(
                FUNC_ADDNEWIPROUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ip)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addProduct(BigInteger _setupFee,
            BigInteger _monthlyFee, String _metaData, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_ADDPRODUCT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_setupFee), 
                new org.web3j.abi.datatypes.generated.Uint256(_monthlyFee), 
                new org.web3j.abi.datatypes.Utf8String(_metaData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<Boolean> admins(String param0) {
        final Function function = new Function(FUNC_ADMINS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createSubscription(BigInteger _commitmentDeposit,
            BigInteger _productID) {
        final Function function = new Function(
                FUNC_CREATESUBSCRIPTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_commitmentDeposit), 
                new org.web3j.abi.datatypes.generated.Uint256(_productID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> extendSubscription(List<BigInteger> _proof_a,
            List<List<BigInteger>> _proof_b, List<BigInteger> _proof_c, BigInteger _nullifierHash,
            BigInteger _root, BigInteger _productID) {
        @SuppressWarnings("unchecked")
        final Function function = new Function(
                FUNC_EXTENDSUBSCRIPTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray2<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_proof_a, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.StaticArray2<org.web3j.abi.datatypes.generated.StaticArray2>(
                        org.web3j.abi.datatypes.generated.StaticArray2.class,
                        org.web3j.abi.Utils.typeMap(_proof_b, org.web3j.abi.datatypes.generated.StaticArray2.class,
                org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.StaticArray2<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_proof_c, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(_nullifierHash), 
                new org.web3j.abi.datatypes.generated.Uint256(_root), 
                new org.web3j.abi.datatypes.generated.Uint256(_productID)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getIPSFromNumber(String gsm_number) {
        final Function function = new Function(FUNC_GETIPSFROMNUMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(gsm_number)), 
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

    public RemoteFunctionCall<List> getIPsForNumber(BigInteger number) {
        final Function function = new Function(FUNC_GETIPSFORNUMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(number)), 
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

    public RemoteFunctionCall<String> getProductMetaData(BigInteger _productID) {
        final Function function = new Function(FUNC_GETPRODUCTMETADATA, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_productID)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getServiceProviderDomain() {
        final Function function = new Function(FUNC_GETSERVICEPROVIDERDOMAIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getServiceProviderMetadata() {
        final Function function = new Function(FUNC_GETSERVICEPROVIDERMETADATA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> indexOfIP() {
        final Function function = new Function(FUNC_INDEXOFIP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> isUserValid(String ens) {
        final Function function = new Function(FUNC_ISUSERVALID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(ens)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> listGSM(String gsm_number, String user_address) {
        final Function function = new Function(
                FUNC_LISTGSM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(gsm_number), 
                new org.web3j.abi.datatypes.Address(160, user_address)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAdmin(String _admin) {
        final Function function = new Function(
                FUNC_REMOVEADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _admin)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> startSubscription(List<BigInteger> _proof_a,
            List<List<BigInteger>> _proof_b, List<BigInteger> _proof_c, BigInteger _nullifierHash,
            BigInteger _root, String ens) {
        @SuppressWarnings("unchecked")
        final Function function = new Function(
                FUNC_STARTSUBSCRIPTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray2<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_proof_a, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.StaticArray2<org.web3j.abi.datatypes.generated.StaticArray2>(
                        org.web3j.abi.datatypes.generated.StaticArray2.class,
                        org.web3j.abi.Utils.typeMap(_proof_b, org.web3j.abi.datatypes.generated.StaticArray2.class,
                org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.StaticArray2<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_proof_c, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(_nullifierHash), 
                new org.web3j.abi.datatypes.generated.Uint256(_root), 
                new org.web3j.abi.datatypes.Utf8String(ens)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> stringToUint(String str) {
        final Function function = new Function(FUNC_STRINGTOUINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(str)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updateNewServiceProvider(byte[] _signature,
            String _messageSigned, byte[] _ENSNode) {
        final Function function = new Function(
                FUNC_UPDATENEWSERVICEPROVIDER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(_signature), 
                new org.web3j.abi.datatypes.Utf8String(_messageSigned), 
                new org.web3j.abi.datatypes.generated.Bytes32(_ENSNode)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ServiceProvider load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new ServiceProvider(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ServiceProvider load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ServiceProvider(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ServiceProvider load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new ServiceProvider(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ServiceProvider load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ServiceProvider(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class AdminChangedEventResponse extends BaseEventResponse {
        public String admin;

        public Boolean isAdded;
    }

    public static class ShowAddressEventResponse extends BaseEventResponse {
        public String subscriptionContract;
    }

    public static class ShowENSEventResponse extends BaseEventResponse {
        public String ens;
    }
}
