package account.mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException {
        String dateStr = jsonParser.getText();
        try {
            dateFormat.setLenient(false);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + dateStr, e);
        }
    }
}
