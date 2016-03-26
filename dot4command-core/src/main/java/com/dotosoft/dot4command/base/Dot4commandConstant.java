package com.dotosoft.dot4command.base;

public class Dot4commandConstant {
	public static final String EVALUATE_REGEX_STRING = "(?=[-/*+][!=&|][=&|])|(?<=[-/*+][!=&|][=&|])";
	public static final String EXPRESSION_REGEX_STRING = "([\"'])((?:(?=(\\\\?))\\3.)*?)\\1";
}
