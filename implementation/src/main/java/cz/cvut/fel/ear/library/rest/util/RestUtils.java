package cz.cvut.fel.ear.library.rest.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;


public class RestUtils {

    public static HttpHeaders createLocationHeaderFromCurrentUri(String path, Object... uriVariableValues) {
        assert path != null;

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(path)
                .buildAndExpand(uriVariableValues)
                .toUri();
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LOCATION, location.toASCIIString());

        return headers;
    }

}

