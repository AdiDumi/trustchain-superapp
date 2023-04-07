# Project: Offline Digital Euro application

This is the repository for the Blockchain Engineering course (CS4160). Our task was to create an easy payment platform using tokens that is able to transfer euros, without Internet. Giving and receiving tokens should be easy and effortless. 

## Abstract
The Offline Digital Euro application is a prototype that implements a way to exchange euros digitally while not being connected to the internet. 

Key features include:
- Simple user-friendly design
- Data persistance when restarting the application
- View transaction history
- Local faucet that can print demo money

PeerChat implements a fully functional prototype of a distributed messaging app. First, the users have to exchange the public keys by scanning each other's QR code, or by copy-pasting the hexadecimal public keys. This guarantees authenticity of all messages which are signed by their author. It prevents man-in-the-middle and impersonation attacks.

An online indicator and the last message is shown for each contact. Users can exchange text messages and get acknowledgments when a message is delivered.


The special requirement was that it should work in an emergency: when the Internet is down. We use QR-codes scanning to move euros between devices. The hard scientific task is to come up with a measure to mitigate the double spending risk. This refers to the risk that a user may spend the same tokens more than once. Since offline transactions cannot be immediately verified by the network, it is possible for a user to spend a tokens and then quickly initiate another transaction using the same tokens, before the network has a chance to process the first transaction. This can result in a situation where the user has spent more tokens than they actually own, which undermines the integrity and security of the whole network. 

# Solution


## Double Spending Mitigation

### Prevention

### Detection

### Enforcement
- Wallets are tied to real-world identities, when debt accumulation on the online chainpasses thresholds like  


# Future Research
During the development of our solution, we encountered various limitations.

- QR-code not the ideal solution:
QR codes can only contain max 1MB of information, sending lots of tokens will not be possible. Disadvantage is that communication is done one-way.
A solution would be to try and implement Near Field Communication (NFC) enables two-way communication between devices.

- 


# API Documentation

## Sending Euros


## Receiving Euros


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



