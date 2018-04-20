package per.xc.entry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能实现类
 *
 * @version V1.0
 * @Author XiongCheng
 * @Date 2018/4/17 12:31.
 */
public class Importer {
    public static List<String> list = new ArrayList<>();

    /**
     * 通过递归的方式找到jar文件.
     * Main processing method. This method is used to find jar file by recursion way.
     * @param filePath
     */
    public static void findJars(String filePath) {
        File file = new File(filePath);
        File fileList[] = file.listFiles();
        for (File currentFile : fileList) {
            if(currentFile.isDirectory()) {
                findJars(currentFile.getAbsolutePath());
            }else {
                if(isJar(currentFile)){
                    importJars(currentFile);
                }else {
                    return;
                }
            }
        }
    }

    /**
     *  生成批处理文件，通过运行批处理文件，实现自动导入
     *  generate install command file ,and run this file
     * @param file
     */
    public static void importJars(File file) {
        String filePath = file.getAbsolutePath();
        String param[] = getArtifactGroupAndVersionFromPath(filePath);
        String execString = "call mvn install:install-file -DgroupId=" + param[0] +
                            " -DartifactId=" + param[1] + " -Dversion=" + param[2] +
                            " -Dpackaging=jar -Dfile=" + filePath;
        System.out.println(execString);
        list.add(execString);
    }
    /**
     * 从标准maven本地库中找到groupId,artifactId,version
     * From local MavenRepository ,Find out groupId artifactid and version
     * @param path jar文件的路径   jar file path
     * @return index:0 groupId
     *         index:1 artifactId
     *         index:2 version
     */
    public static String[] getArtifactGroupAndVersionFromPath(String path) {
        String pathSplit[] = path.split("\\\\");
        int version = pathSplit.length - 2;
        int artifactID = pathSplit.length - 3;
        String result[] = new String[3];
        result[1] = pathSplit[artifactID];
        result[2] = pathSplit[version];
        pathSplit[version] = "#";
        pathSplit[artifactID] = "#";
        StringBuilder stringBuilder = new StringBuilder(pathSplit[2]);
        for (int i = 3; i < pathSplit.length; i++) {
            if(!pathSplit[i].equals("#")) {
                stringBuilder.append('.').append(pathSplit[i]);
            }else {
                result[0] = stringBuilder.toString();
                return result;
            }
        }
        return result;
    }

    /**
     * 判断文件是否为jar文件
     *
     * @param file
     * @return
     */
    public static boolean isJar(File file) {
        String split[] = file.getName().split("\\.");
        return "jar".equals(split[split.length - 1]) ? true: false;
    }

    /**
     * 将命令写入文件
     * Write the result string to file
     * @param saveFilePath
     * @throws IOException
     */
    public static void writeFile(String saveFilePath) throws IOException {
        FileWriter writer = new FileWriter(saveFilePath);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        for (String s : list) {
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        writer.close();
    }
}
