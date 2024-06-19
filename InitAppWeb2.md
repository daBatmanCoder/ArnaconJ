```java

public class InitAppWeb2 {

    AWeb3AJ Web3Service;

    public InitAppWeb2() throws Exception {

        ADataSaveHelper dataSaveHelper = new SharedPreferencesHelper();
        ALogger logger = new Logger();

        Web3Service = new Web3AJ(dataSaveHelper, logger);

        logger.debug("Public key: " + Web3Service.wallet.getPublicKey());  
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        
        String[] serviceProviders = Web3Service.getServiceProviderList(); // Change for a generic URL for the service provider 
        int counter = 1;
        for (String provider : serviceProviders) {
            logger.debug("Service provider: " + counter + "." + provider);
            counter++;
        }
        logger.debug("Enter service provider you want:");
        String serviceProvider = scanner.nextLine();
        Web3Service.setServiceProvider(serviceProviders[Integer.parseInt(serviceProvider) - 1]);

        // Fetch store
        String ipfsContent = Web3Service.fetchStore();
        logger.debug("Store: " + ipfsContent);

        // // Choose a product
        logger.debug("Enter package you want:");
        String packageNum = scanner.nextLine();

        String successURL = "https://www.cellact.com";
        String cancelURL = "https://www.cellact.com";

        // Payment URL
        String url = Web3Service.getPaymentURL(
            packageNum,
            successURL,
            cancelURL
        );

        // window.href(url);
        logger.debug("Payment URL: " + url);

        scanner.nextLine();
        scanner.close();
        
        String fcm_token = "Cl2fX1UQt-R7vY0NxmVV9:APA91bHZdgZ4XvSvq5FDTBI7SCMY1794XQEHpjYE9I_tUIgb5nqoyqt4xoSut_Il7wqW1pFOwV75I80-CvwkQ7NgCqTU9HWGUhp_TZbsbZS3NvxjNcYUTjpKghVxRijafkrE-Wn3hIRH";
        Web3Service.sendDirectFCM(fcm_token);
        String customerIDInput = scanner.nextLine();
        logger.debug(Web3Service.getENS(customerIDInput));

        Web3Service.saveProduct("ANONYMOUS");
        Web3Service.saveProduct("83745837458");
        Web3Service.saveProduct("83745837458123");




        String password = "123123";
        String ciphertextHex = "b035d38ea08b1fcce1a47ca476734c80e3c70abc33333738834a95bd2e4ecc9862356b468815f24cef1bd07f7c9af35b9f30717cce347e65095313d462e84b7deeeb29ac6b5bef8363eddcf0423ccd8c8cfcae0ed186e98d4d93f36c761d08d6595568bf66e3f9cf473e2acfdd9eba53e92d048e0a832a910221229fb700d5b60201de044923243a0fdfd621a8933f06";
        String itemFromEncrypted = Web3Service.updateNewProduct(password, ciphertextHex);
        logger.debug("Item from cloud: " + itemFromEncrypted);

        String domainOFGSM = Web3Service.getGSMDomain(itemFromEncrypted);
        logger.debug(domainOFGSM);

        String domainOfCallee = Web3Service.getCalleeDomain("WcF94LY7UTmZ59cm.cellact");
        logger.debug("Domain of callee: " + domainOfCallee);
 
        String XData = Web3Service.getXData();
        logger.debug("XData: " + XData);
        String XSign = Web3Service.getXSign(XData);
        logger.debug("XSign: " + XSign);


    }
}

```
