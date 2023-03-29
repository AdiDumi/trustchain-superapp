package nl.tudelft.trustchain.offlinemoney.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.tudelft.ipv8.util.toHex
import nl.tudelft.trustchain.common.util.QRCodeUtils
import nl.tudelft.trustchain.common.util.viewBinding
import nl.tudelft.trustchain.offlinemoney.R
import nl.tudelft.trustchain.offlinemoney.databinding.ActivityMainOfflineMoneyBinding
import nl.tudelft.trustchain.offlinemoney.payloads.RequestPayload
import nl.tudelft.trustchain.offlinemoney.payloads.Promise
import org.json.JSONObject
import org.json.JSONException
import nl.tudelft.ipv8.keyvault.defaultCryptoProvider
import nl.tudelft.ipv8.util.hexToBytes
import nl.tudelft.ipv8.util.toASCII

class TransferFragment : OfflineMoneyBaseFragment(R.layout.activity_main_offline_money) {
    private val binding by viewBinding(ActivityMainOfflineMoneyBinding::bind)

    private val qrCodeUtils by lazy {
        QRCodeUtils(requireContext())
    }

    private fun updateTextViewAmount() {
        var sum = 0;

        sum += binding.edt1Euro.text.toString().toInt() * 1 + binding.edt2Euro.text.toString().toInt() * 2 + binding.edt5Euro.text.toString().toInt() * 5;

        binding.txtBalance.text = sum.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edt1Euro.text = 8.toString()
        binding.edt2Euro.text = 2.toString()
        binding.edt5Euro.text = 6.toString()

        updateTextViewAmount()
//        val pbk = transactionRepository.
//
//        lifecycleScope.launch {
//            binding.txtPublicKey.text = pbk.toHex()
//
//            val json = JSONObject().put("type", "request")
//            json.put("payload", pbk.toString())
//            val bitmap = withContext(Dispatchers.Default) {
//                qrCodeUtils.createQR(json.toString())
//            }
//            binding.qrPublicKey.setImageBitmap(bitmap)
//        }

        binding.btnGet.setOnClickListener {
            qrCodeUtils.startQRScanner(this)
        }

        binding.btnSend.setOnClickListener{
            findNavController().navigate(R.id.action_transferFragment_to_sendAmountFragment)
        }

    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        qrCodeUtils.parseActivityResult(requestCode, resultCode, data)?.let {
            try {
                val type = JSONObject(it).optString("type")
                    val privateKey = defaultCryptoProvider.keyFromPrivateBin(
                        JSONObject(it).getString("pvk").hexToBytes()
                    )
                Log.d("DEBUG:", privateKey.toString());
            } catch (e: JSONException) {
                Toast.makeText(requireContext(), "Scan failed, try again", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(requireContext(), "Scan failed", Toast.LENGTH_LONG).show()
        return
    }
}


