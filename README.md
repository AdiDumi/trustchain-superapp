# Project: Offline Token Money application

This is the repository for the Blockchain Engineering course (XXX). Our task was to create an easy payment platform using tokens that is able to transfer tokens, without Internet. Giving and receiving tokens should be easy and effortless. 

The special requirement was that it should also work in an emergency: when the Internet is down. Probably you need to use QR-codes scanning to move Tiktok Tokens between devices. The hard scientific task is to come up with a measure to mitigate the double spending risk. This refers to the risk that a user may spend the same cryptocurrency more than once. Since offline transactions cannot be immediately verified by the network, it is possible for a user to spend a cryptocurrency and then quickly initiate another transaction using the same cryptocurrency, before the network has a chance to process the first transaction. This can result in a situation where the user has spent more cryptocurrency than they actually own, which undermines the integrity and security of the blockchain system. 



# API Documentation

Here we list and explain the function calls that are possible within our application:

'''send_tokens'''

'''receive_tokens'''

'''show_token_balance'''

'''show_user_transactions'''



