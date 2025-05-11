package kz.ssss.filo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtil {

    public static final String FOLDER_DELIMITER = "/";
    private static final String ROOT_PATH = "";

    private static final String USER_FILES_FORMAT = "user-%d-files%s%s";

    public static String getFullPath(Long userId, String path) {
        return String.format(USER_FILES_FORMAT, userId, FOLDER_DELIMITER, path);
    }

    public static String removeUserPrefix(String fullPath, Long userId) {
        String prefix = String.format(USER_FILES_FORMAT, userId, FOLDER_DELIMITER, "");
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
        if(index == -1 || index == 0){
            return ROOT_PATH;
        }
        return path.substring(0, index) + FOLDER_DELIMITER;
    }

    public static boolean isFile(String path) {
        return path!=null && !path.endsWith(FOLDER_DELIMITER);
    }

    public static String getRelativePath(String prefix, String path) {
        if (path.startsWith(prefix)) {
            String folder = getName(prefix) + FOLDER_DELIMITER;
            return path.substring(prefix.length() - folder.length());
        }
        return path;
    }

    public static boolean isValidDestinationFolder(String candidatePath, String excludedPath) {
        String parentPath = getParentPath(excludedPath);

        return !candidatePath.equals(parentPath)
               && !candidatePath.equals(excludedPath)
               && !candidatePath.startsWith(excludedPath);
    }

    public static String getParentPath(String path) {
        if (path.isEmpty() || path.equals(FOLDER_DELIMITER)) {
            return ROOT_PATH;
        }
        String trimmed = path.substring(0, path.length() - 1);
        int lastSlash = trimmed.lastIndexOf(FOLDER_DELIMITER);
        if (lastSlash == -1) {
            return ROOT_PATH;
        }
        return trimmed.substring(0, lastSlash + 1);
    }

}
