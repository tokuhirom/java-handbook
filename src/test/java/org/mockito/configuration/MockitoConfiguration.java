package org.mockito.configuration;

import org.mockito.stubbing.Answer;

// https://solidsoft.wordpress.com/2012/07/02/beyond-the-mockito-refcard-part-1-a-better-error-message-on-npe-with-globally-configured-smartnull/
// See org.mockito.internal.configuration.ClassPathLoader
public class MockitoConfiguration extends DefaultMockitoConfiguration {
    public Answer<Object> getDefaultAnswer() {
//        return new ReturnsSmartNulls();
        return invocation -> {
            throw new UnsupportedOperationException("Unknown operation: " + invocation);
        };
    }
}
