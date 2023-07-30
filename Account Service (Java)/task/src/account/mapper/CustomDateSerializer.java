package account.mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateSerializer extends JsonSerializer<Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM-yyyy");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        String formattedDate = monthYearFormat.format(date);
        jsonGenerator.writeString(formattedDate);
    }
}

