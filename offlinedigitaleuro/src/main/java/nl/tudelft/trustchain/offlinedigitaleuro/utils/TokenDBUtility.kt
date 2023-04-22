package nl.tudelft.trustchain.offlinedigitaleuro.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import nl.tudelft.ipv8.util.toHex
import nl.tudelft.trustchain.offlinedigitaleuro.db.OfflineMoneyRoomDatabase
import nl.tudelft.trustchain.offlinedigitaleuro.db.Token as DbToken
import nl.tudelft.trustchain.offlinedigitaleuro.src.Token as Token


class TokenDBUtility {

    enum class Codes {
        OK,                // all is good
        ERR_COLLISION,     // the token already is in the DB
        ERR_DB_TOKEN_CONV, // the token could not be converted to DB format or reversed
        ERR_NOT_FOUND,     // the token does not exist in the DB
        ERR_NOT_ENOUGH,    // not enough tokens in the DB to suffice request
    }

companion object {


//    inserts tokens into the DB
//    on success returns Pair<OK, []>
//    on failure returns Pair<err_code, [*]]>
//    if a token with the same id is in the DB we return it as Pair<ERR_COLLISION, [Token, ...]>
//    OR if conversion to DbToken fails we return Pair<ERR_DB_TOKEN_CONV, []>
    fun insertToken(tokens: List<Token>, db: OfflineMoneyRoomDatabase) : Pair<Codes, MutableList<Token>> {
        val ret: MutableList<Token> = mutableListOf()

        for (t in tokens) {
            val dbToken: DbToken = tokenToDbToken(t)
                ?: return Pair(Codes.ERR_DB_TOKEN_CONV, mutableListOf())

            runBlocking(Dispatchers.IO) {
                //        TODO: check if the token is already in the DB

                db.tokensDao().insertToken(dbToken)
            }
        }

        return if (ret.isEmpty()) {
            Pair(Codes.OK, mutableListOf())
        } else {
            Pair(Codes.ERR_COLLISION, ret)
        }
    }

//    finds a token in the DB and returns it or null
    fun findToken(t: Token, db: OfflineMoneyRoomDatabase) : Token? {
        return null
    }

//    gets how many tokens were requested out of the database
//    on success returns Pair<OK, [*]>
//    on failure returns Pair<err_code, []>
//    if not enough tokens are in the DB to satisfy the request we return Pair<ERR_NOT_ENOUGH, []>
//    OR if conversion from DbToken fails we return Pair<ERR_DB_TOKEN_CONV, []>
//    TODO: extract from DB only how many are needed, not all as it happens currently
    fun getTokens(value: Double, count: Int, db: OfflineMoneyRoomDatabase) : Pair<Codes, MutableList<Token>> {
        val dbTokens: Array<DbToken>

        runBlocking(Dispatchers.IO) {
            dbTokens = db.tokensDao().getAllTokensOfValue(value)
        }

        if (count > dbTokens.size) {
            return Pair(Codes.ERR_NOT_ENOUGH, mutableListOf())
        }

        val ret: MutableList<Token> = mutableListOf()

        for (i in 0 until count) {
            val token = dbTokenToToken(dbTokens[i]) ?: return Pair(Codes.ERR_DB_TOKEN_CONV, mutableListOf())

            ret.add(token)
        }

        return Pair(Codes.OK, ret)
    }

//    deletes the tokens that were passed from the DB
//    on success returns Pair<OK, []>
//    on failure returns Pair<err_code, [*]>
//    if a token is not found to be deleted we return Pair<ERR_COLLISION, [Token, ...]>
//    OR if conversion to DbToken fails we return Pair<ERR_DB_TOKEN_CONV, []>
    fun deleteTokens(tokens: List<Token>, db: OfflineMoneyRoomDatabase) : Pair<Codes, MutableList<Token>> {
        val ret: MutableList<Token> = mutableListOf()

        for (t in tokens) {
            val dbToken: nl.tudelft.trustchain.offlinedigitaleuro.db.Token = tokenToDbToken(t)
                ?: return Pair(Codes.ERR_DB_TOKEN_CONV, mutableListOf())

            runBlocking(Dispatchers.IO) {
//                TODO: check if token is inside before deleting
//                if (false) { ret.add(t) }

                db.tokensDao().deleteToken(dbToken.token_id)
            }
        }

    return if (ret.isEmpty()) {
            Pair(Codes.OK, mutableListOf())
        } else {
            Pair(Codes.ERR_NOT_FOUND, ret)
        }
    }

    private fun dbTokenToToken(o: DbToken): Token? {
        val setOfToken = Token.deserialize(o.token_data) // should be a set of only one token

        for (t in setOfToken) { // idk how to access a set of one element
            return t
        }

        return null
    }

    private fun tokenToDbToken(o: Token): DbToken? {
        val tId: String = o.id.toHex()
        val tValue: Double = o.value.toDouble()
        val tData: ByteArray = Token.serialize(mutableSetOf(o))

        if (tData.isEmpty()) {
            return null
        }

        return DbToken(
            tId,
            tValue,
            tData
        )
    }
} // companion object
}
