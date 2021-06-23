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

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

interface PropertyParser<T> {
	
	PropertyParser<Boolean> BOOLEAN_PARSER = new BooleanParser();
	PropertyParser<String> STRING_PARSER = new StringParser();
	PropertyParser<Long> LONG_PARSER = new LongParser();
	PropertyParser<Integer> INT_PARSER = new IntParser();

	static <T extends Enum<T>> PropertyParser<T> enumParser(Class<T> enumClass) {
		return new EnumParser<>(enumClass);
	}

	T parse(String value);
	
	class BooleanParser implements PropertyParser<Boolean> {
		@Override
		public Boolean parse(String value) {
			if ("false".equalsIgnoreCase(value)) {
				return false;
			}
			if ("true".equalsIgnoreCase(value)) {
				return true;
			}
			throw new IllegalArgumentException("Cannot parse boolean value: \"" + value + "\"");
		}
	};
	
	class StringParser implements PropertyParser<String> {
		@Override
		public String parse(String value) {
			return value;
		}
	};

	class LongParser implements PropertyParser<Long> {
		@Override
		public Long parse(String value) {
			return Long.valueOf(value);
		}
	}
	
	class IntParser implements PropertyParser<Integer> {
		@Override
		public Integer parse(String value) {
			return Integer.valueOf(value);
		}
	}

	class EnumParser<T extends Enum<T>> implements PropertyParser<T> {
		private final Class<T> enumClass;

		public EnumParser(Class<T> enumClass) {
			this.enumClass = requireNonNull(enumClass);
		}

		@Override
		public T parse(String value) {
			return Arrays.stream(enumClass.getEnumConstants())
					.filter(it -> it.name().equalsIgnoreCase(value))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Unknown " + enumClass.getSimpleName() + " value " + value));
		}
	}
}
