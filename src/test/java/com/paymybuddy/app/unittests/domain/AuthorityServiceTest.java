package com.paymybuddy.app.unittests.domain;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.repository.AuthorityRepository;
import com.paymybuddy.app.domain.service.AuthorityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorityServiceTest {

    @InjectMocks
    private AuthorityService authorityService;

    @Mock
    private AuthorityRepository authorityRepository;

    @Test
    public void should_returnSavedAuthority_whenCreateAuthority() {
        when(authorityRepository.save(any())).thenReturn(getFakeAuthorityEntity());

        AuthorityEntity authorityEntity = authorityService.createAuthority(1);

        assertThat(authorityEntity).isNotNull();
        assertThat(authorityEntity.getAuthority()).isEqualTo("USER");
        verify(authorityRepository, times(1)).save(any());

    }

    private AuthorityEntity getFakeAuthorityEntity() {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUserId(1);
        authorityEntity.setAuthority("USER");
        return authorityEntity;
    }
}
