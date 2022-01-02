package id.adjidarmo.rsud.app.function

import android.app.Application
import android.content.Context

class Helper: Application() {

    companion object{
        fun Log(text:String){
            android.util.Log.i("APPRSUD",text);
        }

        fun Toast(context: Context, text: String){
            android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}