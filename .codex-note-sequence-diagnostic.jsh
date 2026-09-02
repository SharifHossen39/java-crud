import java.sql.*;

void inspectNoteSequence() throws Exception {
    var properties = new java.util.Properties();
    properties.setProperty("user", System.getenv("NOTE_DB_USER"));
    properties.setProperty("password", System.getenv("NOTE_DB_PASSWORD"));
    properties.setProperty("readOnly", "true");
    try (var connection = DriverManager.getConnection(System.getenv("NOTE_DB_URL"), properties)) {
        connection.setReadOnly(true);
        try (var statement = connection.createStatement();
             var results = statement.executeQuery("select last_value, is_called from public.note_sharing_id_seq")) {
            results.next();
            System.out.printf("last_value=%d | is_called=%s%n", results.getLong("last_value"), results.getBoolean("is_called"));
        }
    }
}

inspectNoteSequence();
/exit
