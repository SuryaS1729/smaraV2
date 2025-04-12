package expo.modules.smaranative

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast

class SmaraActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val receivedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
    if (receivedText != null) {
      try {
        val dbPath = "/data/user/0/com.surya7314.smaraV2/files/SQLite/smara.db"
        val db = SQLiteDatabase.openOrCreateDatabase(dbPath, null)

        db.execSQL("""
          CREATE TABLE IF NOT EXISTS Words (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            word TEXT,
            createdAt INTEGER
          );
        """)

        val values = ContentValues().apply {
          put("word", receivedText)
          put("createdAt", System.currentTimeMillis())
        }

        val cursor = db.rawQuery("SELECT COUNT(*) FROM Words WHERE word = ?", arrayOf(receivedText))
        cursor.moveToFirst()
        val exists = cursor.getInt(0) > 0
        cursor.close()

        if (!exists) {
          db.insert("Words", null, values)
          Toast.makeText(this, "Saving: $receivedText", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(this, "Already saved: $receivedText", Toast.LENGTH_SHORT).show()
        }

        db.close()

        
      } catch (e: Exception) {
        Toast.makeText(this, "Error saving: ${e.message}", Toast.LENGTH_LONG).show()
      }
    }

    finish()
  }
}