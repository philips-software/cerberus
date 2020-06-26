package com.philips.swcoe.cerberus.hounds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;
import java.io.IOException;

class BaseCommandTest extends CerberusBaseTest {

    @BeforeEach
    public void beforeEach() {
        super.setUpStreams();
    }

    @AfterEach
    public void afterEach() {
        super.restoreStreams();
    }

    @Test
    void testWritingStringToUI() throws IOException {
        BaseCommand baseCommand = new BaseCommand();
        baseCommand.writeToUI("We are the legends");
        assertTrue(getModifiedOutputStream().toString().contains("We are the legends"));
    }

    @Test
    void testThrowingIOException() throws IOException {
        BaseCommand mockBaseCommand = mock(BaseCommand.class);
        Mockito.doThrow(new IOException()).when(mockBaseCommand).writeToUI("mock message");
        assertThrows(IOException.class, () -> {
            mockBaseCommand.writeToUI("mock message");
        });
    }

    @Test
    void testDoesNotThrowIOException() throws IOException {
        BaseCommand baseCommand = new BaseCommand();
        assertDoesNotThrow(() -> {
            baseCommand.writeToUI("Any message");
        });
    }
}