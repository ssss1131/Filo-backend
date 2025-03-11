package kz.ssss.filo.util;

import java.util.Objects;

public class PathUtil {

    public static final String FOLDER_DELIMITER = "/";

    private static final String USER_FILES_FORMAT = "user-%d-files%s%s";

    public static String getFullPath(Long userId, String path) {
        if (Objects.equals(path, FOLDER_DELIMITER)) {
            return String.format(USER_FILES_FORMAT, userId, FOLDER_DELIMITER, "");
        }

        return String.format(USER_FILES_FORMAT, userId, FOLDER_DELIMITER, path);
    }

    public static String removeUserPrefix(String fullPath, Long userId) {
        String prefix = "user-" + userId + "-files/";
        return fullPath.startsWith(prefix) ? fullPath.substring(prefix.length()) : fullPath;
    }

    public static String removePathFromName(String objectName) {
        String[] dividedByFolderNames = objectName.split(FOLDER_DELIMITER);
        return dividedByFolderNames[dividedByFolderNames.length - 1];
    }
}
