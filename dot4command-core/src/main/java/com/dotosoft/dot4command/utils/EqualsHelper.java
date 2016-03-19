package com.dotosoft.dot4command.utils;

public final class EqualsHelper {

	public static boolean equals(final Object x, final Object y) {
		return x == y || (x != null && y != null && x.equals(y));
	}

	private EqualsHelper() {
	}

}
