package user_profile_service.services.dataHandler;

import org.springframework.stereotype.Component;
import user_profile_service.dtos.res.UserProfileResponseDTO;
import user_profile_service.entities.UserProfile;

@Component
public class UserProfileMapper {

    public UserProfileResponseDTO toResponseDTO(
        UserProfile profile
    ) {
        return new UserProfileResponseDTO(
            profile.getId(),
            profile.getFullName(),
            profile.getEmail(),
            profile.getDocument()
        );
    }
}