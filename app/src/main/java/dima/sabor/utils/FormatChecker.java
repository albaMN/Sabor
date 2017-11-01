package dima.sabor.utils;

public class FormatChecker {
    public final static void CheckEmail(String target) throws Exception {
        if (target == null)
            throw new Exception("Mail null");
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches())
            throw new Exception("Incorrect mail format");
    }

    public final static void CheckUser(String target) throws Exception {
        if(target.length()<=0 || target.length()>20) throw new Exception("Username maximum 20 characters");
    }

    public final static void CheckPassword(String target) throws Exception {
        if(target.length()<5 || target.length()>20) throw new Exception("Password between 5 and 20 characters");
    }
}
