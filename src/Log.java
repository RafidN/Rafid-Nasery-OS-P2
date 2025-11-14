public class Log {
    private Log()
    {

    }

    public static synchronized void p(String role, int id, String ctx, String msg)
    {
        System.out.printf("%s %d [%s]: %s%n", role, id, ctx, msg);
    }
}
