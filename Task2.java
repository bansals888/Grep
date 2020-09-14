package Mypackage;


import java.io.File;
import java.io.FileFilter;
import java.nio.file.PathMatcher;
import java.util.ArrayList;


public class Task2 implements Runnable{
    public String location;
    public ArrayList<PathMatcher> AllPathMatchers;
    public  String regex;
    static ThreadPool FileTP;
    static ThreadPool DirectoryTP;
    public Task2(String location, ArrayList<PathMatcher> AllPathMatchers,String regex,ThreadPool Filetp,ThreadPool Directorytp){
        this.location=location;
        this.AllPathMatchers=AllPathMatchers;
        this.regex=regex;
        FileTP=Filetp;
        DirectoryTP=Directorytp;
    }
    @Override
    public void run() {
        try {
//            System.out.println(location);
            FUN(location,AllPathMatchers,regex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void FUN(String location,ArrayList<PathMatcher> AllPathMatchers,String regex)throws Exception{
        File f=new File(location);
//        System.out.println(f);
        if(f.isFile()){
            for(int j=0;j<AllPathMatchers.size();j++){
                if (AllPathMatchers.get(j).matches(f.toPath())) {
                    FileTP.execute(new Task1(f.toPath(),regex));
                    break;
                }
            }

        }
        else{
            File[] files = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return !file.isHidden();
                }
            });
            for(int i=0;i<files.length;i++){
                for(int j=0;j<AllPathMatchers.size();j++){
                    if (AllPathMatchers.get(j).matches(files[i].toPath())) {
                        Task2 t=new Task2(files[i].toString(),AllPathMatchers,regex,FileTP,DirectoryTP);
                        DirectoryTP.execute(t);
                        break;
                    }
                }
            }
        }
    }
}