# Arnacon SDK Documentation

Welcome to the Arnacon SDK documentation. This SDK is designed to facilitate blockchain integration into your applications, providing a wide range of functionalities from network configurations to smart contract interactions and utility functions.

## Table of Contents

- [Introduction](#introduction)
- [Installation](#installation)
    - [Prerequisites](#prerequisities)
- [Configuration](#configuration)
    - [Contracts](#contracts)
    - [Network](#network)
- [Arnacon Components](#arnacon-components)
    - [Contracts](#contracts)
    - [Cloud Functions](#cloud-functions)
    - [Config Service Provider](#config-service-provider)
    - [Networks](#networks)
        - [Ethereum Network](#ethereum)
        - [Mumbai Network](#mumbai)
    - [Initialization](#initialization)
        - [InitAppWeb2](#initappweb2)
        - [InitAppWeb3](#initappweb3)
    - [Utilities](#Utils)
    - [Wallet](#wallet)
    - [Web3AJ](#web3aj)
- [License](#license)

## Introduction

The Cellact SDK offers a robust set of tools for developers looking to incorporate blockchain features into their applications. This includes managing network configurations, interacting with smart contracts, and performing a variety of blockchain-related tasks.

## Installation

The Arnacon SDK is designed to be easily integrated into your Java projects using Maven Or Gradl. Follow the steps below to include the SDK in your project.

### Prerequisites

Ensure you have Maven installed and configured on your system. The SDK requires Java 11 or higher due to its dependencies and language features.

### Android studio

Place the JAR file in the project folder `libs`.

Open your build.gradle file located in your app module directory.
Add the include JAR Package and web3j dependency within the dependencies block as shown above.
Sync your Gradle project to ensure the dependencies are downloaded and included in your project build path.

```gradle
implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
implementation "org.web3j:core:4.8.7-android"
```

This version of web3j is optimized for Android development, providing compatibility with Android's networking and concurrency frameworks.

### Maven

### Adding the SDK to Your Project

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


## Config

### Contracts

The `Contracts` abstraction in the `Config` folder allows for easy management of contract addresses and related functionalities.

- **NAME_HASH_ADDRESS**: Address for the NameHash contract.
- **W_ENS_ADDRESS**: Address for the Wrapped ENS contract - (.web3) 

### Network

The `Network` abstraction facilitates the configuration of different blockchain networks.

- **ENTRY_POINT_URL**: URL for the network's entry point.
- **CHAIN_ID**: Identifier for the blockchain network.

## Arnacon Components

Detailed overview of each component within the `Arnacon` directory.

### Contracts

Extends `Contracts` from the `Utils` SubPackage to provide specific contract addresses for the Arnacon ecosystem.

### Cloud Functions

Includes methods to interact with cloud functions for tasks such as retrieving user ENS or service provider details.



```java
getUserENS(String userAddress)
```
- Sends a request to a cloud function to retrieve the ENS name associated with the given user address.
***

```java
getShopCID(String serviceProvider)
```
- Fetches the CID of the shop associated with the given service provider. This CID can be used to access the shop's content on IPFS.

***
***

### Config Service Provider

Manages service provider configurations, allowing for customization of service provider-related functionalities.

### Networks 

#### Ethereum

Configures the SDK to interact with the Ethereum blockchain.

#### Mumbai

Configures the SDK for interaction with the Polygon Mumbai test network.

### Initialization

#### InitAppWeb2

The `InitAppWeb2` class is provided as a conceptual example to illustrate how Web2 applications can interact with blockchain technologies using the Cellact SDK. This class demonstrates a series of operations from initializing network connections to executing blockchain transactions, all within a synchronous, step-by-step execution model typical in Web2 environments.

### Synchronous Process Execution

The use of `Scanner` and user input prompts in `InitAppWeb2` is deliberate:

**Process Synchronization**: The `Scanner` lines act as breakpoints, ensuring that each step is completed before proceeding to the next. This is crucial in blockchain interactions where operations depend on the successful completion of previous steps (e.g., a transaction must be mined before its effects can be observed).

### Step-by-Step Guide

1. **Initialization**

    - Initializes the Web3 service with the Mumbai test network configuration.
    - Sets up the configuration for the service provider.
    - Instantiates the cloud functions service.

        ```java
        this.Web3Service = new Web3AJ(new Mumbai());
        configServiceProvider = new configServiceProvider("test2.cellact.nl");
        cloudFunctions = new cloudFunctions();
        ```

2. **Shop CID Retrieval and Display**

    - Uses the `cloudFunctions` service to retrieve the CID for the shop associated with the configured service provider.
    - Displays the shop CID, which is used to fetch content from IPFS.

        ```java
        String shopCID = cloudFunctions.getShopCID(configServiceProvider.getServiceProviderName());
        ```

3. **IPFS Content Fetch and Display**     

    - Retrieves content from IPFS using the shop CID and displays the content.

        ```java
        String ipfsContent = Web3AJ.fetchStoreFromIPFS(shopCID);
        ```

4. **User Interaction for Package Selection**

    - Prompts the user to select a package and validates the selection against the available packages in the IPFS content- that correspondes to the store packages.

        ```java
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter package you want: ");
        String packageNum = scanner.nextLine();
        boolean isValid = Utils.isValidPackage(packageNum, ipfsContent);
        System.out.println("Package validated? " + isValid);
        ```

5. **Payment URL Generation**

    - If the package selection is valid, generates a payment URL for the selected package.

        ```java
        String url = Utils.getPaymentURL(this.Web3Service.wallet.getPublicKey(), packageNum, ipfsContent);
        ```
***
(Remark) Between those steps there should be a waiting time in order to complete the registration of the user and be able to retrieve the ENS.
***

6. **User ENS Retrieval**

    - Retrieves the ENS name associated with the user's public key - Only after the payment and google as been completed(before this step there should be a wait of 10-20 seconds)

        ```java
        String ens = cloudFunctions.getUserENS(this.Web3Service.wallet.getPublicKey());
        ```

7. **Message Signing**

    - Demonstrates signing a message with the user's private key- signing using hash protcol- SHA3

        ```java
        String message = "9c71ab46-370b-40f6-8235-bf1b03da1867"; // Example message
        String signed = Web3Service.signMessage(message);
        ```

### Important Considerations

- **Security**: Always ensure sensitive operations, especially those involving private keys and signatures, are handled securely. Avoid exposing private keys in your application code.

- **Asynchronous Nature of Blockchain**: While this example uses a synchronous approach for simplicity - thus between steps you may want to consider to make some checking to the blockchain before continuing 


#### InitAppWeb3

(TBD: Initialization steps for Web3 applications.)

### Utils

Provides utility functions such as package validation, shop opening, and payment URL generation.

```java
isValidPackage(String userInput, String jsonData)
```
- Validates if the user input matches any of the packages listed in the provided JSON data. 
  This function is essential for verifying user selections against available options.

***

```java
getPaymentURL(String userID, String packageNum, String jsonData)
```
- Constructs a URL for processing payments for a selected package using Stripe payment service. 


***
***      

### Wallet

Manages wallet functionalities including key generation, import, and retrieval.

```java
generateMnemonic()
```
- Generates a new mnemonic phrase following the BIP-39 standard.
  This mnemonic can be used to regenerate a wallet's private keys.

***

```java
getPrivateKeyFromMnemonic(String mnemonic)
```
- Derives the private key from the given mnemonic phrase, allowing for wallet recovery and transaction signing.

***

```java
getPublicKey()
```
- Returns the public key derived from the wallet's private key. 
  The public key is used to derive the wallet's address and can be safely shared.

***

```java
getCredentials()
```
- Retrieves the wallet's credentials, including its private key, which is crucial for signing transactions and interacting with smart contracts.

***
***

### Web3AJ

Core component for interacting with the blockchain, including contract interactions, signing messages, and handling transactions.


```java
signMessage(String Message)
```
- Takes a string message, signs it with the private key of the current wallet, and returns the signature.
  This is useful for proving ownership of a wallet address without revealing the private key.

***

```java
mintNFT(String _userENS)
```
- Encodes a function call to mint an NFT with the provided ENS (Ethereum Name Service) name. 
  It constructs and sends a transaction to mint the NFT, then returns the transaction hash.

***
```java
buyENS(String _userENS)
```
- Similar to mintNFT, but specifically for purchasing an ENS name. 
  It sends a transaction to the ENS contract to register the name under the user's wallet address.

***
```java
checkBalance(String publicKey)
```
- Returns the balance of the wallet associated with the given public key. 
  This function is essential for monitoring wallet funds.

*** 
```java
fetchStoreFromIPFS(String cid) 
```
- Fetches content from IPFS using the provided CID (Content Identifier).
  This function is useful for retrieving decentralized content.

***
***

## License

&copy; All right reserved to B.V Cellact NL.
