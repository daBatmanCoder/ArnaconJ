```java

public class InitAppWeb2 {

    public InitAppWeb2() throws Exception {

        DataSaveHelper dataSaveHelper = <HELPER_FOR_DATA_SAVE>;
        ConfigServiceProvider serviceProvider = new ConfigServiceProvider("test2.cellact.nl", dataSaveHelper);

        Web3AJ Web3Service = new Web3AJ(dataSaveHelper);

        // Fetch store
        String ipfsContent = Web3Service.fetchStore();
        System.out.println("Store: " + ipfsContent);

        // // Choose a product
        System.out.println("Enter package you want: ");
        String packageNum = "<USER_INPUT>";

        // Payment URL
        String url = Web3Service.getPaymentURL(packageNum);
        System.out.println("Payment URL: " + url);

        // Send FCM
        String fcm_token = "9c71ab46-370b-40f6-8235-bf1b03da1867";
        Web3Service.sendFCM(fcm_token);

    }
}
```
