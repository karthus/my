package com.hdj.hook.util;

import com.google.gson.Gson;

public class GsonUtil {

	public static Gson gson;

	public static Gson getInstance() {
		if (gson == null)
			gson = new Gson();
		return gson;

	}

}
