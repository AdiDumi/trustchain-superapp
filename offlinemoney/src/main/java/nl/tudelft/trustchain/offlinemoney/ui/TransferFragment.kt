package nl.tudelft.trustchain.offlinemoney.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import nl.tudelft.ipv8.util.toHex
import nl.tudelft.trustchain.common.contacts.ContactStore
import nl.tudelft.trustchain.common.util.QRCodeUtils
import nl.tudelft.trustchain.common.util.viewBinding
import nl.tudelft.trustchain.offlinemoney.R
import nl.tudelft.trustchain.offlinemoney.databinding.ActivityMainOfflineMoneyBinding
import org.json.JSONObject

class TransferFragment : OfflineMoneyBaseFragment(R.layout.activity_main_offline_money) {
    private val binding by viewBinding(ActivityMainOfflineMoneyBinding::bind)

    private val qrCodeUtils by lazy {
        QRCodeUtils(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGet.setOnClickListener {
            qrCodeUtils.startQRScanner(this)
        }

        binding.btnSend.setOnClickListener{
            val amount = getAmount(binding.edtAmount.text.toString())
            println(amount)

            if (amount > 0) {
                val myPeer = "kjalsdjfoiawonsad"

                val connectionData = JSONObject()
                connectionData.put("public_key", myPeer)
                connectionData.put("amount", amount)
                connectionData.put("name", myPeer)


                val args = Bundle()

                args.putString(SendMoneyFragment.ARG_DATA, connectionData.toString())

                findNavController().navigate(
                    R.id.action_transferFragment_to_sendMoneyFragment,
                    args
                )
            }

        }

    }

    fun getAmount(amount: String): Long {
        val regex = """[^\d]""".toRegex()
        if (amount.isEmpty()) {
            return 0L
        }
        return regex.replace(amount, "").toLong()
    }
}

