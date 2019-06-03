import java.io.IOException;

public class LoadingBar
{

    public void loadingBar(int count)
    {
        try
        {
            var anim = "|/-\\";
            var data = "\r" + anim.charAt(count % anim.length()) + " " + count;
            System.out.write(data.getBytes());
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}
