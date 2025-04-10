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
        Toast.makeText(this, "DB Path: $dbPath", Toast.LENGTH_LONG).show()

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

        val insertedRow = db.insert("Words", null, values)
        Toast.makeText(this, "Inserted row ID: $insertedRow", Toast.LENGTH_SHORT).show()

        // Debug: Read all words in DB
        val cursor = db.rawQuery("SELECT word FROM Words", null)
        val wordList = mutableListOf<String>()
        while (cursor.moveToNext()) {
          wordList.add(cursor.getString(cursor.getColumnIndexOrThrow("word")))
        }
        cursor.close()

        Toast.makeText(this, "All: ${wordList.joinToString()}", Toast.LENGTH_LONG).show()

        db.close()
      } catch (e: Exception) {
        Toast.makeText(this, "Error saving: ${e.message}", Toast.LENGTH_LONG).show()
      }
    }

    finish()
  }
}