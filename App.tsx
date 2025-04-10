import { useEffect, useState } from "react";
import { View, TextInput, Button, FlatList, Text, RefreshControl } from "react-native";
import * as SQLite from "expo-sqlite"; // <-- NEW import for SDK 52+
import * as FileSystem from 'expo-file-system';

console.log("Expo SQLite Path:", FileSystem.documentDirectory + "SQLite/smara.db");
const db = SQLite.openDatabaseSync("smara.db");

export default function App() {
  const [text, setText] = useState("");
  const [words, setWords] = useState<string[]>([]);
  const [refreshing, setRefreshing] = useState(false);

  // create table once
  useEffect(() => {
    (async () => {
      await db.execAsync(`
        CREATE TABLE IF NOT EXISTS Words (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          word TEXT,
          createdAt INTEGER
        );
      `);
      await loadWords();
    })();
  }, []);

  const loadWords = async () => {
    const result = await db.getAllAsync<{ word: string }>(
      "SELECT word FROM Words ORDER BY createdAt DESC"
    );
    setWords(result.map(row => row.word));
  };

  const insertWord = async () => {
    const trimmed = text.trim();
    if (!trimmed) return;
    await db.runAsync(
      "INSERT INTO Words (word, createdAt) VALUES (?, ?)",
      [trimmed, Date.now()]
    );
    setText("");
    await loadWords();
  };

  return (
    <View style={{ flex: 1, padding: 20 }}>
      <TextInput
        placeholder="Type a word"
        value={text}
        onChangeText={setText}
        style={{
          borderWidth: 1,
          borderColor: "#ccc",
          padding: 10,
          marginBottom: 10,
          borderRadius: 5,
        }}
      />
      <Button title="Save Word" onPress={insertWord} />
      <FlatList
        data={words}
        keyExtractor={(item, index) => index.toString()}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={async () => {
              setRefreshing(true);
              await loadWords();
              setRefreshing(false);
            }}
          />
        }
        renderItem={({ item }) => (
          <Text style={{ paddingVertical: 10 }}>{item}</Text>
        )}
      />
    </View>
  );
}