package utilites;

public class CommandUtility {

    public static boolean isNewMode(String text){
        return text.matches("^/newMode\".*?\"");
    }

    public static String getNewModeName(String text){
        final String[] split = text.split("\"");
        return split[1];
    }
}
