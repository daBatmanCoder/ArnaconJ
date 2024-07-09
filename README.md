# Arnacon SDK Documentation

Welcome to the Arnacon SDK documentation. This SDK is designed to facilitate blockchain integration into your applications, providing a wide range of functionalities from network configurations to smart contract interactions and utility functions.

## Table of Contents

- [Introduction](#introduction)
- [Installation](#installation)
    - [Prerequisites](#prerequisities)
    - [Android studio](#android-studio)
    - [Maven](#maven)
- [Web3 Service Base](#web3servicebase)
    - [AWeb3AJ](#aweb3aj)
    - [AContracts](#contracts)
    - [ANetwork](#anetwork)
    - [ADataSaveHelper](#adatasavehelper)
    - [ALogger](#alogger)

- [Arnacon Components](#arnacon-components)
    - [Contracts](#contracts)
    - [Cloud Functions](#cloud-functions)
    - [Config Service Provider](#configserviceprovider)
    - [Network](#network)
    - [Utilities](#Utils)
    - [DataSaverHelper](#data-saver-helper)
    - [Wallet](#wallet)
    - [Web3AJ](#web3aj)
- [Example](#example)
- [License](#license)

***
***
***
***

## Introduction

The Cellact SDK offers a robust set of tools for developers looking to incorporate blockchain features into their applications. This includes managing network configurations, interacting with smart contracts, and performing a variety of blockchain-related tasks.

***
***


## Installation

The Arnacon SDK is designed to be easily integrated into your Java projects using Maven Or Gradl. Follow the steps below to include the SDK in your project.


### Prerequisites

Ensure you have Maven installed and configured on your system. The SDK requires Java 11 or higher due to its dependencies and language features.

***

#### Android studio

- Place the JAR file in the project folder `libs`.

Open your build.gradle file located in your app module directory.
Add the include JAR Package and web3j dependency within the dependencies block as shown above.
Sync your Gradle project to ensure the dependencies are downloaded and included in your project build path.

```gradle
implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
implementation("org.web3j:core:4.8.7-android")
```

This version of web3j is optimized for Android development, providing compatibility with Android's networking and concurrency frameworks.

***

### Maven

1. Install the JAR into Your Local Maven Repository

To use this JAR in other projects on your local machine, you'll need to install it into your local Maven repository.
If you've already built the JAR with Maven, it should be correctly formatted. 
To manually install it, use the following command:
```bash
mvn install:install-file -Dfile=target/ArnaconSDK-1.0.12.2.jar -DgroupId=com.Arnacon -DartifactId=ArnaconSDK -Dversion=1.0.12.2 -Dpackaging=jar
```

Ensure to replace target/ArnaconSDK-1.0.12.2.jar with the actual path to your generated JAR file.

2. Include Your JAR as a Dependency in Other Projects

With your JAR installed in your local Maven repository, you can include it as a dependency in any Maven project by adding the following to the pom.xml file of those projects:
```xml
<dependency>
    <groupId>com.Arnacon</groupId>
    <artifactId>ArnaconSDK</artifactId>
    <version>1.0.12.2</version>
</dependency>
```

This configuration tells Maven to look for your *ArnaconSDK* artifact in the local repository and include it in the project's classpath.


### Verifying Installation

After completing the build process, verify the installation by running a simple test that imports and utilizes a class from the SDK. If the project builds successfully without errors, the SDK has been correctly integrated into your project.

***
***

## Web3ServiceBase

The `Web3ServiceBase` abstraction defines the essential methods and functionalities required to interact with web3 services. It serves as a blueprint for implementing concrete web3 service classes.

### AWeb3AJ

The `AWeb3AJ` abstraction defines the methods and functionalities required to interact with web3 services in a structured and flexible manner. It serves as a blueprint for implementing concrete web3 service classes, ensuring consistency and ease of use.

- **ADataSaveHelper dataSaveHelper**: An instance of `ADataSaveHelper` used for saving and retrieving data.
- **ALogger logger**: An instance of `ALogger` used for logging.

#### Methods

- **getServiceProviderList()**: Retrieves a list of available service providers.
- **setServiceProvider(String provider)**: Sets the current service provider.
- **fetchStore()**: Fetches data from the store.
- **getPaymentURL(String packageNum, String successURL, String cancelURL)**: Generates a payment URL for the specified package.
- **sendFCM(String fcmToken)**: Sends an FCM notification.
- **getXData()**: Retrieves XData.
- **getXSign(String XData)**: Generates a signature for the given XData.
- **saveENSItem(String item)**: Saves and update ayala in the app a new product

***

### AContracts

The `AContracts` abstraction in the `Config` folder allows for easy management of contract addresses and related functionalities.

- **NAME_HASH_ADDRESS**: Address for the NameHash contract.
- **W_ENS_ADDRESS**: Address for the Wrapped ENS contract - (.web3) 

***

### ANetwork

The `ANetwork` abstraction facilitates the configuration of different blockchain networks.

- **ENTRY_POINT_URL**: URL for the network's entry point.
- **CHAIN_ID**: Identifier for the blockchain network.

***

### ALogger

The `ALogger` abstraction defines the logging interface used throughout the application. This allows for flexibility in how logging is handled, whether to console, file, or external services.
***

### ADataSaveHelper

The `ADataSaveHelper` abstraction provides an interface for saving and retrieving application data. Implementations of this interface can vary, allowing for different storage mechanisms.

!!! In order to use this SDK you'll have to implement this class !!!

- setPrefeneces(key,value)
- getPrefeneces(key,defaultValue)

***
***

## Arnacon Components

Detailed overview of each component within the `Arnacon` directory.

### Contracts

Extends `AContract` from the `Utils` SubPackage to provide specific contract addresses for the Arnacon ecosystem.

***

### Cloud Functions

Includes methods to interact with cloud functions for tasks such as retrieving user ENS or service provider details.



```java
String getUserENS(String userAddress)
```
- Sends a request to a cloud function to retrieve the ENS name associated with the given user address.

```java
String getShopCID(String serviceProvider)
```
- Fetches the CID of the shop associated with the given service provider. This CID can be used to access the shop's content on IPFS.

```java
JSONObject getNetwork(String InetworkName)
```
- Fetches the details - RPC and chain ID for the input network.

```java
String getContractAddress(String contractName)
```
- Returns the contract address from a contract name.

```java
void sendFCM(String fcm_token)
```
- Sends the server the FCM token to authenticate and update the user's FCM_token for notification

```java
void registerAyala(String data, String signedData, String ens)
```
- Sends the server the data in order to register in ayala to be identited as one of the service provider clients

***

### Config Service Provider

Manages service provider configurations, allowing for customization of service provider-related functionalities.

***

### Network 
(Extends ANetwork)
This class is used to get the network configuration from a cloud functions for a wanted network name

***

### Utils

Provides utility functions such as package validation, shop opening, and payment URL generation.

```java
CloudFunctions CloudFunctions = new CloudFunctions();
Contracts Contracts = new Contracts();
```

- The cloud functions and Contracts class are under Utils


```java
static boolean isValidPackage(String packageNum, String shopData)
```
- Validates if the user input matches any of the packages listed in the provided JSON data. 
  This function is essential for verifying user selections against available options.


```java
static String getPaymentURL(String userID, String packageNum, String jsonStore)
```
- Constructs a URL for processing payments for a selected package using Stripe payment service. 
(The store is a fetched json with a number of items)

***
### Data Saver Helper

Interface to manage the data storing mechanism. (Set,Get)

```java
void setPreference(String key, String value)
```

```java
String getPreference(String key, String defaultValue)
```


***

### Wallet

Manages wallet functionalities including key generation, import, and retrieval.

```java
generateMnemonic()
```
- Generates a new mnemonic phrase following the BIP-39 standard.
  This mnemonic can be used to regenerate a wallet's private keys.


```java
getPrivateKeyFromMnemonic(String mnemonic)
```
- Derives the private key from the given mnemonic phrase, allowing for wallet recovery and transaction signing.


```java
getPublicKey()
```
- Returns the public key derived from the wallet's private key. 
  The public key is used to derive the wallet's address and can be safely shared.


```java
getCredentials()
```
- Retrieves the wallet's credentials, including its private key, which is crucial for signing transactions and interacting with smart contracts.

***

### Web3AJ

Core component for interacting with the blockchain, including contract interactions, signing messages, and handling transactions.


```java
signMessage(String Message)
```
- Takes a string message, signs it with the private key of the current wallet, and returns the signature.
  This is useful for proving ownership of a wallet address without revealing the private key.

??? Future ???
<!-- ```java
mintNFT(String _userENS)
```
- Encodes a function call to mint an NFT with the provided ENS (Ethereum Name Service) name. 
  It constructs and sends a transaction to mint the NFT, then returns the transaction hash.

```java
buyENS(String _userENS)
```
- Similar to mintNFT, but specifically for purchasing an ENS name. 
  It sends a transaction to the ENS contract to register the name under the user's wallet address.

```java
checkBalance(String publicKey)
```
- Returns the balance of the wallet associated with the given public key. 
  This function is essential for monitoring wallet funds. -->

```java
public String fetchStore() 
```
- Fetches for a given service provider the CID of his store and then 
  fetches content from IPFS using the received CID (Content Identifier).
  This function is useful for retrieving decentralized content.

  Store example will be a JSON format file:
  ```json
  {
    "1": {
        "description": "Landline",
        "image": "https://imgur.com/e0JPFxK.png",
        "name": "Landline",
        "attributes": [
          {"trait_type": "InitP", "value": "0.99"},
          {"trait_type": "Price", "value": "9.99"},
          {"trait_type": "Currency", "value": "EUR"},
          {"display_type": "boost_number", "trait_type": "Duration", "value": 30}
        ]
    },
    "2": {
        "description": "Subline",
        "image": "https://imgur.com/e0JPFxK.png",
        "name": "Subline",
        "attributes": [
          {"trait_type": "InitP", "value": "2.99"},
          {"trait_type": "Price", "value": "19.99"},
          {"trait_type": "Currency", "value": "EUR"},
          {"display_type": "boost_number", "trait_type": "Duration", "value": 46}
        ]
    }
  }
  ```

  Where:
   `InitP` is the a one-time payment for the initalizing of the product
   `Price` is the Subscription price paid every `Duration` days
   `Currency` to pay (iDeal is supported with EUR)

```java
public void sendFCM(String fcm_token) 
```
- Sending the FCM token to update in the server side for future notification


```java
public String getPaymentURL(String packageNum) 
```
- Receiving user's choice and then sends it to the Utils- getPaymentURL to retrieve the URL

```java
public String getXData() 
```
- Generates a unique data string in order to sign on it with the user's private key and send to the SIP Server

```java
public String getXSign(String data) 
```
- Takes the data object and signs on it (the same signMessage does)

```java
public String getCalleeDomain(String callee)
```
- Retrieved the relevant callee domain for the sip invite 

```java
public String updateNewProduct(String password, String ciphertextHex) 
```
- Gets the encrypted String and password of encryption, decypher it, recgonizes which product is it and then update accordingly, aswell the ensList to be loaded in the app

```java
public String saveENSItem(String item) 
```
- saves a new item to the system- ENS/GSM, and updates ayala, if the item is not listed for the user's application it will fail

***
***  

 ## Example 


The `InitAppWeb2` md file is provided as a conceptual example to illustrate how Web2 applications can interact with blockchain technologies using the Cellact SDK. This class demonstrates a series of operations from initializing network connections to executing blockchain transactions, all within a synchronous, step-by-step execution model typical in Web2 environments.

### Step-by-Step Guide

1. **Initialization**
    - Initalizes a new dataSaverHelper - interface you need to implement in order to save necessary parameters.
        The SharedPreferencesHelper (Saver to the file system) is just one way to implement the Data Saver protocol.
    - Initalizes a logger object (need to implement the ALogger interface)
    - Initializes the Web3 service with a desired network- or default("mumbai" for now) 
    - Sets up the configuration for the service provider.
    

        ```java
        ADataSaveHelper dataSaveHelper = new SharedPreferencesHelper();
        ALogger logger = new Logger();

        Web3Service = new Web3AJ(dataSaveHelper, logger);
        // all the service providers that are available from the GCP
        String[] serviceProviders = Web3Service.getServiceProviderList();
        Web3Service.setServiceProvider(serviceProviders[0]);
        ```



2. **Fetch Store**  # Can be bypassed (?)

    - Retrieves content from IPFS using the shop CID and displays the content.

        ```java
        String ipfsContent = Web3Service.fetchStore();
        ```

3. **Payment URL Generation** # Can be bypassed (?)

    - After user selection (packageNum) we send the selection to get the payment URL - `DEEPLINK`


        ```java
        String successURL = "<YOUR_SUCCESS_DEEPLINK>";
        String cancelURL = "<YOUR_FAILURE_DEEPLINK>";

        // Payment URL
        String url = Web3Service.getPaymentURL(
            packageNum,
            successURL,
            cancelURL
        );
        ```
***
(Remark) Between those steps there should be a waiting time in order to complete the registration of the user and be able to retrieve the ENS.
***

**** 

**OR** 

2. **Get From remote method**
  
  We can bypass stages 2-6 if you're loading a product bought in a remote store such that, that product needs to follow the instructions that will be in the future listed here,
  the necessary parameters are-
  1. encrypted AES string
  2. password of that encrypted string.

  in the string needs to be 1. item type- (supported gsm/ens) 2. private_key of that ens_gsm.


3. We can continue with 4 as normal- 2 here is bypassing 2+3 from the previous version

**** 

4. **FCM Token sending** # Can be bypassed (?)

    - Sending the fcm_token to update in the server side for future notification

        ```java
        Web3Service.sendFCM(fcm_token); // Old version to be listed in Ayala

        OR

        Web3Sservice.sendFCM(fcm_token,"NOAyala"); // If you don't want to be register in the ayala --> new methods
        ```


4.  **SIP Register method** /

    - In order to register to a sip server(kamailio), we'll need to generate a X-Data- `uuid:timestamp` where `uuid` - is a unique universal id and `timestamp` is the current timestamp in miliseconds- meaning this concat is unique and will be used only once every user register, then we send it to the getXSign with the data just generated, and then we can make the request to the sip server using those 2 as headers- `X-Data` and `X-Sign`

        ```java
        String xdata = Web3Service.getXData();
        String xsign = Web3Service.getXSign(xdata);
        ```
  
### Important Considerations

- **Security**: Always ensure sensitive operations, especially those involving private keys and signatures, are handled securely. Avoid exposing private keys in your application code.

- **Asynchronous Nature of Blockchain**: While this example uses a synchronous approach for simplicity - thus between steps you may want to consider to make some checking to the blockchain before continuing 

***
**** 
*****

## License

&copy; All right reserved to B.V Cellact NL
