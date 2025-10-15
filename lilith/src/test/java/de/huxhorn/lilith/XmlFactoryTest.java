/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlFactoryTest {

    @Test
    void correctInputFactoryIsObtained() {
        String factoryClassName = XMLInputFactory.newFactory().getClass().getName();
        assertTrue(factoryClassName.startsWith("com.ctc.wstx.stax"));
    }

    @Test
    void correctOutputFactoryIsObtained() {
        String factoryClassName = XMLOutputFactory.newFactory().getClass().getName();
        assertTrue(factoryClassName.startsWith("com.ctc.wstx.stax"));
    }
}
