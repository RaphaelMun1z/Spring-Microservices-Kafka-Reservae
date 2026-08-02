package notification_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailAssetService {
    private final Resource logoResource;
    private final ResourceLoader resourceLoader;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public EmailAssetService(
        @Value("${notification.email.logo-classpath}") Resource logoResource,
        ResourceLoader resourceLoader
    ) {
        this.logoResource = logoResource;
        this.resourceLoader = resourceLoader;
    }

    public String getLogoDataUri() {
        return readAsDataUri(logoResource, "email-logo");
    }

    public String getAssetDataUri(String classpathLocation) {
        if (classpathLocation == null || classpathLocation.isBlank()) {
            return "";
        }

        Resource resource = resourceLoader.getResource(classpathLocation);
        return readAsDataUri(resource, classpathLocation);
    }

    private String readAsDataUri(Resource resource, String cacheKey) {
        return cache.computeIfAbsent(cacheKey, key -> {
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);

                String mimeType = resolveMimeType(resource.getFilename());

                return "data:%s;base64,%s".formatted(mimeType, base64);
            } catch (IOException exception) {
                throw new IllegalStateException(
                    "Failed to load email asset: " + cacheKey,
                    exception
                );
            }
        });
    }

    private String resolveMimeType(String filename) {
        if (filename == null) {
            return "image/png";
        }

        String lowerCaseFilename = filename.toLowerCase();

        if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        if (lowerCaseFilename.endsWith(".gif")) {
            return "image/gif";
        }

        if (lowerCaseFilename.endsWith(".webp")) {
            return "image/webp";
        }

        return "image/png";
    }
}