package com.myProject.myProject.service;

import com.myProject.myProject.repositories.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final EntityManager entityManager;
    private final UserService userService;

    @Transactional
    public void deleteRole(Integer userId) {
        // delete from user_roles
        String deleteUserRolesQuery = "DELETE FROM user_roles WHERE user_id = :userId";
        entityManager.createNativeQuery(deleteUserRolesQuery)
                .setParameter("userId", userId)
                .executeUpdate();

        String nullifyUserInLogsQuery = "UPDATE mylog SET user_id = NULL WHERE user_id = :userId";
        entityManager.createNativeQuery(nullifyUserInLogsQuery)
                .setParameter("userId", userId)
                .executeUpdate();

        // Теперь удаляем роль
        userService.deteleFromDb(userId);
    }

}
