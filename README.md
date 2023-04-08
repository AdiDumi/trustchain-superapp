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
The special requirement was that it should work in an emergency: when the Internet is down. When starting the project, we were adviced to implement QR-code scanning to move euros between devices since that was the easiest to implement. 


## Double Spending Mitigation
The hard scientific task is to come up with a measure to mitigate the double spending risk. This refers to the risk that a user may spend the same tokens more than once. Since offline transactions cannot be immediately verified by the network, it is possible for a user to spend a tokens and then quickly initiate another transaction using the same tokens, before the network has a chance to process the first transaction. This can result in a situation where the user has spent more tokens than they actually own, which undermines the integrity and security of the whole network. 

### Prevention
The first measure in double spending mitigation is the prevention of it.

### Detection
While prevention measures in the design can make it more difficult to double spend. It does not completely migitate the risk. Therefore, it becomes important to detect when it in fact does occur.

- Debt accumulation
- Not connected to network in XX amount of days when 1 party uploaded transaction.

### Enforcement
Whenever double spending is detected, it should first be investigated whether it was done on purpose or whether something went wrong in the process. This can be done in similar fashion to what commericial banks are doing when they see strange transactions on one's creditcard.
However, when done on purpose and maliciously, the people in question should be held accountable. 

Our solution to this is that future wallets are tied to real-world identities, when double spending is detected. It becomes a matter of the law to track down and procecute them.

# Limitations
During the development of our solution, we encountered various limitations.

- QR codes can only contain max 1MB of information, sending lots of tokens will not be possible.
- Exchange of information and data is only done one-way. 
- Everyone can scan the QR-code, so if somebody gets a copy of the QR and scans it as well, it will save that transaction
- When sending a large number of tokens, the QR code gets very cluttered. This makes it difficult for the receiving party to accurately scan the code.


# Future Research 

- One solution would be to try and implement Near Field Communication (NFC) instead of using a QR-code implementation. This solves many of the mentioned limitations since it allows for private two-way communication between two devices and the exchange of more information. 
- - No size limitation of 1MB
- - No hijacking of the session by scanning other people's QR codes.
- - Both parties are able to exchange information to one another this improves the bookkeeping and administration of transactions. 


# API Documentation

## Sending Euros


## Receiving Euros


## 

# Database Design

In order for our offline money token application to be usable we need to be able to save and store information that is entered and exchanged from another device. This information needs to be there every time the user starts the application again. To achieve this, we need to store information permanently onto the device. 
One method to store and retrieve persistent variables throughout your application is through the use of SharedPreferences. However, SharedPreferences is intended for storing small amounts of data, such as user preferences and settings. Since we need to store more complex data structures, such as lists or objects, we choose to use a database using Room library. Room is built on top of SQLite and provides a set of annotations that allow you to define the database schema, as well as the relationships between entities, and access them through DAO (Data Access Object) classes. It also provides built-in support for common database operations such as insert, update, and delete.

### Room Database Design

DB name: OfflineDigitalEuroRoomDatabase

The following entities (tables) will be stored in the database along with their respective columns:
In the userdata_table all information regarding the user will be stored. This table will only consist of one row that will be updated whenever information regarding the user changes.

-	userdata_table
o	user_id : Int
o	username : String
o	public_key : String
o	private_key : String

The transactions_table will contain transactions that took place for the user that is logged into the device. 

-	transactions_table
o	transaction_id : Int
o	transaction_datetime : String
o	pubk_sender : String
o	pubk_receiver : String
o	amount : Double
o	verified : Boolean

The tokens_table stored all tokens that the logged in user owns. Currently owned tokens are stored in this table, but also transferred incoming tokens will be inserted into the table.

-	tokens_table
o	token_id : String
o	token_value : Double
o	token_data : ByteArray/String

We can interact with the data in the database through the use of DAOs where each table will have its own DAO with functions that send instructions to the database.

Userdata_table : UserDao 
-	getUserData()
-	insertUser()
-	updateUser()
-	deleteUserData()

transactions_table : TransactionsDao
-	getTransactionData()
-	insertTransaction()

tokens_table : TokensDao
-	getMoneyBalance()
-	getAllTokensOfType(token_type)
-	insertToken()
-	deleteToken()
