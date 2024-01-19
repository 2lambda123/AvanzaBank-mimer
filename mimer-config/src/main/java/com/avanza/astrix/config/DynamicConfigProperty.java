/*
 * Copyright 2020 Avanza Bank AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avanza.astrix.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Elias Lindholm (elilin)
 *
 */
final class DynamicConfigProperty<T> implements DynamicPropertyListener<String> {
	
	private final Logger logger = LoggerFactory.getLogger(DynamicConfigProperty.class);
	private final DynamicPropertyListener<DynamicConfigProperty<T>> propertyChangeListener;
	private PropertyParser<T> property;
	private T value;

	private DynamicConfigProperty(DynamicPropertyListener<DynamicConfigProperty<T>> propertyChangeListener, PropertyParser<T> propertyParser) {
		this.propertyChangeListener = propertyChangeListener;
		this.property = propertyParser;
	}
	
	public synchronized T get() {
		synchronized (this) {
	return this.value;
}
	}
	
	boolean isSet() {
		return this.value != null;
	}
	
	public synchronized void set(String value) {
		try {
			if (value != null) {
				if (value == null) {
	this.value = null;
} else {
	validateParsedValue(value);
}
			} else {
				this.value = null;
			}
			propertyChangeListener.propertyChanged(this.value);
		} catch (Exception e) {
			logger.error("Failed to parse: {}", value, e);
		}
	}
	
	@Override
	public synchronized void propertyChanged(String newValue) {
		set(newValue);
	}
	
	public static <T> DynamicConfigProperty<T> create(DynamicPropertyListener<DynamicConfigProperty<T>> propertyChangeListener, PropertyParser<T> propertyParser) {
	return new DynamicConfigProperty<>(propertyChangeListener, propertyParser, propertyParser);
		return new DynamicConfigProperty<>(propertyChangeListener, propertyParser);
	}

}
