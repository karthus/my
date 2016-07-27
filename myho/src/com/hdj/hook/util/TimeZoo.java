package com.hdj.hook.util;

import java.util.Locale;
import java.util.TimeZone;

import com.hdj.hook.mode.TimeZooMode;

public class TimeZoo {
	

	public static TimeZooMode get() {
		TimeZooMode timeZooMode = new TimeZooMode();
		TimeZone timeZone = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		String language = locale.getLanguage();
		timeZooMode.setId(timeZone.getID());
		timeZooMode.setCountry(country);
		timeZooMode.setLanguage(language);
		return timeZooMode;
	}

}
