package id.adjidarmo.rsud.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import id.adjidarmo.rsud.app.databinding.ActivityMainBinding
import id.adjidarmo.rsud.app.function.Helper
import im.delight.android.webview.AdvancedWebView

class MainActivity : AppCompatActivity(), AdvancedWebView.Listener {
    private lateinit var binding: ActivityMainBinding
    private var url: String? = null
    private val rxPermissions = RxPermissions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponent()
        getPermissionAccess()
    }

    private fun initComponent(){
        url = BuildConfig.base_URL
        checkNetworkConnection(url)
        binding.apply {
            lytNoInternet.btnReload.setOnClickListener {
                loadWebview(url)
            }
        }
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
    }

    override fun onPageFinished(url: String?) {
        binding.vfWebview.displayedChild = 2
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        if(description.equals("net::ERR_FAILED")){
            //Error
        } else {
            binding.wvMain.loadUrl("about:blank")
            binding.vfWebview.displayedChild = 1
        }
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun onExternalPageRequest(url: String?) {
        TODO("Not yet implemented")
    }

    private fun loadWebview(url: String?) {
        binding.wvMain.apply {
            setListener(this@MainActivity, this@MainActivity)
            setGeolocationEnabled(false)
            setMixedContentAllowed(true)
            setCookiesEnabled(true)
            setThirdPartyCookiesEnabled(true)
            loadUrl(url.toString())
        }
    }

    @SuppressLint("CheckResult")
    fun getPermissionAccess(){
        rxPermissions
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
                if (granted) {
                    // All requested permissions are granted
                    Helper.Log("Permission Granted")
                } else {
                    // At least one permission is denied
                    Helper.Log("Permission Denied")
                }
            }
    }

    private fun checkNetworkConnection(url: String?): Boolean {
        val connMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        var isConnected = false
        if (networkInfo != null && networkInfo.isConnected.also { isConnected = it }) {
            loadWebview(url.toString())
        } else {
            Toast.makeText(this, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show()
        }
        return isConnected
    }

    override fun onBackPressed() {
        if (!binding.wvMain.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}