package Mypackage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class GrepMain {
    static ThreadPool FileTP=new ThreadPool(4);
    static ThreadPool DirectoryTP=new ThreadPool(6);


    public static void match(String glob, String location, String regex) throws IOException {

        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
                glob);
        Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path,
                                             BasicFileAttributes attrs) throws IOException {
                if (pathMatcher.matches(path)) {
                    Task1 t=new Task1 (path,regex);
                    try {
                        FileTP.execute(t);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }


    public static void match_parallel(String glob, String location, String regex) throws Exception {
        String cur="glob:";
        ArrayList<PathMatcher> AllPathMatchers=new ArrayList<PathMatcher>();
        for(int i=0;i<glob.length();i++){
            if(i>0&&glob.charAt(i)=='/') {
                AllPathMatchers.add(FileSystems.getDefault().getPathMatcher(cur));
            }
            cur+=glob.charAt(i);
        };

        AllPathMatchers.add(FileSystems.getDefault().getPathMatcher(cur));
        Task2 t=new Task2(location,AllPathMatchers,regex,FileTP,DirectoryTP);
        DirectoryTP.execute(t);
    }

    public static  void main(String args[]) throws Exception {
        long StartTime=  System.currentTimeMillis();

        String path="/";
        String regex="rew";
        String glob="/Users/bansals/CompanyWork/Grep/rand_Files/**";
        File f=new File(glob);
        glob=f.getCanonicalPath();


        match_parallel(glob,path,regex);
        DirectoryTP.stop();
        FileTP.stop();

        long EndTime = System.currentTimeMillis();
        System.out.println(EndTime-StartTime);
    }
}
