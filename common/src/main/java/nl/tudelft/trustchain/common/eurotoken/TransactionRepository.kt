package nl.tudelft.trustchain.common.eurotoken

import androidx.core.graphics.translationMatrix
import nl.tudelft.ipv8.attestation.trustchain.TrustChainBlock
import nl.tudelft.ipv8.attestation.trustchain.TrustChainCommunity
import nl.tudelft.ipv8.keyvault.PublicKey
import nl.tudelft.ipv8.keyvault.defaultCryptoProvider
import java.lang.Math.abs
import java.math.BigInteger

class TransactionRepository (
    private val trustChainCommunity: TrustChainCommunity
) {

    fun createSendTransaction(recipient: ByteArray, amount_in_cent: Long): TrustChainBlock {
        val transaction = mapOf(
            KEY_AMOUNT to amount_in_cent
        )
        return trustChainCommunity.createProposalBlock(
            BLOCK_TYPE_TRANSFER, transaction,
            recipient
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getBalance(): Long {
        return getTransactions().map { transaction ->
            (if (transaction.outgoing) -1 else 1) * transaction.amount
        }.reduceOrNull { a, b -> a+b } ?: 0L
    }

    fun getTransactions(): List<Transaction> {
        return trustChainCommunity.database.getBlocksWithType(BLOCK_TYPE_TRANSFER).map { block ->
            val sender = defaultCryptoProvider.keyFromPublicBin(block.publicKey)
            Transaction(
                block,
                sender,
                defaultCryptoProvider.keyFromPublicBin(block.linkPublicKey),
                (block.transaction["amount"] as BigInteger).toLong(),
                "transaction",
                sender == trustChainCommunity.myPeer.publicKey,
                block.timestamp
            )
        }
    }

    fun getTransactionWithHash(hash: ByteArray?): TrustChainBlock? {
        return hash?.let {
            trustChainCommunity.database
                .getBlockWithHash(it)
        }
    }

    companion object {
        fun prettyAmount(amount: Long): String {
            return "€" + (amount / 100).toString() + "," + (abs(amount) % 100).toString().padStart(2, '0')
        }

        private const val BLOCK_TYPE_TRANSFER = "transfer"
        private const val BLOCK_TYPE_CREATE   = "creation"
        private const val BLOCK_TYPE_DESTROY  = "destruction"

        const val KEY_AMOUNT = "amount"
    }

}
