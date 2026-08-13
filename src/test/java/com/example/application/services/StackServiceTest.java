package com.example.application.services;

import com.example.application.entities.StackEntity;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.StackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class StackServiceTest {

    @Mock
    private StackRepository stackRepository;

    @Mock
    private ObserverRepository observerRepository;

    private StackService stackService;

    @BeforeEach
    void setUp() {
        stackService = new StackService(stackRepository, observerRepository);
    }

    @Test
    void deleteStackDeletesObserversBelongingToTheStackBeforeDeletingTheStackItself() {
        // Observers have a NOT NULL foreign key to their stack, so the stack must not be deleted
        // before its observers - otherwise the deletion would violate that constraint.
        StackEntity stack = new StackEntity();
        stack.setId(42);

        stackService.deleteStack(stack);

        InOrder order = inOrder(observerRepository, stackRepository);
        order.verify(observerRepository).deleteByStack(stack);
        order.verify(stackRepository).delete(stack);
        verifyNoMoreInteractions(observerRepository, stackRepository);
    }
}
