package account.mapper;

import account.domain.Role;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.*;

public class RoleNameSerializer extends JsonSerializer<Set<Role>> {
    @Override
    public void serialize(Set<Role> roles, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        List<Role> sortedRoles = new ArrayList<>(roles);
        Collections.sort(sortedRoles, Comparator.comparing(Role::getName));

        gen.writeStartArray();
        for (Role role : sortedRoles) {
            gen.writeString(role.getName());
        }
        gen.writeEndArray();
    }
}
