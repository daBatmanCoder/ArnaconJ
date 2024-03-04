# Arnacon SDK Documentation

Welcome to the Arnacon SDK documentation. This SDK is designed to facilitate blockchain integration into your applications, providing a wide range of functionalities from network configurations to smart contract interactions and utility functions.

## Table of Contents

- [Introduction](#introduction)
- [Installation](#installation)
    - [Prerequisites](#prerequisities)
    - [Android studio](#android-studio)
    - [Maven](#maven)
- [Configuration](#configuration)
    - [AContracts](#contracts)
    - [ANetwork](#anetwork)
- [Arnacon Components](#arnacon-components)
    - [Contracts](#contracts)
    - [Cloud Functions](#cloud-functions)
    - [Config Service Provider](#configserviceprovider)
    - [Network](#network)
    - [Utilities](#Utils)
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
mvn install:install-file -Dfile=target/ArnaconSDK-1.0.jar -DgroupId=com.Arnacon -DartifactId=ArnaconSDK -Dversion=1.0 -Dpackaging=jar
```

Ensure to replace target/ArnaconSDK-1.0.jar with the actual path to your generated JAR file.

2. Include Your JAR as a Dependency in Other Projects

With your JAR installed in your local Maven repository, you can include it as a dependency in any Maven project by adding the following to the pom.xml file of those projects:
```xml
<dependency>
    <groupId>com.Arnacon</groupId>
    <artifactId>ArnaconSDK</artifactId>
    <version>1.0</version>
</dependency>
```

This configuration tells Maven to look for your *ArnaconSDK* artifact in the local repository and include it in the project's classpath.


### Verifying Installation

After completing the build process, verify the installation by running a simple test that imports and utilizes a class from the SDK. If the project builds successfully without errors, the SDK has been correctly integrated into your project.

***
***

## Config

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
***

## Arnacon Components

Detailed overview of each component within the `Arnacon` directory.

### Contracts

Extends `AContract` from the `Utils` SubPackage to provide specific contract addresses for the Arnacon ecosystem.

***

### CloudFunctions

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

***

### ConfigServiceProvider

Manages service provider configurations, allowing for customization of service provider-related functionalities.

***

### Network 
(Extends ANetwork)
This class is used to get the network configuration from a cloud functions for a wanted network name

***

### Utils

Provides utility functions such as package validation, shop opening, and payment URL generation.

```java
public static CloudFunctions CloudFunctions = new CloudFunctions();
public static Contracts Contracts = new Contracts();
```

- The cloud functions and Contracts class are under Utils


```java
isValidPackage(String packageNum, String shopData)
```
- Validates if the user input matches any of the packages listed in the provided JSON data. 
  This function is essential for verifying user selections against available options.


```java
getPaymentURL(String userID, String packageNum, String shopData)
```
- Constructs a URL for processing payments for a selected package using Stripe payment service. 
(The store is a fetched json with a number of items)

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


```java
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
  This function is essential for monitoring wallet funds.

```java
fetchStore(String serviceProviderName) 
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
          {"display_type": "boost_number", "trait_type": "Duration", "value": 45}
        ]
    }
  }
  ```

  Where:
   `InitP` is the a one-time payment for the initalizing of the product,
   `Price` is the Subscription price paid every `Duration` days,
   `Currency` to pay (iDeal is supported with EUR).

***
***  

 ## Example 


The `InitAppWeb2` class is provided as a conceptual example to illustrate how Web2 applications can interact with blockchain technologies using the Cellact SDK. This class demonstrates a series of operations from initializing network connections to executing blockchain transactions, all within a synchronous, step-by-step execution model typical in Web2 environments.

### Synchronous Process Execution

The use of `Scanner` and user input prompts in `InitAppWeb2` is deliberate:

**Process Synchronization**: The `Scanner` lines act as breakpoints, ensuring that each step is completed before proceeding to the next. This is crucial in blockchain interactions where operations depend on the successful completion of previous steps (e.g., a transaction must be mined before its effects can be observed).

### Step-by-Step Guide

1. **Initialization** ( cloudFunctions are now under Utils)

    - Initializes the Web3 service with the Mumbai test network configuration.
    - Sets up the configuration for the service provider.

        ```java
        this.Web3Service = new Web3AJ(new Network("mumbai"));
        configServiceProvider = new configServiceProvider("test2.cellact.nl");
        ```

2. **Fetch Store** 

    - Retrieves content from IPFS using the shop CID and displays the content.

        ```java
        String ipfsContent = Web3AJ.fetchStore(shopCID);
        ```

3. **Payment URL Generation**

    - After user selection (packageNum) we send the user's public key,package num and the shop JSON and get a URL to pay.


        ```java
        String url = Utils.getPaymentURL(this.Web3Service.wallet.getPublicKey(), packageNum, ipfsContent);
        ```
***
(Remark) Between those steps there should be a waiting time in order to complete the registration of the user and be able to retrieve the ENS.
***


4. **FCM Token sending**

    - Sending the fcm_token to update in the server side for future notification

        ```java
        Utils.CloudFunctions.sendFCM(fcm_token);
        ```

### Important Considerations

- **Security**: Always ensure sensitive operations, especially those involving private keys and signatures, are handled securely. Avoid exposing private keys in your application code.

- **Asynchronous Nature of Blockchain**: While this example uses a synchronous approach for simplicity - thus between steps you may want to consider to make some checking to the blockchain before continuing 

***
**** 
*****

## License

&copy; All right reserved to B.V Cellact NL
