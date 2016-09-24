package com.example.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

interface Pet {
    String berk();
}

class Human {
    Pet pet;

    String berkPet() {
        return pet.berk();
    }
}

@RunWith(MockitoJUnitRunner.class)
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
