package nl.tudelft.trustchain.offlinemoney.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.tudelft.ipv8.util.toHex
import nl.tudelft.trustchain.common.util.QRCodeUtils
import nl.tudelft.trustchain.common.util.viewBinding
import nl.tudelft.trustchain.offlinemoney.R
import nl.tudelft.trustchain.offlinemoney.databinding.SendMoneyFragmentBinding
import nl.tudelft.trustchain.offlinemoney.src.Wallet
import org.json.JSONObject

import java.util.UUID

import nl.tudelft.ipv8.keyvault.defaultCryptoProvider
import nl.tudelft.trustchain.offlinemoney.src.Token
import java.util.*

class SendMoneyFragment : OfflineMoneyBaseFragment(R.layout.send_money_fragment)  {
    private val binding by viewBinding(SendMoneyFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val json = JSONObject().put("type", "transfer")
        json.put("pvk", Wallet().privateKey.keyToBin().toHex())

        // Generate random byte arrays for id, verifier, and genesisHash
        val id = ByteArray(16) { UUID.randomUUID().toString().toByte() }
        val verifier = ByteArray(20) { "0".toByte() }
        val genesisHash = ByteArray(30) { "0".toByte() }

        // Create a new token with the generated byte arrays
        val token = Token(id, 5, verifier, genesisHash, mutableListOf())

        Log.d("TOKEN:", token.toString())
        //val promiseString = requireArguments().getString(ARG_DATA)!!
        // TO DO to store promise
        //binding.txtSendData.text = promiseString

        val json = JSONObject().put("type", "transfer")
        json.put("payload", 20)

        lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                qrCodeUtils.createQR(json.toString())
            }
            binding.qrImageView.setImageBitmap(bitmap)
        }

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_sendMoneyFragment_to_transferFragment)
        }
    }

    private val qrCodeUtils by lazy {
        QRCodeUtils(requireContext())
    }

    companion object {
        const val ARG_DATA = "data"
    }
}
