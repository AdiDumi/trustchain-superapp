package com.example.musicdao.core.wallet

import org.bitcoinj.params.RegTestParams
import java.io.File

data class WalletConfig(
    val cacheDir: File,
    val filePrefix: String,
    val networkParams: RegTestParams,
    val regtestBootstrapIp: String,
    val regtestBootstrapPort: String,
    val regtestFaucetEndPoint: String
) {
    companion object {
        val DEFAULT_NETWORK_PARAMS = RegTestParams.get()
        val DEFAULT_FILE_PREFIX = "regtest-musicdao"
        val DEFAULT_REGTEST_BOOTSTRAP_IP = "131.180.27.224"
        val DEFAULT_REGTEST_BOOTSTRAP_PORT = "3000"
        val DEFAULT_FAUCET_ENDPOINT = "https://taproot.tribler.org"
    }
}
