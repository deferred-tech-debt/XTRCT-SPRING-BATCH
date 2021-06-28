package com.contribute.xtrct.presentation;

import java.util.Locale;

/**
 * Factory to allow assisted injection with Spring for creating PresentationMarshaller objects
 */
public interface PresentationMarshallerFactory {

    PresentationMarshaller create(Locale locale);
}
