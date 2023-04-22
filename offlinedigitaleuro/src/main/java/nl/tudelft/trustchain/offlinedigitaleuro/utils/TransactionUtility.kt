package nl.tudelft.trustchain.offlinedigitaleuro.utils

import android.util.Log
import nl.tudelft.ipv8.keyvault.PrivateKey
import nl.tudelft.ipv8.keyvault.PublicKey
import nl.tudelft.trustchain.offlinedigitaleuro.db.OfflineMoneyRoomDatabase
import nl.tudelft.trustchain.offlinedigitaleuro.payloads.TransferQR
import nl.tudelft.trustchain.offlinedigitaleuro.src.Token
import nl.tudelft.trustchain.offlinedigitaleuro.src.Wallet
import org.json.JSONObject

class TransactionUtility {

    class SendRequest(
        val oneEuroCount: Int = 0,
        val twoEuroCount: Int = 0,
        val fiveEuroCount: Int = 0,
        val tenEuroCount: Int = 0,
    ) {}

    class DuplicatedTokens(
        val incomingToken: Token,
        val storedToken: Token,
    ) {
        fun getDoubleSpenders() {
//            TODO: some LCC algo to find who double spent and be careful to not blame the central authority
        }

    }

companion object {

//    completes a transaction and inserts the tokens in the DB
//    returns true if the operation succeeded and on failure also returns the message
    fun receiveTransaction(tq: TransferQR, db: OfflineMoneyRoomDatabase, my_pbk: PublicKey): Pair<Boolean, String> {
        val nowOwnedTokens: MutableSet<Token> = cedeTokens(my_pbk, tq.pvk, tq.tokens.toMutableList())

        val result = TokenDBUtility.insertToken(nowOwnedTokens.toList(), db)
        val code: TokenDBUtility.Codes = result.first
        if (code != TokenDBUtility.Codes.OK) {
            Log.e("ODE", "Error: failed to insert received tokens into the DB, reason: $code")
            return Pair(false, "Error: failed to insert received tokens into the DB, reason: $code")
        }

        return Pair(true, "")
    }

//    Gets the tokens from the transfer qr and checks if they are already stored in the DB. If true,
//    then return the tokens for later to check who double spent
    fun getDuplicateTokens(tq: TransferQR) : MutableList<DuplicatedTokens> {


        return mutableListOf()
    }

//    completes the transaction by deleting the transferred tokens from the DB and some other stuff
//    returns true if everything went fine
//    otherwise returns false and the error message
    fun completeSendTransaction(sendTransaction: JSONObject, db: OfflineMoneyRoomDatabase) : Pair<Boolean, String> {
        val (maybeTq, errMsg) = TransferQR.fromJson(sendTransaction)

        if (maybeTq == null) {
            val newErrMsg = "Error: Transaction JSON wrongly formatted, reason: $errMsg"
            return Pair(false, newErrMsg)
        }
        val tq: TransferQR = maybeTq

        val result: Pair<TokenDBUtility.Codes, MutableList<Token>> = TokenDBUtility.deleteTokens(tq.tokens.toList(), db)
        val code: TokenDBUtility.Codes = result.first
        if (code != TokenDBUtility.Codes.OK) {
            Log.e("ODE", "Error: failed to delete tokens from DB, reason: $code")
            return Pair(false, "Error: failed to delete tokens from DB, reason: $code")
        }

        return Pair(true, "")
    }

//    get a JSON object which represents a transaction with the required tokens signed and the
//    intermediary wallet or get null and an error message
    fun getSendTransaction(req: SendRequest, db: OfflineMoneyRoomDatabase, my_pvk: PrivateKey): Pair<JSONObject?, String> {
        val tokenExtractTransaction = getTokensToSend(req, db)



        val tokens: MutableList<Token> = tokenExtractTransaction.first ?: return Pair(null, tokenExtractTransaction.second)

        val intermediaryWallet: Wallet = Wallet()

        val cededTokens: MutableSet<Token> = cedeTokens(intermediaryWallet.publicKey, my_pvk, tokens)

        val ret: JSONObject =  TransferQR.createJson(intermediaryWallet.privateKey, cededTokens)

        return Pair(ret, "")
    }

//    extracts the required number of tokens of certain values from the DB
//    returns null and an error message on failure
    private fun getTokensToSend(req: SendRequest, db: OfflineMoneyRoomDatabase): Pair<MutableList<Token>?, String> {
        val coinAndCount: List<Pair<Double, Int>> = listOf(
            Pair(1.0, req.oneEuroCount),  Pair(2.0, req.twoEuroCount),
            Pair(5.0, req.fiveEuroCount), Pair(10.0, req.tenEuroCount)
        )

        val ret: MutableList<Token> = mutableListOf()

        for (r in coinAndCount) {
            val coinVal = r.first
            val coinCount = r.second

            val result = TokenDBUtility.getTokens(coinVal, coinCount, db)

            val code = result.first
            if (code != TokenDBUtility.Codes.OK) {
                Log.e("ODE", "Error: failed to prepare to send tokens, reason: $code")
                return Pair(null, "Error: failed to prepare to send tokens, reason: $code")
            }

            ret.addAll(result.second)
        }

        return Pair(ret, "")
    }

//    change the owner of the tokens to the public key given
    private fun cedeTokens(other_pbk: PublicKey, my_pvk: PrivateKey, tokens: MutableList<Token>) : MutableSet<Token> {
        val ret: MutableSet<Token> = mutableSetOf()

        for (t in tokens) {
            t.signByPeer(other_pbk.keyToBin(), my_pvk)
            ret.add(t)
        }

        return ret
    }

} // companion object
}
