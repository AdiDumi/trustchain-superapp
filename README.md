# Project: Offline Digital Euro application

This is the repository for the Blockchain Engineering course (CS4160). Our task was to create an easy payment platform using tokens that is able to transfer euros, without Internet. Giving and receiving tokens should be easy and effortless. 

## Abstract
The Offline Digital Euro application is prototype application that implements a way to exchange token-based euros digitally while not being connected to the internet. 

Key features include:
- Simple user-friendly design
- Data persistance when restarting the application
- View transaction history
- Local faucet that can print money for demonstrative purposes


 

# User-Guide
The information listed below shows the steps needed to take to accomplish the desired goal when using the Offline Digital Euro application.

Steps to print digital euro tokens:
1. Open up the application.
2. Print euros via the top right menu.
3. Select the euro tokens to add the wallet.
4. Click on the Continue button. The euro tokens are generated and will be displayed in the user's wallet.

Steps to send euro tokens:
1. Open the application.
2. Click on the Send button.
3. Select the amount of euro tokens to send.
4. When clicking the Select button, a QR-code gets generated for the receiver to scan.
5. Have the receiving party scan the QR-code 

Steps to receive euro tokens:
1. Open the application
2. Click on the Get button. This opens up a camera with scanning capabilities.
3. Now have the sending party generate a QR code with the agreed number of tokens.
4. Scan the generated QR-code. Confirmation sound will play once transaction is complete.
5. Check in balance and transaction history whether transaction has been completed.


# Solution
The special requirement was that it should work in an emergency: when the Internet is down. When starting the project, we were advised to implement QR-code scanning to move euros between devices since that was the easiest to implement. Additionally, we had to come up with a mitigating measure that allows for the detection of any malicious users or cheaters that try to double spend.

## Double Spending Mitigation
The hard scientific task is to come up with a measure to mitigate the double spending risk. This refers to the risk that a user may spend the same tokens more than once. Since offline transactions cannot be immediately verified by the network or a central authority, it is possible for a user to spend a tokens and then quickly initiate another transaction using the same tokens, before the network/authority has a chance to process the first transaction. This can result in a situation where the user has spent more tokens than they actually own, which undermines the integrity and security of the whole network.

### Prevention
The first measure in double spending mitigation is the prevention of it.

### Detection
While prevention measures in the design can make it more difficult to double spend. It does not completely migitate the risk. Therefore, it becomes important to detect when it in fact does occur. We came up with duplicate token detection measure that update



- Debt accumulation
- Not connected to network in XX amount of days when 1 party uploaded transaction.

### Enforcement
Whenever double spending is detected, it should first be investigated whether it was done on purpose or whether something went wrong in the process. This can be done in similar fashion to what commericial banks are doing when they see strange transactions on one's creditcard.
However, when done on purpose and maliciously, the people in question should be held accountable. Our solution to this is that wallets are tied to real-world identities, when double spending is detected. It becomes a matter of the law to track down these malicious users and procecute them.

Whenever a person whom has spend money in an offline environment comes back online again, the token, transactions and web-of-trust information gets uploaded to the central authority's servers where the information gets processed accordingly to update the chain about who is trustworthy or not.

# Limitations
During the development of our solution, we encountered various limitations. The main issues are related to the implementation of the QR code.

## QR code limitations
1. QR codes can only contain max 3KB of information, sending lots of tokens will not be possible.
2. When sending a large number of tokens, the QR code gets very cluttered. This makes it difficult for the receiving party to accurately scan the code.
3. Exchange of information and data is only done one-way. 
4. Everyone can scan the QR-code, so if somebody gets a copy of the QR and scans it as well, it will save that transaction

## Data Storage

Since we are storing the tokens and transactions locally, in theory it would possible to come across a storage limitiation where there is no more space left on the device to store information. It especially poses a risk when a user's device also has information from other apps like photos, audio or videos.  

# Future Research 

- One solution would be to try and implement Near Field Communication (NFC) instead of using a QR-code implementation. This solves many of the mentioned limitations since it allows for private two-way communication between two devices and the exchange of more information. 
    - No size limitation of 3KB
    - No hijacking of the session by scanning other people's QR codes.
    - Both parties are able to exchange information to one another this improves the bookkeeping and administration of transactions. 
- A solution for the data storage problem would be to keep a blacklist of malicous users issued by the Central Authority instead of keeping track of every individual token in your posession. 


# API Documentation

## Sending Euro Tokens
We adopt the existing EuroToken as our token. On the main page, when a user wants to send tokens, click *"SEND"* button and choose the amount of tokens of each value to send. 

