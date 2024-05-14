```java

public class InitAppWeb2 {

    AWeb3AJ Web3Service;

    public InitAppWeb2() throws Exception {

        ADataSaveHelper dataSaveHelper = new SharedPreferencesHelper();
        ALogger logger = new Logger();

        Web3Service = new Web3AJ(dataSaveHelper, logger);
        String[] serviceProviders = Web3Service.getServiceProviderList();
        Web3Service.setServiceProvider(serviceProviders[0]);

        // Fetch store
        String ipfsContent = Web3Service.fetchStore();
        System.out.println("Store: " + ipfsContent);

        // // Choose a product
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter package you want: ");
        String packageNum = scanner.nextLine();

        String successURL = "<YOUR_SUCCESS_DEEPLINK>";
        String cancelURL = "<YOUR_FAILURE_DEEPLINK>";

        // Payment URL
        String url = Web3Service.getPaymentURL(
            packageNum,
            successURL,
            cancelURL
        );
        System.out.println("Payment URL: " + url);

        scanner.nextLine();
        scanner.close();

        String fcm_token = "<YOUR_FCM_TOKEN_HERE";
        Web3Service.sendFCM(fcm_token);

        String XData = Web3Service.getXData();
        System.out.println("XData: " + XData);
        
        String XSign = Web3Service.getXSign(XData);
        System.out.println("XSign: " + XSign);

    }
}

```
