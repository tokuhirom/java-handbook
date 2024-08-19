package com.example.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

interface Pet {
    String berk();
}

class Human {
    Pet pet;

    String berkPet() {
        return pet.berk();
    }
}

@ExtendWith(MockitoExtension.class)
public class MockitoAnnotationTest {
    @Mock
    Pet pet;

    @InjectMocks
    private Human underTest;

    @Test
    public void test() {
        when(pet.berk()).thenReturn("foo");

        assertThat(underTest.berkPet())
                .isEqualTo("foo");
    }
}
