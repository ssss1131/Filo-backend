package kz.ssss.filo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtil {

    public static final String FOLDER_DELIMITER = "/";

    private static final String USER_FILES_FORMAT = "user-%d-files%s%s";

    public static String getFullPath(Long userId, String path) {
        return String.format(USER_FILES_FORMAT, userId, FOLDER_DELIMITER, path);
    }

    public static String removeUserPrefix(String fullPath, Long userId) {
        String prefix = "user-" + userId + "-files" + FOLDER_DELIMITER;
        return fullPath.startsWith(prefix) ? fullPath.substring(prefix.length()) : fullPath;
    }

    public static String getName(String path) {
        String[] dividedByFolderNames = path.split(FOLDER_DELIMITER);
        return dividedByFolderNames[dividedByFolderNames.length - 1];
    }

    public static boolean isCorrectPath(String path) {
        if (path == null) {
            return false;
        }

        if (path.isEmpty()) {
            return true;
        }

        if (!path.endsWith(FOLDER_DELIMITER)) {
            return false;
        }

        String pattern = "^[\\p{L}\\p{N}_\\-. ]+(\\/[\\p{L}\\p{N}_\\-. ]+)*\\/?$";
        return path.matches(pattern);
    }


    public static String getPath(String path) {
        int index = path.lastIndexOf(FOLDER_DELIMITER);
        return path.substring(0, index) + FOLDER_DELIMITER;
    }

    public static boolean isFile(String path) {
        return !path.endsWith(FOLDER_DELIMITER);
    }

    public static String getRelativePath(String prefix, String path) {
        if (path.startsWith(prefix)) {
            String folder = getName(prefix) + FOLDER_DELIMITER;
            return path.substring(prefix.length() - folder.length());
        }
        return path;
    }
}