- Fragment Name: SendDigitalEuroFragment

**loadTokensToSend(oneCount: Int, twoCount: Int, fiveCount: Int, tenCount: Int): MutableSet<Token>**
Loads the specified number of tokens of each denomination (1, 2, 5, and 10) from the database to be sent to the recipient.
 
- Parameters:
  - oneCount - The number of 1 Euro tokens to be sent.
  - twoCount - The number of 2 Euro tokens to be sent.
  - fiveCount - The number of 5 Euro tokens to be sent.
  - tenCount - The number of 10 Euro tokens to be sent.
 
- Returns: A mutable set containing the selected tokens to be sent.
 
**dbTokens2Tokens(dbTokens: Array<DBToken>, count: Int): MutableList<Token>**
Converts an array of DBToken objects into a list of Token objects.
 
- Parameters:
  - dbTokens - An array of DBToken objects to be converted.
  - count - The number of tokens to convert.
- Returns: A mutable list containing the converted Token objects.

**Usage**
1. When a user wants to send tokens, navigate to the main page.
2. The user choose the amount of tokens of each value to be sent.
3. The fragment will display a QR code containing the transfer data.
4. The recipient scans the QR code to receive the tokens.
5. The sender can either cancel the transfer or confirm it by clicking the corresponding buttons. If confirmed, the tokens will be removed from the sender's database.
 
## Receiving Euro Tokens
On the main page, the user could also use the *GET* button to get tokens from other users. Click the *GET* button to scan other user's QR Code. This QR code is generated by another user. 
 
- Usage
  - The user clicked the *GET* button, and scan other users' QR Code.
  - The user can scan the QR code to obtain the transfer information.
  - The trust score of the sender is displayed, along with a color-coded warning message based on the trust score value.
  - The user can either accept the transfer or refuse it by clicking the corresponding buttons. If accepted, the tokens will be added to the user's database.
  - If refused, no action will be taken and the user will be redirected back to the transfer fragment.



## 

# Database Design

In order for our offline money token application to be usable we need to be able to save and store information that is entered and exchanged from another device. This information needs to be there every time the user starts the application again. To achieve this, we need to store information permanently onto the device. 
One method to store and retrieve persistent variables throughout your application is through the use of SharedPreferences. However, SharedPreferences is intended for storing small amounts of data, such as user preferences and settings. Since we need to store more complex data structures, such as lists or objects, we choose to use a database using Room library. Room is built on top of SQLite and provides a set of annotations that allow you to define the database schema, as well as the relationships between entities, and access them through DAO (Data Access Object) classes. It also provides built-in support for common database operations such as insert, update, and delete.

### Room Database Design

DB name: *OfflineDigitalEuroRoomDatabase*

The following entities (tables) will be stored in the database along with their respective columns:
In the userdata_table all information regarding the user will be stored. This table will only consist of one row that will be updated whenever information regarding the user changes.

-	userdata_table
    - user_id : Int
    - username : String
    - public_key : String
    - private_key : String

The transactions_table will contain transactions that took place for the user that is logged into the device. 

-	transactions_table
   - transaction_id : Int
   - transaction_datetime : String
   - pubk_sender : String
   - pubk_receiver : String
   - amount : Double 
   - verified : Boolean

The tokens_table stores all tokens that the logged in user owns. Currently owned tokens are stored in this table, but also transferred incoming tokens will be inserted into the table.

-	tokens_table
    - token_id : String
    - token_value : Double
    - token_data : ByteArray/String

The weboftrust_table will keep track of the reputations of other people that the device’s user interacted with. It stores the public key of those users and a score value that is associated with that user. 

-	weboftrust _table
    - public_key : String
    - trust_score: Int


We can interact with the data in the database through the use of DAOs where each table will have its own DAO with functions that send instructions to the database.

Userdata_table : UserDao 

```getUserData()```

```insertUser()```

```deleteUserData()```

transactions_table : TransactionsDao

```getTransactionData()```

```getLimitedTransactionsData(limit_value)```

```insertTransaction(transactions)```

```deleteTransactionData(transactions)```



tokens_table : TokensDao

``` getAllTokens ()```

```getAllTokensOfType(token_type)```

``` getAllTokensOfValue(token_value)```

```getCountTokensOfValue(token_value)```

``` getSpecificToken(token_id)```

```insertToken()```

``` deleteToken(token_id)```


weboftrust_table : WebOfTrustDAO

``` getUserTrustScore(public_key)```

``` getAllTrustScores()```

```insertUserTrustScore(user)```

``` updateUserScore(public_key, update_score_value)```

