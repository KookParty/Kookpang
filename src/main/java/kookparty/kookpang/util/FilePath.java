package kookparty.kookpang.util;

import java.io.File;

import jakarta.servlet.http.HttpServletRequest;

public class FilePath {
	public static String getSavePath(HttpServletRequest request) {
		File appPath = new File(request.getServletContext().getRealPath("/"));
		File webappsDir = appPath.getParentFile();
		File saveDir = new File(webappsDir, "upload");
		return saveDir.getAbsolutePath();
	}
}
