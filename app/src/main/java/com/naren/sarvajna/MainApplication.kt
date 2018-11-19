package com.naren.sarvajna

import android.app.Application
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

public class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        copyDB()
    }

    fun copyDB() : Unit {
        try {

            val dbFile = File(Environment.getDataDirectory().absolutePath + File.separator + "data" + File.separator + packageName + File.separator + "databases" + File.separator + "1.db")
            if (dbFile.exists()) {
                return
            }
            if(!dbFile.parentFile.exists()){
                dbFile.parentFile.mkdirs()
            }
            dbFile.createNewFile()
            val fis = assets.open("1.vc")
            val fos = FileOutputStream(dbFile)
            var bytes = ByteArray(2048)
            while (fis.available() > 0) {
                fis.read(bytes)
                fos.write(bytes)
            }
            fis.close()
            fos.close()
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }
}