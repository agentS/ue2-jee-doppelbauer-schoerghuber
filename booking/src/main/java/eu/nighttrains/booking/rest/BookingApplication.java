package eu.nighttrains.booking.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Timetable API",
                version = "0.0.0",
                contact = @Contact(
                        name = "Alexander Doppelbauer"
                ),
                license = @License(
                        name = "GPLv3",
                        url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
                )
        ),
        //servers = @Server(url = "http://127.0.0.1:8080", description = "Booking API development server"),
        tags = @Tag(name = BookingApplication.OPEN_API_TAG_NAME_BOOKING, description = "Timetable-related endpoints")
)
public class BookingApplication extends Application {
    public static final String OPEN_API_TAG_NAME_BOOKING = "booking";
}
