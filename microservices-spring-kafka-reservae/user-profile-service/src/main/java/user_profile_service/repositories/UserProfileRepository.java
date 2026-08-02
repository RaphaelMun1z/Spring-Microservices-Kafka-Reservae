package user_profile_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import user_profile_service.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    boolean existsByEmailIgnoreCaseAndIdNot(
        String email,
        String id
    );

    boolean existsByDocumentAndIdNot(
        String document,
        String id
    );
}