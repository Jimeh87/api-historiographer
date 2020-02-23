package com.jimrennie.apihistoriographer.service.util;

import lombok.Value;

@Value(staticConstructor = "of")
public class Pair<T, U> {
	private T left;
	private U right;
}
