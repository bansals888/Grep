package Mypackage;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task1 implements Runnable{
    public Path path;
    public String regex;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Task1(Path path, String regex){
        this.path=path;
        this.regex=regex;
    }
    @Override
    public void run() {
        String text= null;
        try {
            text = new String(Files.readAllBytes(Paths.get(path.toString())));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher=pattern.matcher(text);
        int prevend=-1,prestart=-1;
        ArrayList<Integer> Array=new ArrayList<Integer>();
        while (matcher.find()){
            int start=matcher.start();
            int end=matcher.end();
            int x=start,y=end;
            for(int i=start-1;i>=0;i--){
                if(text.charAt(i)=='\n')break;
                start--;
            }
            for(int i=end;i<text.length();i++){
                if(text.charAt(i)=='\n')break;
                end++;
            }
            if(prevend!=end){
                if(prevend!=-1){
                    String ans="";
                    int temp=prestart;
                    for(int i=0;i<Array.size();i+=2){
                        ans+=text.substring(temp,Array.get(i));
                        ans+=ANSI_RED;
                        ans+=text.substring(Array.get(i),Array.get(i+1));
                        ans+=ANSI_RESET;
                        temp=Array.get(i+1);
                    }
                    ans+=text.substring(Array.get(Array.size()-1),prevend);
                    System.out.println(ans);
                    Array.clear();
                }
                Array.add(x);
                Array.add(y);
                prevend=end;
                prestart=start;
            }
            else{
                Array.add(x);
                Array.add(y);
            }
        }
        if(prevend!=-1){
            String ans="";
            int temp=prestart;
            for(int i=0;i<Array.size();i+=2){
                ans+=text.substring(temp,Array.get(i));
                ans+=ANSI_RED;
                ans+=text.substring(Array.get(i),Array.get(i+1));
                ans+=ANSI_RESET;
                temp=Array.get(i+1);
            }
            ans+=text.substring(Array.get(Array.size()-1),prevend);
            System.out.println(ans);
            Array.clear();
        }
    }
}